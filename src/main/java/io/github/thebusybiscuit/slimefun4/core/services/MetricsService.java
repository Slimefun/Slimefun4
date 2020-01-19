package io.github.thebusybiscuit.slimefun4.core.services;

import java.util.HashMap;
import java.util.Map;

import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import io.github.thebusybiscuit.slimefun4.core.services.localization.Language;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;

public class MetricsService extends Metrics {

	public MetricsService(SlimefunPlugin plugin) {
		super(plugin);

		addCustomChart(new SimplePie("auto_updates", () -> 
			SlimefunPlugin.getCfg().getBoolean("options.auto-update") ? "enabled": "disabled"
		));

		addCustomChart(new SimplePie("resourcepack", () -> {
			String version = SlimefunPlugin.getItemTextureService().getVersion();

			if (version != null && version.startsWith("v")) {
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

		addCustomChart(new SimplePie("language", () -> {
			Language language = SlimefunPlugin.getLocal().getDefaultLanguage();
			return language.getID();
		}));

		addCustomChart(new AdvancedPie("player_languages", () -> {
			Map<String, Integer> languages = new HashMap<>();

			for (Player p : Bukkit.getOnlinePlayers()) {
				Language lang = SlimefunPlugin.getLocal().getLanguage(p);
				languages.merge(lang.getID(), 1, Integer::sum);
			}

			return languages;
		}));
	}

}
