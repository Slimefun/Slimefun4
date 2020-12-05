package io.github.thebusybiscuit.slimefun4.core.services;

import java.io.File;
import java.util.logging.Level;

import javax.annotation.Nonnull;

import org.bukkit.plugin.Plugin;

import io.github.thebusybiscuit.cscorelib2.config.Config;
import io.github.thebusybiscuit.cscorelib2.updater.GitHubBuildsUpdater;
import io.github.thebusybiscuit.cscorelib2.updater.Updater;
import io.github.thebusybiscuit.slimefun4.api.SlimefunBranch;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.utils.PatternUtils;

/**
 * This Class represents our {@link Updater} Service.
 * If enabled, it will automatically connect to https://thebusybiscuit.github.io/builds/
 * to check for updates and to download them automatically.
 *
 * @author TheBusyBiscuit
 *
 */
public class UpdaterService {

    /**
     * Our {@link SlimefunPlugin} instance.
     */
    private final SlimefunPlugin plugin;

    /**
     * Our {@link Updater} implementation.
     */
    private final Updater updater;

    /**
     * The {@link SlimefunBranch} we are currently on.
     * If this is an official {@link SlimefunBranch}, auto updates will be enabled.
     */
    private final SlimefunBranch branch;

    /**
     * This will create a new {@link UpdaterService} for the given {@link SlimefunPlugin}.
     * The {@link File} should be the result of the getFile() operation of that {@link Plugin}.
     *
     * @param plugin
     *            The instance of Slimefun
     * @param version
     *            The current version of Slimefun
     * @param file
     *            The {@link File} of this {@link Plugin}
     */
    public UpdaterService(@Nonnull SlimefunPlugin plugin, @Nonnull String version, @Nonnull File file) {
        this.plugin = plugin;
        Updater autoUpdater = null;

        if (version.contains("UNOFFICIAL")) {
            // This Server is using a modified build that is not a public release.
            branch = SlimefunBranch.UNOFFICIAL;
        } else if (version.startsWith("DEV - ")) {
            // If we are using a development build, we want to switch to our custom
            try {
                autoUpdater = new GitHubBuildsUpdater(plugin, file, "TheBusyBiscuit/Slimefun4/master");

            } catch (Exception x) {
                plugin.getLogger().log(Level.SEVERE, "Failed to create AutoUpdater", x);
            }

            branch = SlimefunBranch.DEVELOPMENT;
        } else if (version.startsWith("RC - ")) {
            // If we are using a "stable" build, we want to switch to our custom
            try {
                autoUpdater = new GitHubBuildsUpdater(plugin, file, "TheBusyBiscuit/Slimefun4/stable", "RC - ");
            } catch (Exception x) {
                plugin.getLogger().log(Level.SEVERE, "Failed to create AutoUpdater", x);
            }

            branch = SlimefunBranch.STABLE;
        } else {
            branch = SlimefunBranch.UNKNOWN;
        }

        this.updater = autoUpdater;
    }

    /**
     * This method returns the branch the current build of Slimefun is running on.
     * This can be used to determine whether we are dealing with an official build
     * or a build that was unofficially modified.
     *
     * @return The branch this build of Slimefun is on.
     */
    @Nonnull
    public SlimefunBranch getBranch() {
        return branch;
    }

    /**
     * This method returns the build number that this is running on (or -1 if unofficial).
     * You should combine the usage with {@link #getBranch()} in order to properly see if this is
     * a development or stable build number.
     *
     * @return The build number of this Slimefun.
     */
    public int getBuildNumber() {
        if (updater != null && PatternUtils.NUMERIC.matcher(this.updater.getLocalVersion()).matches()) {
            return Integer.parseInt(updater.getLocalVersion());
        }

        return -1;
    }

    /**
     * This will start the {@link UpdaterService} and check for updates.
     * If it can find an update it will automatically be installed.
     */
    public void start() {
        if (updater != null) {
            updater.start();
        } else {
            printBorder();
            plugin.getLogger().log(Level.WARNING, "It looks like you are using an unofficially modified build of Slimefun!");
            plugin.getLogger().log(Level.WARNING, "Auto-Updates have been disabled, this build is not considered safe.");
            plugin.getLogger().log(Level.WARNING, "Do not report bugs encountered in this Version of Slimefun to any official sources.");
            printBorder();
        }
    }

    /**
     * This returns whether the {@link Updater} is enabled or not.
     * This includes the {@link Config} setting but also whether or not we are running an
     * official or unofficial build.
     *
     * @return Whether the {@link Updater} is enabled
     */
    public boolean isEnabled() {
        return SlimefunPlugin.getCfg().getBoolean("options.auto-update") && updater != null;
    }

    /**
     * This method is called when the {@link UpdaterService} was disabled.
     */
    public void disable() {
        printBorder();
        plugin.getLogger().log(Level.WARNING, "It looks like you have disabled auto-updates for Slimefun!");
        plugin.getLogger().log(Level.WARNING, "Auto-Updates keep your server safe, performant and bug-free.");
        plugin.getLogger().log(Level.WARNING, "We respect your decision.");

        if (branch != SlimefunBranch.STABLE) {
            plugin.getLogger().log(Level.WARNING, "If you are just scared of Slimefun breaking, then please consider using a \"stable\" build instead of disabling auto-updates.");
        }

        printBorder();
    }

    private void printBorder() {
        plugin.getLogger().log(Level.WARNING, "#######################################################");
    }

}
