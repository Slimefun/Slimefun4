package io.github.thebusybiscuit.slimefun5.core.services;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.logging.Level;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.plugin.Plugin;

import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import io.github.bakedlibs.dough.common.CommonPatterns;
import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun5.utils.JsonUtils;

/**
 * This Class represents a Metrics Service that sends data to https://bstats.org/
 * This data is used to analyse the usage of this {@link Plugin}.
 * <p>
 * You can find more info in the README file of this Project on GitHub. <br>
 * <b>Note:</b> To start the metrics you will need to be calling {@link #start()}
 *
 * @author WalshyDev
 */
public class MetricsService {

    /**
     * The URL pointing towards the GitHub API.
     */
    private static final String API_URL = "https://api.github.com/";

    /**
     * The Name of our repository - Version 2 of this repo (due to big breaking changes)
     */
    private static final String REPO_NAME = "SlimefunMetrics";

    /**
     * The name of the metrics jar file.
     */
    private static final String JAR_NAME = "SlimefunMetrics";

    /**
     * The URL pointing towards the /releases/ endpoint of our
     * Metrics repository
     */
    private static final String RELEASES_URL = API_URL + "repos/Slimefun5/" + REPO_NAME + "/releases/latest";

    /**
     * The URL pointing towards the download location for a
     * GitHub release of our Metrics repository
     */
    private static final String DOWNLOAD_URL = "https://github.com/Slimefun5/" + REPO_NAME + "/releases/download";

    /**
     * The connect/read timeout (in milliseconds) for our GitHub requests.
     */
    private static final int TIMEOUT = 5_000;

    private final Slimefun plugin;
    private final File parentFolder;
    private final File metricsModuleFile;

    private URLClassLoader moduleClassLoader;
    private String metricVersion = null;
    private boolean hasDownloadedUpdate = false;

    /**
     * This constructs a new instance of our {@link MetricsService}.
     *
     * @param plugin
     *            Our {@link Slimefun} instance
     */
    public MetricsService(@Nonnull Slimefun plugin) {
        this.plugin = plugin;
        this.parentFolder = new File(plugin.getDataFolder(), "cache" + File.separatorChar + "modules");

        if (!parentFolder.exists()) {
            parentFolder.mkdirs();
        }

        this.metricsModuleFile = new File(parentFolder, JAR_NAME + ".jar");
    }

    /**
     * This method loads the metric module and starts the metrics collection.
     */
    public void start() {
        if (!metricsModuleFile.exists()) {
            plugin.getLogger().info(JAR_NAME + " does not exist, downloading...");

            // Fall back to the bundled copy when GitHub has no release to download (avoids a 404).
            if (!download(getLatestVersion()) && !extractBundledModule()) {
                plugin.getLogger().warning("Failed to start metrics as the file could not be downloaded.");
                return;
            }
        }

        try {
            /*
             * Load the jar file into a child class loader using the Slimefun
             * PluginClassLoader as a parent.
             */
            moduleClassLoader = URLClassLoader.newInstance(new URL[] { metricsModuleFile.toURI().toURL() }, plugin.getClass().getClassLoader());
            Class<?> metricsClass = moduleClassLoader.loadClass("dev.walshy.sfmetrics.MetricsModule");

            metricVersion = metricsClass.getPackage().getImplementationVersion();

            /*
             * If it has not been newly downloaded, auto-updates are enabled
             * AND there's a new version then cleanup, download and start
             */
            if (!hasDownloadedUpdate && hasAutoUpdates() && checkForUpdate(metricVersion)) {
                plugin.getLogger().info("Cleaned up, now re-loading Metrics-Module!");
                start();
                return;
            }

            // Finally, we're good to start this.
            Method start = metricsClass.getDeclaredMethod("start");
            String version = metricsClass.getPackage().getImplementationVersion();

            // This is required to be sync due to bStats.
            Slimefun.runSync(() -> {
                try {
                    start.invoke(null);
                    plugin.getLogger().info("Metrics build #" + version + " started.");
                } catch (InvocationTargetException e) {
                    plugin.getLogger().log(Level.WARNING, "An exception was thrown while starting the metrics module", e.getCause());
                } catch (Exception | LinkageError e) {
                    plugin.getLogger().log(Level.WARNING, "Failed to start metrics.", e);
                }
            });
        } catch (Exception | LinkageError e) {
            plugin.getLogger().log(Level.WARNING, "Failed to load the metrics module. Maybe the jar is corrupt?", e);
        }
    }

    /**
     * Extracts the metrics module that is bundled inside the Slimefun jar to the cache folder.
     * <p>
     * This is the offline fallback used when the module could not be downloaded from GitHub (for
     * example when no release is published on the Metrics repository), so that bStats metrics still
     * work without any manual setup.
     *
     * @return Whether the bundled module was successfully extracted.
     */
    private boolean extractBundledModule() {
        try (InputStream input = Slimefun.class.getClassLoader().getResourceAsStream(JAR_NAME + ".jar")) {
            if (input == null) {
                return false;
            }

            Files.copy(input, metricsModuleFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            plugin.getLogger().info("Using bundled " + JAR_NAME + " module (offline fallback).");
            return true;
        } catch (IOException e) {
            plugin.getLogger().log(Level.WARNING, "Failed to extract the bundled metrics module: {0}", e.getMessage());
            return false;
        }
    }

    /**
     * This will close the child {@link ClassLoader} and mark all the resources held under this no longer
     * in use, they will be cleaned up the next GC run.
     */
    public void cleanUp() {
        try {
            if (moduleClassLoader != null) {
                moduleClassLoader.close();
            }
        } catch (IOException e) {
            plugin.getLogger().log(Level.WARNING, "Could not clean up module class loader. Some memory may have been leaked.");
        }
    }

    /**
     * Checks for a new update and compares it against the current version.
     * If there is a new version available then this returns true.
     *
     * @param currentVersion
     *            The current version which is being used.
     *
     * @return if there is an update available.
     */
    public boolean checkForUpdate(@Nullable String currentVersion) {
        if (currentVersion == null || !CommonPatterns.NUMERIC.matcher(currentVersion).matches()) {
            return false;
        }

        int latest = getLatestVersion();

        if (latest > Integer.parseInt(currentVersion)) {
            return download(latest);
        }

        return false;
    }

    /**
     * Gets the latest version available as an int.
     * This is an internal method used by {@link #checkForUpdate(String)}.
     * If it cannot get the version for whatever reason this will return 0, effectively always
     * being behind.
     *
     * @return The latest version as an integer or -1 if it failed to fetch.
     */
    private int getLatestVersion() {
        HttpURLConnection connection = null;

        try {
            connection = openConnection(RELEASES_URL);
            int statusCode = connection.getResponseCode();

            if (statusCode < 200 || statusCode >= 300) {
                return -1;
            }

            JsonElement element = JsonUtils.parseString(readBody(connection));

            return element.getAsJsonObject().get("tag_name").getAsInt();
        } catch (IOException | JsonParseException e) {
            plugin.getLogger().log(Level.WARNING, "Failed to fetch latest builds for Metrics: {0}", e.getMessage());
            return -1;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    /**
     * Downloads the version specified to Slimefun's data folder.
     *
     * @param version
     *            The version to download.
     */
    private boolean download(int version) {
        File file = new File(parentFolder, "Metrics-" + version + ".jar");
        HttpURLConnection connection = null;

        try {
            plugin.getLogger().log(Level.INFO, "# Starting download of MetricsModule build: #{0}", version);

            if (file.exists()) {
                // Delete the file in case we accidentally downloaded it before
                Files.delete(file.toPath());
            }

            connection = openConnection(DOWNLOAD_URL + "/" + version + "/" + JAR_NAME + ".jar");
            int statusCode = connection.getResponseCode();

            if (statusCode >= 200 && statusCode < 300) {
                downloadToFile(connection, file);

                plugin.getLogger().log(Level.INFO, "Successfully downloaded {0} build: #{1}", new Object[] { JAR_NAME, version });

                // Replace the metric file with the new one
                cleanUp();
                Files.move(file.toPath(), metricsModuleFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

                metricVersion = String.valueOf(version);
                hasDownloadedUpdate = true;
                return true;
            } else {
                plugin.getLogger().log(Level.WARNING, "Failed to download the latest jar file from GitHub. Response code: {0}", statusCode);
            }
        } catch (JsonParseException e) {
            plugin.getLogger().log(Level.WARNING, "Failed to fetch the latest jar file from the builds page. Perhaps GitHub is down? Response: {0}", e.getMessage());
        } catch (IOException e) {
            plugin.getLogger().log(Level.WARNING, "Failed to replace the old metric file with the new one. Please do this manually! Error: {0}", e.getMessage());
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }

        return false;
    }

    /**
     * Returns the currently downloaded metrics version.
     * This <strong>can change</strong>! It may be null or an
     * older version before it has downloaded a newer one.
     *
     * @return The current version or null if not loaded.
     */
    @Nullable
    public String getVersion() {
        return metricVersion;
    }

    /**
     * Returns if the current server has metrics auto-updates enabled.
     *
     * @return True if the current server has metrics auto-updates enabled.
     */
    public boolean hasAutoUpdates() {
        return Slimefun.instance().getConfig().getBoolean("metrics.auto-update");
    }

    /**
     * Opens a configured {@link HttpURLConnection} to the given URL.
     * <p>
     * Java 8: we use {@link HttpURLConnection} instead of the Java 11 {@link java.net.http.HttpClient}
     * so the universal jar runs on legacy (1.8+) servers running Java 8.
     */
    @Nonnull
    private HttpURLConnection openConnection(@Nonnull String url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setConnectTimeout(TIMEOUT);
        connection.setReadTimeout(TIMEOUT);
        connection.setRequestProperty("User-Agent", "MetricsModule Auto-Updater");
        connection.setRequestProperty("Accept", "application/vnd.github.v3+json");
        return connection;
    }

    /**
     * Reads the full response body from the given connection as a UTF-8 String.
     */
    @Nonnull
    private static String readBody(@Nonnull HttpURLConnection connection) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder builder = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }

            return builder.toString();
        }
    }

    /**
     * Streams the response body of the given connection to the target file, logging download
     * progress in ~20% increments (mirrors the previous {@code BodySubscriber}-based progress monitor).
     */
    private void downloadToFile(@Nonnull HttpURLConnection connection, @Nonnull File file) throws IOException {
        long totalBytes = connection.getContentLengthLong();
        long bytesWritten = 0;
        int lastPercentPosted = 0;

        try (InputStream input = connection.getInputStream(); OutputStream output = new FileOutputStream(file)) {
            byte[] buffer = new byte[8192];
            int read;

            while ((read = input.read(buffer)) != -1) {
                output.write(buffer, 0, read);
                bytesWritten += read;

                if (totalBytes > 0) {
                    int percent = (int) (20 * Math.round((((double) bytesWritten / totalBytes) * 100) / 20));

                    if (percent != 0 && percent != lastPercentPosted) {
                        plugin.getLogger().info("# Downloading... " + percent + "% (" + bytesWritten + "/" + totalBytes + " bytes)");
                        lastPercentPosted = percent;
                    }
                }
            }
        }
    }
}
