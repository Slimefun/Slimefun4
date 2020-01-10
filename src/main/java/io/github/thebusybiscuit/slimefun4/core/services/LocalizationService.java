package io.github.thebusybiscuit.slimefun4.core.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import io.github.thebusybiscuit.cscorelib2.config.Localization;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.api.Slimefun;

public class LocalizationService extends Localization {
	
	private static final String LANGUAGE_PATH = "language";
	
	private final SlimefunPlugin plugin;

	public LocalizationService(SlimefunPlugin plugin) {
		super(plugin);
		this.plugin = plugin;
		
		String selectedLanguage = SlimefunPlugin.getSelectedLanguage();
		String language = getLanguage();
		
		if (hasLanguage(selectedLanguage)) {
			setLanguage(selectedLanguage, !selectedLanguage.equals(language));
		}
		else {
			plugin.getLogger().log(Level.WARNING, "Could not recognize the given language: \"{0}\"", selectedLanguage);
		}
		
		setPrefix("&aSlimefun 4 &7> ");
		save();
	}
	
	private String getLanguage() {
		String language = getConfig().getString(LANGUAGE_PATH);
		return language == null ? "en": language;
	}
	
	private void setLanguage(String language, boolean reset) {
		
		if (reset) {
			for (String key : getConfig().getKeys()) {
				getConfig().setValue(key, null);
			}
		}
		
		Slimefun.getLogger().log(Level.INFO, "Loading language \"{0}\"", language);
		getConfig().setValue(LANGUAGE_PATH, language);
		
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(plugin.getClass().getResourceAsStream("/languages/messages_" + language + ".yml")))) {
			FileConfiguration config = YamlConfiguration.loadConfiguration(reader);
			getConfig().getConfiguration().setDefaults(config);
        } catch (IOException e) {
            Slimefun.getLogger().log(Level.SEVERE, "Failed to load language file: \"messages_" + language + ".yml\"", e);
        }
		
		save();
	}
	
	public boolean hasLanguage(String language) {
		return plugin.getClass().getResource("/languages/messages_" + language + ".yml") != null;
	}
}
