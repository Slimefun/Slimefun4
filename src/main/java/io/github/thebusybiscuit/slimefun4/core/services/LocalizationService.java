package io.github.thebusybiscuit.slimefun4.core.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;

import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import io.github.thebusybiscuit.cscorelib2.data.PersistentDataAPI;
import io.github.thebusybiscuit.slimefun4.core.services.localization.Language;
import io.github.thebusybiscuit.slimefun4.core.services.localization.SlimefunLocalization;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.api.Slimefun;

public class LocalizationService extends SlimefunLocalization {

	private static final String LANGUAGE_PATH = "language";

	// All supported languages are stored in this LinkedHashMap, it is Linked so we keep the order
	private final Map<String, Language> languages = new LinkedHashMap<>();
	private final SlimefunPlugin plugin;
	private final NamespacedKey languageKey;
	private final Language defaultLanguage;

	public LocalizationService(SlimefunPlugin plugin, String serverDefaultLanguage) {
		super(plugin);

		this.plugin = plugin;
		languageKey = new NamespacedKey(plugin, LANGUAGE_PATH);
		defaultLanguage = new Language(serverDefaultLanguage, getConfig().getConfiguration(), "11b3188fd44902f72602bd7c2141f5a70673a411adb3d81862c69e536166b");
		loadLanguages();

		String language = getConfig().getString(LANGUAGE_PATH);
		if (language == null) language = serverDefaultLanguage;

		if (hasLanguage(serverDefaultLanguage)) {
			setLanguage(serverDefaultLanguage, !serverDefaultLanguage.equals(language));
		}
		else {
			setLanguage("en", false);
			plugin.getLogger().log(Level.WARNING, "Could not recognize the given language: \"{0}\"", serverDefaultLanguage);
		}

		setPrefix("&aSlimefun 4 &7> ");
		save();
	}

	// Load included Languages (with their ID and texture)
	private void loadLanguages() {
		addLanguage("en", "a1701f21835a898b20759fb30a583a38b994abf60d3912ab4ce9f2311e74f72");
	}

	@Override
	public NamespacedKey getKey() {
		return languageKey;
	}

	@Override
	public Language getLanguage(String id) {
		return languages.get(id);
	}

	@Override
	public Collection<Language> getLanguages() {
		return languages.values();
	}

	@Override
	public boolean hasLanguage(String language) {
		// Checks if our jar files contains a .yml file for this id
		return plugin.getClass().getResource("/languages/messages_" + language + ".yml") != null;
	}

	@Override
	public Language getDefaultLanguage() {
		return defaultLanguage;
	}

	@Override
	public Language getLanguage(Player p) {
		Optional<String> language = PersistentDataAPI.getOptionalString(p, languageKey);

		if (language.isPresent()) return languages.get(language.get());
		else return getDefaultLanguage();
	}

	private void setLanguage(String language, boolean reset) {
		// Clearing out the old Language (if necessary)
		if (reset) {
			for (String key : getConfig().getKeys()) {
				getConfig().setValue(key, null);
			}
		}

		Slimefun.getLogger().log(Level.INFO, "Loading language \"{0}\"", language);
		getConfig().setValue(LANGUAGE_PATH, language);

		// Loading in the defaults from our resources folder
		String path = "/languages/messages_" + language + ".yml";

		try (BufferedReader reader = new BufferedReader(new InputStreamReader(plugin.getClass().getResourceAsStream(path)))) {
			FileConfiguration config = YamlConfiguration.loadConfiguration(reader);
			getConfig().getConfiguration().setDefaults(config);
		} catch (IOException e) {
			Slimefun.getLogger().log(Level.SEVERE, "Failed to load language file: \"" + path + "\"", e);
		}

		save();
	}

	private void addLanguage(String id, String hash) {
		FileConfiguration cfg;

		if (!hasLanguage(id)) {
			cfg = getConfig().getConfiguration();
		}
		else {
			String path = "/languages/messages_" + id + ".yml";
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(plugin.getClass().getResourceAsStream(path)))) {
				cfg = YamlConfiguration.loadConfiguration(reader);
				cfg.setDefaults(getConfig().getConfiguration());
			} catch (IOException e) {
				Slimefun.getLogger().log(Level.SEVERE, "Failed to load language file into memory: \"" + path + "\"", e);
				cfg = getConfig().getConfiguration();
			}
		}

		languages.put(id, new Language(id, cfg, hash));
	}
}
