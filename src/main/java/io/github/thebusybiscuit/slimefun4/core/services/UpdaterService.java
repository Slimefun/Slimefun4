package io.github.thebusybiscuit.slimefun4.core.services;

import io.github.thebusybiscuit.cscorelib2.updater.GitHubBuildsUpdater;
import io.github.thebusybiscuit.cscorelib2.updater.Updater;
import io.github.thebusybiscuit.slimefun4.api.SlimefunBranch;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.logging.Level;

public class UpdaterService {
    private final Plugin plugin;
    private final Updater updater;
    private final SlimefunBranch branch;

    public UpdaterService(Plugin plugin, File file) {
        this.plugin = plugin;
        String version = plugin.getDescription().getVersion();

        if (version.contains("NIGHTLY") || version.contains("WEEKLY")) {
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

    public SlimefunBranch getBranch() {
        return branch;
    }

    public void start() {
        if (updater != null) {
            updater.start();
        }
        else {
            drawBorder();
            plugin.getLogger().log(Level.WARNING, "自动更新已被关闭.");
            plugin.getLogger().log(Level.WARNING, "请注意: 在官方渠道下使用此版本反馈问题不会被回应.");
            drawBorder();
        }
    }

    private void drawBorder() {
        plugin.getLogger().log(Level.WARNING, "#######################################################");
    }

}
