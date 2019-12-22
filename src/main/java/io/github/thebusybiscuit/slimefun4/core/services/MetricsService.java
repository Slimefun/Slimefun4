package io.github.thebusybiscuit.slimefun4.core.services;

import org.bstats.bukkit.Metrics;

import me.mrCookieSlime.Slimefun.SlimefunPlugin;

public class MetricsService extends Metrics {

	public MetricsService(SlimefunPlugin plugin) {
		super(plugin);
		
		addCustomChart(new SimplePie("auto_updates", () -> 
			SlimefunPlugin.getCfg().getBoolean("options.auto-update") ? "enabled": "disabled"
		));
		
		addCustomChart(new SimplePie("resourcepack", () -> {
			String version = SlimefunPlugin.getItemTextureService().getVersion();
			
			if (version != null) {
				return version + " (Official)";
			}
			else if (SlimefunPlugin.getItemTextureService().isActive()) {
				return "Custom / Modified";
			}
			else {
				return "None";
			}
		}));
		
		addCustomChart(new SimplePie("branch", () -> {
			if (plugin.getDescription().getVersion().startsWith("DEV - ")) {
				return "master";
			}
			else if (plugin.getDescription().getVersion().startsWith("RC - ")) {
				return "stable";
			}
			else {
				return "Unknown";
			}
		}));
	}

}
