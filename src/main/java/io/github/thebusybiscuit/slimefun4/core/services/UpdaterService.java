package io.github.thebusybiscuit.slimefun4.core.services;

import io.github.thebusybiscuit.cscorelib2.updater.GitHubBuildsUpdater;
import io.github.thebusybiscuit.cscorelib2.updater.Updater;
import io.github.thebusybiscuit.slimefun4.api.SlimefunBranch;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.logging.Level;

/**
 * This Class represents our {@link Updater} Service.
 * If enabled, it will automatically connect to https://thebusybiscuit.github.io/builds/
 * to check for updates and to download them automatically.
 *
 * @author TheBusyBiscuit
 */
public class UpdaterService {

    private final Plugin plugin;
    private final Updater updater;
    private final SlimefunBranch branch;

    public UpdaterService(Plugin plugin, File file) {
        this.plugin = plugin;
        String version = plugin.getDescription().getVersion();

        if (version.startsWith("DEV - ")) {
            // If we are using a development build, we want to switch to our custom
            updater = new GitHubBuildsUpdater(plugin, file, "TheBusyBiscuit/Slimefun4/master");
            branch = SlimefunBranch.DEVELOPMENT;
        } else if (version.startsWith("RC - ")) {
            // If we are using a "stable" build, we want to switch to our custom
            updater = new GitHubBuildsUpdater(plugin, file, "TheBusyBiscuit/Slimefun4/stable", "RC - ");
            branch = SlimefunBranch.STABLE;
        } else {
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
    }

    /**
     * This method is called when the {@link UpdaterService} was disabled.
     */
    public void disable() {
        drawBorder();
        plugin.getLogger().log(Level.WARNING, "Slimefun 的自动更新已关闭.");
        plugin.getLogger().log(Level.WARNING, "我们尊重你的选择.");
        drawBorder();
    }

    private void drawBorder() {
        plugin.getLogger().log(Level.WARNING, "#######################################################");
    }

}
