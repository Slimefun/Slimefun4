package io.github.thebusybiscuit.slimefun5.core.guide.installer;

import java.io.File;

import javax.annotation.Nonnull;

import org.bukkit.Bukkit;

import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;

/**
 * Resolves where jars and working files go.
 *
 * New installs land directly in /plugins; updates (and any self-update of an already-loaded
 * plugin, including Slimefun core) land in the server's update folder, which Bukkit swaps in on
 * the next start — sidestepping the live-jar file lock. The update folder name is configurable in
 * bukkit.yml, so it is always resolved through the API, never hardcoded.
 */
public final class InstallTargets {

    private InstallTargets() {}

    /** The /plugins directory (parent of the Slimefun data folder). */
    @Nonnull
    public static File pluginsDir() {
        return Slimefun.instance().getDataFolder().getParentFile();
    }

    /**
     * The directory a staged jar should be written to.
     *
     * @param alreadyLoaded
     *            whether a plugin with this entry's name is currently loaded — if so we update via
     *            the update folder; otherwise it is a fresh install into /plugins.
     */
    @Nonnull
    public static File targetDir(boolean alreadyLoaded) {
        if (alreadyLoaded) {
            File updateDir = Bukkit.getServer().getUpdateFolderFile();

            if (!updateDir.exists()) {
                updateDir.mkdirs();
            }

            return updateDir;
        }

        return pluginsDir();
    }

    /** Reusable source-cache root: plugins/Slimefun/addon-sources. */
    @Nonnull
    public static File sourcesDir() {
        File dir = new File(Slimefun.instance().getDataFolder(), "addon-sources");

        if (!dir.exists()) {
            dir.mkdirs();
        }

        return dir;
    }

    /** Build-log directory: plugins/Slimefun/installer-logs. */
    @Nonnull
    public static File logsDir() {
        File dir = new File(Slimefun.instance().getDataFolder(), "installer-logs");

        if (!dir.exists()) {
            dir.mkdirs();
        }

        return dir;
    }
}
