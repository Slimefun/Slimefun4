package io.github.thebusybiscuit.slimefun5.core.guide.installer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.plugin.java.JavaPlugin;

import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;

/**
 * Repairs a downloaded addon jar so it actually runs on this fork — the same fixes the local
 * {@code run.ps1} orchestrator applies at copy time, but at install time:
 *
 * <ul>
 *   <li><b>Strip bundled core classes:</b> some addons shade the whole Slimefun core. A duplicate
 *   {@code Slimefun} class loaded by the addon's own classloader has null static state ("Slimefun
 *   instance is null"); duplicates in a shared API signature cause loader-constraint LinkageErrors.
 *   The core supplies every such class at runtime via the addon's {@code depend: [Slimefun]} link.</li>
 *   <li><b>Relocate {@code slimefun4} → {@code slimefun5}:</b> shaded deps (e.g. a metrics module)
 *   compiled against upstream Slimefun4 reference the old package, which does not exist here. An
 *   equal-length byte swap in the {@code .class} bytes fixes them.</li>
 * </ul>
 *
 * In-place via a temp file + atomic rename. Blocking — call off the main thread.
 */
final class AddonJarProcessor {

    private AddonJarProcessor() {}

    /** The core jar's class-entry names, read once (the set an addon must not duplicate). */
    private static volatile Set<String> coreClasses;

    /** Strips bundled core classes and relocates slimefun4 → slimefun5 in a single rewrite. */
    static void repair(@Nonnull File jar) {
        Set<String> core = coreClasses();
        byte[] from = "slimefun4".getBytes(StandardCharsets.UTF_8);
        byte[] to = "slimefun5".getBytes(StandardCharsets.UTF_8);
        File temp = new File(jar.getParentFile(), jar.getName() + ".repair.tmp");
        int stripped = 0;
        boolean swapped = false;

        try (ZipInputStream zin = new ZipInputStream(new FileInputStream(jar));
                ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(temp))) {
            ZipEntry entry;

            while ((entry = zin.getNextEntry()) != null) {
                String name = entry.getName();
                byte[] data = readAll(zin);

                if (name.endsWith(".class") && core.contains(name)) {
                    stripped++;
                    continue; // drop the bundled core duplicate
                }

                if (name.endsWith(".class")) {
                    for (int i = 0; i <= data.length - from.length; i++) {
                        boolean match = true;

                        for (int j = 0; j < from.length; j++) {
                            if (data[i + j] != from[j]) {
                                match = false;
                                break;
                            }
                        }

                        if (match) {
                            System.arraycopy(to, 0, data, i, to.length);
                            swapped = true;
                        }
                    }
                }

                zout.putNextEntry(new ZipEntry(name));
                zout.write(data);
                zout.closeEntry();
            }
        } catch (IOException e) {
            temp.delete();
            return; // a failed repair must not lose the download; leave the original in place
        }

        if ((stripped > 0 || swapped) && jar.delete() && temp.renameTo(jar)) {
            return;
        }

        temp.delete();
    }

    @Nonnull
    private static Set<String> coreClasses() {
        Set<String> cached = coreClasses;

        if (cached != null) {
            return cached;
        }

        Set<String> names = new HashSet<>();
        File coreJar = coreJar();

        if (coreJar != null && coreJar.exists()) {
            try (ZipFile zf = new ZipFile(coreJar)) {
                Enumeration<? extends ZipEntry> en = zf.entries();

                while (en.hasMoreElements()) {
                    String n = en.nextElement().getName();

                    if (n.endsWith(".class")) {
                        names.add(n);
                    }
                }
            } catch (IOException ignored) {
                // If we can't read the core jar, skip stripping (the swap still runs).
            }
        }

        coreClasses = names;
        return names;
    }

    @Nullable
    private static File coreJar() {
        try {
            Method getFile = JavaPlugin.class.getDeclaredMethod("getFile");
            getFile.setAccessible(true);
            Object file = getFile.invoke(Slimefun.instance());
            return file instanceof File ? (File) file : null;
        } catch (ReflectiveOperationException e) {
            return null;
        }
    }

    @Nonnull
    private static byte[] readAll(@Nonnull ZipInputStream zin) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[8192];
        int read;

        while ((read = zin.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }

        return out.toByteArray();
    }
}
