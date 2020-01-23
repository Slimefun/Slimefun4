package io.github.thebusybiscuit.slimefun4.core.services;

import java.io.File;
import java.util.logging.Level;

import com.sun.tools.javac.resources.version;
import org.bukkit.plugin.Plugin;

import io.github.thebusybiscuit.cscorelib2.updater.BukkitUpdater;
import io.github.thebusybiscuit.cscorelib2.updater.GitHubBuildsUpdater;
import io.github.thebusybiscuit.cscorelib2.updater.Updater;

public class UpdaterService {

	private final Updater updater;

	public UpdaterService(Plugin plugin, File file) {
		String version = plugin.getDescription().getVersion();
		if (version.startsWith("DEV - ")) {
			// If we are using a development build, we want to switch to our custom 
			updater = new GitHubBuildsUpdater(plugin, file, "TheBusyBiscuit/Slimefun4/master");
		}
		else if (version.startsWith("RC - ")) {
			// If we are using a "stable" build, we want to switch to our custom 
			updater = new GitHubBuildsUpdater(plugin, file, "TheBusyBiscuit/Slimefun4/stable", "RC - ");
		}
		else {
			updater = null;
		}
	}

	public void start() {
		if (updater != null) {
			updater.start();
		}
	}

}
