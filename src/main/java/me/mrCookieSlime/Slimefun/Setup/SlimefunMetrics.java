package me.mrCookieSlime.Slimefun.Setup;

import org.bstats.bukkit.Metrics;

import me.mrCookieSlime.Slimefun.SlimefunPlugin;

public class SlimefunMetrics extends Metrics {

	public SlimefunMetrics(SlimefunPlugin plugin) {
		super(plugin);
		
		addCustomChart(new SimplePie("auto_updates", () -> SlimefunPlugin.getCfg().getBoolean("options.auto-update") ? "enabled": "disabled"));
		
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
