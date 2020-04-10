package io.github.thebusybiscuit.slimefun4.core.services;

import java.io.File;
import java.util.logging.Level;

import org.bukkit.plugin.Plugin;

import io.github.thebusybiscuit.cscorelib2.updater.GitHubBuildsUpdater;
import io.github.thebusybiscuit.cscorelib2.updater.Updater;
import io.github.thebusybiscuit.slimefun4.api.SlimefunBranch;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;

/**
 * This Class represents our {@link Updater} Service.
 * If enabled, it will automatically connect to https://thebusybiscuit.github.io/builds/
 * to check for updates and to download them automatically.
 * 
 * @author TheBusyBiscuit
 *
 */
public class UpdaterService {

    private final SlimefunPlugin plugin;
    private final Updater updater;
    private final SlimefunBranch branch;

    /**
     * This will create a new {@link UpdaterService} for the given {@link SlimefunPlugin}.
     * The {@link File} should be the result of the getFile() operation of that {@link Plugin}.
     * 
     * @param plugin
     *            The instance of Slimefun
     * @param file
     *            The {@link File} of this {@link Plugin}
     */
    public UpdaterService(SlimefunPlugin plugin, File file) {
        this.plugin = plugin;
        String version = plugin.getDescription().getVersion();

        if (version.contains("UNOFFICIAL")) {
            // This Server is using a modified build that is not a public release.
            updater = null;
            branch = SlimefunBranch.UNOFFICIAL;
        }
        else if (version.startsWith("DEV - ")) {
            // If we are using a development build, we want to switch to our custom
            updater = new GitHubBuildsUpdater(plugin, file, "TheBusyBiscuit/Slimefun4/master");
            branch = SlimefunBranch.DEVELOPMENT;
        }
        else if (version.startsWith("RC - ")) {
            // If we are using a "stable" build, we want to switch to our custom
            updater = new GitHubBuildsUpdater(plugin, file, "TheBusyBiscuit/Slimefun4/stable", "RC - ");
            branch = SlimefunBranch.STABLE;
        }
        else {
            updater = null;
            branch = SlimefunBranch.UNKNOWN;
        }
    }

    /**
     * This method returns the branch the current build of Slimefun is running on.
     * This can be used to determine whether we are dealing with an official build
     * or a build that was unofficially modified.
     * 
     * @return The branch this build of Slimefun is on.
     */
    public SlimefunBranch getBranch() {
        return branch;
    }

    /**
     * This will start the {@link UpdaterService} and check for updates.
     * If it can find an update it will automatically be installed.
     */
    public void start() {
        if (updater != null) {
            updater.start();
        }
        else {
            printBorder();
            plugin.getLogger().log(Level.WARNING, "It looks like you are using an unofficially modified build of Slimefun!");
            plugin.getLogger().log(Level.WARNING, "Auto-Updates have been disabled, this build is not considered safe.");
            plugin.getLogger().log(Level.WARNING, "Do not report bugs encountered in this Version of Slimefun to any official sources.");
            printBorder();
        }
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
