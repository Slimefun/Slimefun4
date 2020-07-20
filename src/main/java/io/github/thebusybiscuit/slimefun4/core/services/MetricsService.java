package io.github.thebusybiscuit.slimefun4.core.services;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;

import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.UnirestException;
import me.mrCookieSlime.Slimefun.api.Slimefun;
import org.bukkit.plugin.Plugin;

/**
 * This Class represents a Metrics Service that sends data to https://bstats.org/
 * This data is used to analyse the usage of this {@link Plugin}.
 * <p>
 * You can find more info in the README file of this Project on GitHub. <br>
 * <b>Note:</b> To start the metrics you will need to be calling {@link #start()}
 *
 * @author TheBusyBiscuit
 * @author WalshyDev
 */
public class MetricsService {

    private static final String REPO_NAME = "MetricsModule";
    private static final String GH_API = "https://api.github.com/repos/Slimefun/" + REPO_NAME;
    private static final String GH_REPO_RELEASES = "https://github.com/Slimefun/" + REPO_NAME
        + "/releases/download";

    private final SlimefunPlugin plugin;
    private final File metricFile;

    private URLClassLoader moduleClassLoader;
    private String metricVersion = null;

    static {
        Unirest.config()
            .concurrency(2, 1)
            .setDefaultHeader("User-Agent", "MetricsModule Auto-Updater")
            .setDefaultHeader("Accept", "application/vnd.github.v3+json")
            .enableCookieManagement(false)
            .cookieSpec("ignoreCookies");
    }

    public MetricsService(SlimefunPlugin plugin) {
        this.plugin = plugin;
        this.metricFile = new File(plugin.getDataFolder(), REPO_NAME + ".jar");
    }

    /**
     * This method loads the metric module and starts the metrics collection.
     */
    public void start() {
        boolean newlyDownloaded = false;
        if (!metricFile.exists()) {
            info(REPO_NAME + " does not exist, downloading...");
            download(getLatestVersion());
            newlyDownloaded = true;
        }

        try {
            // Load the jar file into a child class loader using the SF PluginClassLoader
            // as a parent.
            moduleClassLoader = URLClassLoader.newInstance(new URL[] { metricFile.toURI().toURL() },
                plugin.getClass().getClassLoader());
            Class<?> cl = moduleClassLoader.loadClass("dev.walshy.sfmetrics.MetricsModule");

            metricVersion = cl.getPackage().getImplementationVersion();

            // If it has not been newly downloaded, auto-updates are on AND there's a new version
            // then cleanup, download and start
            if (!newlyDownloaded
                && plugin.getConfig().getBoolean("metrics.auto-update")
                && checkForUpdate(metricVersion)
            ) {
                info("Cleaning up and re-loading Metrics.");
                cleanUp();
                start();
                return;
            }

            // Finally, we're good to start this.
            Method start = cl.getDeclaredMethod("start");
            String s = cl.getPackage().getImplementationVersion();

            // This is required to be sync due to bStats.
            Slimefun.runSync(() -> {
                try {
                    start.invoke(null);
                    info("Metrics v" + s + " started.");
                } catch (Exception e) {
                    plugin.getLogger().log(Level.WARNING, "Failed to start metrics.", e);
                }
            });
        } catch (Exception e) {
            plugin.getLogger().log(Level.WARNING,
                "Failed to load the metrics module. Maybe the jar is corrupt?", e);
        }
    }

    /**
     * This will close the child classloader and mark all the resources held under this no longer
     * in use, they will be cleaned up the next GC run.
     */
    public void cleanUp() {
        try {
            if (this.moduleClassLoader != null)
                this.moduleClassLoader.close();
        } catch (IOException e) {
            plugin.getLogger().log(Level.WARNING,
                "Could not clean up module class loader. Some memory may have been leaked.");
        }
    }

    /**
     * Checks for a new update and compares it against the current version.
     * If there is a new version available then this returns true.
     *
     * @param currentVersion The current version which is being used.
     * @return True if there is an update available.
     */
    public boolean checkForUpdate(String currentVersion) {
        if (currentVersion == null || currentVersion.equals("UNOFFICIAL")) return false;

        int latest = getLatestVersion();
        if (latest > Integer.parseInt(currentVersion)) {
            download(latest);
            return true;
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
        try {
            HttpResponse<JsonNode> response = Unirest.get(GH_API + "/releases/latest")
                .asJson();
            if (!response.isSuccess()) return -1;

            JsonNode node = response.getBody();

            if (node == null) return -1;

            return node.getObject().getInt("tag_name");
        } catch (UnirestException e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to fetch latest builds for SFMetrics");
            return -1;
        }
    }

    /**
     * Downloads the version specified to Slimefun's data folder.
     *
     * @param version The version to download.
     */
    private void download(int version) {
        try {
            if (metricFile.exists())
                metricFile.delete();

            info("# Starting download of MetricsModule build: #" + version);
            AtomicInteger lastPercentPosted = new AtomicInteger();
            HttpResponse<File> response = Unirest.get(GH_REPO_RELEASES + "/" + version
                + "/" + REPO_NAME + ".jar")
                .downloadMonitor((b, fileName, bytesWritten, totalBytes) -> {
                    int percent = (int) (20 * (Math.round((((double) bytesWritten / totalBytes) * 100) / 20)));

                    if (percent != 0 && percent != lastPercentPosted.get()) {
                        info("# Downloading... " + percent + "% " +
                            "(" + bytesWritten + "/" + totalBytes + " bytes)");
                        lastPercentPosted.set(percent);
                    }
                })
                .asFile(metricFile.getPath());
            if (response.isSuccess()) {
                info("Successfully downloaded " + REPO_NAME + " build: " + version);
                metricVersion = String.valueOf(version);
            } else
                // If it failed we don't want this to be like a file containing "404 not found"
                metricFile.delete();
        } catch (UnirestException e) {
            plugin.getLogger().log(Level.WARNING, "Failed to fetch the latest jar file from the" +
                " builds page. Perhaps GitHub is down.");
        }
    }

    private void info(String str) {
        plugin.getLogger().info(str);
    }

    /**
     * Returns the currently downloaded metric version. This CAN change! It may be null or an
     * older version before it has downloaded a newer one.
     *
     * @return The current version or null if not loaded.
     */
    @Nullable
    public String getVersion() {
        return metricVersion;
    }

    /**
     * Returns if the current server has metric auto-updates enabled.
     *
     * @return True if the current server has metric auto-updates enabled.
     */
    public boolean hasAutoUpdates() {
        return SlimefunPlugin.instance().getConfig().getBoolean("metrics.auto-update");
    }
}
