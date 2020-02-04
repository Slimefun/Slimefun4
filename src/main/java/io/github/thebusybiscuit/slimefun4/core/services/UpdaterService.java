package io.github.thebusybiscuit.slimefun4.core.services;

import java.io.File;
import java.util.logging.Level;

import io.github.thebusybiscuit.slimefun4.api.SlimefunBranch;
import org.bukkit.plugin.Plugin;

import io.github.thebusybiscuit.cscorelib2.updater.GitHubBuildsUpdater;
import io.github.thebusybiscuit.cscorelib2.updater.Updater;

public class UpdaterService {

    private final SlimefunBranch branch;
	private final Updater updater;

	public UpdaterService(Plugin plugin, File file) {
		String version = plugin.getDescription().getVersion();

		if (version.equals("git") || version.equals("UNOFFICIAL")) {
			// This Server is using a modified build that is not a public release.
			plugin.getLogger().log(Level.INFO, "你正在使用 Namelesssss 汉化的 Slimefun, 自动更新已关闭!");
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
	}

}
