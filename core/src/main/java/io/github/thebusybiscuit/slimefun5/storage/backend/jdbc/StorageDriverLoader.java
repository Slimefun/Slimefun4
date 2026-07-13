package io.github.thebusybiscuit.slimefun5.storage.backend.jdbc;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.Driver;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import javax.annotation.Nonnull;

import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;

/**
 * Loads a JDBC {@link Driver} for a storage backend without shading the driver into the plugin jar.
 *
 * <p>
 * The universal jar ships without H2/MySQL bundled (they would push it past the file-size limit of
 * some publishing platforms). Instead this loader:
 * <ol>
 * <li>first tries the current classpath - so unit tests (which put H2 on the test classpath) and any
 * server that already provides the driver work offline with no download; then</li>
 * <li>downloads the driver jar(s) from Maven Central once into {@code plugins/Slimefun/libraries/},
 * caches them, and loads the driver through a child {@link URLClassLoader}.</li>
 * </ol>
 *
 * <p>
 * The driver is instantiated and used via {@link Driver#connect(String, java.util.Properties)} directly
 * rather than {@link java.sql.DriverManager}, because DriverManager only surfaces drivers loaded by the
 * caller's own classloader - a driver loaded through a child loader would be invisible to it.
 */
final class StorageDriverLoader {

    private static final String MAVEN_CENTRAL = "https://repo1.maven.org/maven2/";

    // Cached per driver class name so repeated backend constructions don't re-download or re-load.
    private static final Map<String, Driver> CACHE = new HashMap<>();

    private StorageDriverLoader() {}

    /**
     * Returns a ready {@link Driver} instance for {@code driverClass}, downloading the given Maven
     * coordinates if the class isn't already reachable on the classpath.
     *
     * @param driverClass
     *            fully-qualified JDBC driver class (e.g. {@code "org.h2.Driver"})
     * @param mavenCoords
     *            {@code group:artifact:version} coordinates to fetch when the driver isn't on the
     *            classpath (the driver plus any of its own runtime dependencies)
     */
    @Nonnull
    static synchronized Driver loadDriver(@Nonnull String driverClass, @Nonnull List<String> mavenCoords) {
        Driver cached = CACHE.get(driverClass);
        if (cached != null) {
            return cached;
        }

        // 1. Already on the classpath (unit tests, or an admin-provided driver)? Use it directly.
        try {
            Driver direct = (Driver) Class.forName(driverClass).getConstructor().newInstance();
            CACHE.put(driverClass, direct);
            return direct;
        } catch (ClassNotFoundException notOnClasspath) {
            // Expected in production (driver not shaded) - fall through to download + child-load.
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Storage driver " + driverClass + " is present but could not be instantiated", e);
        }

        // 2. Download (if needed) into plugins/Slimefun/libraries and load through a child loader.
        try {
            File libraries = new File(Slimefun.instance().getDataFolder(), "libraries");

            if (!libraries.isDirectory() && !libraries.mkdirs()) {
                throw new IllegalStateException("Could not create the storage libraries directory: " + libraries);
            }

            List<URL> urls = new ArrayList<>();
            for (String coord : mavenCoords) {
                urls.add(ensureLibrary(libraries, coord).toURI().toURL());
            }

            URLClassLoader loader = new URLClassLoader(urls.toArray(new URL[0]), StorageDriverLoader.class.getClassLoader());
            Driver driver = (Driver) Class.forName(driverClass, true, loader).getConstructor().newInstance();
            CACHE.put(driverClass, driver);
            return driver;
        } catch (Exception e) {
            throw new IllegalStateException("Could not obtain the storage driver " + driverClass
                    + " (download the jar into plugins/Slimefun/libraries/ manually if this server has no internet access)", e);
        }
    }

    /** Ensures {@code <artifact>-<version>.jar} for the coordinate exists in the libraries dir, downloading it if not. */
    @Nonnull
    private static File ensureLibrary(@Nonnull File libraries, @Nonnull String coord) throws Exception {
        String[] parts = coord.split(":");
        if (parts.length != 3) {
            throw new IllegalArgumentException("Malformed Maven coordinate: " + coord);
        }

        String group = parts[0];
        String artifact = parts[1];
        String version = parts[2];
        String fileName = artifact + '-' + version + ".jar";

        File target = new File(libraries, fileName);
        if (target.isFile() && target.length() > 0) {
            return target;
        }

        String url = MAVEN_CENTRAL + group.replace('.', '/') + '/' + artifact + '/' + version + '/' + fileName;
        Slimefun.logger().log(Level.INFO, "Downloading storage driver: {0}", fileName);

        File tmp = new File(libraries, fileName + ".tmp");
        try (InputStream in = new URL(url).openStream()) {
            Files.copy(in, tmp.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }

        // Atomic move so a partial/interrupted download never leaves a corrupt jar at the real name.
        Files.move(tmp.toPath(), target.toPath(), StandardCopyOption.REPLACE_EXISTING);
        return target;
    }
}
