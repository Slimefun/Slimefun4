package io.github.thebusybiscuit.slimefun4.core.services;

import java.io.File;
import java.util.logging.Level;

import org.bukkit.plugin.Plugin;

import io.github.thebusybiscuit.cscorelib2.updater.BukkitUpdater;
import io.github.thebusybiscuit.cscorelib2.updater.GitHubBuildsUpdater;
import io.github.thebusybiscuit.cscorelib2.updater.Updater;

public class UpdaterService {

	private final Updater updater;

	public UpdaterService(Plugin plugin, File file) {
		String version = plugin.getDescription().getVersion();

		if (version.equals("git")) {
			// This Server is using a modified build that is not a public release.
			plugin.getLogger().log(Level.INFO, "你正在使用 Namelesssss 汉化的 Slimefun, 自动更新已关闭!");
			updater = null;
		}
		else if (version.startsWith("DEV - ")) {
			// If we are using a development build, we want to switch to our custom 
			updater = new GitHubBuildsUpdater(plugin, file, "TheBusyBiscuit/Slimefun4/master");
		}
		else if (version.startsWith("RC - ")) {
			// If we are using a "stable" build, we want to switch to our custom 
			updater = new GitHubBuildsUpdater(plugin, file, "TheBusyBiscuit/Slimefun4/stable", "RC - ");
		}
		else {
			// We are using an official build from Bukkit, use the BukkitDev Updater
			updater = new BukkitUpdater(plugin, file, 53485);
		}
	}

	public void start() {
		if (updater != null) {
			updater.start();
		}
	}

}
