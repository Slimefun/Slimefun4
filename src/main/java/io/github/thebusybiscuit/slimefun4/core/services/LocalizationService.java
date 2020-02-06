package io.github.thebusybiscuit.slimefun4.core.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.logging.Level;

import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import io.github.thebusybiscuit.cscorelib2.data.PersistentDataAPI;
import io.github.thebusybiscuit.cscorelib2.math.DoubleHandler;
import io.github.thebusybiscuit.slimefun4.core.services.localization.Language;
import io.github.thebusybiscuit.slimefun4.core.services.localization.SlimefunLocalization;
import io.github.thebusybiscuit.slimefun4.core.services.localization.SupportedLanguage;
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
        defaultLanguage = new Language(serverDefaultLanguage, "11b3188fd44902f72602bd7c2141f5a70673a411adb3d81862c69e536166b");

        defaultLanguage.setMessages(getConfig().getConfiguration());

        for (SupportedLanguage lang : SupportedLanguage.values()) {
            addLanguage(lang.getId(), lang.getTexture());
        }

        String language = getConfig().getString(LANGUAGE_PATH);
        if (language == null) language = serverDefaultLanguage;

        if (hasLanguage(serverDefaultLanguage)) {
            setLanguage(serverDefaultLanguage, !serverDefaultLanguage.equals(language));
        }
        else {
            setLanguage("en", false);
            plugin.getLogger().log(Level.WARNING, "Could not recognize the given language: \"{0}\"", serverDefaultLanguage);
        }

        Slimefun.getLogger().log(Level.INFO, "Available languages: {0}", String.join(", ", languages.keySet()));

        setPrefix("&aSlimefun 4 &7> ");
        save();
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
        // Checks if our jar files contains any .yml file for this id
        return containsResource("messages_" + language) || containsResource("researches_" + language);
    }

    private boolean containsResource(String file) {
        return plugin.getClass().getResource("/languages/" + file + ".yml") != null;
    }

    public boolean isLanguageLoaded(String id) {
        return languages.containsKey(id);
    }

    @Override
    public Language getDefaultLanguage() {
        return defaultLanguage;
    }

    @Override
    public Language getLanguage(Player p) {
        Optional<String> language = PersistentDataAPI.getOptionalString(p, languageKey);

        if (language.isPresent()) {
            Language lang = languages.get(language.get());

            if (lang != null) {
                return lang;
            }
        }

        return getDefaultLanguage();
    }

    private void setLanguage(String language, boolean reset) {
        // Clearing out the old Language (if necessary)
        if (reset) {
            getConfig().clear();
        }

        Slimefun.getLogger().log(Level.INFO, "Loaded language \"{0}\"", language);
        getConfig().setValue(LANGUAGE_PATH, language);

        // Loading in the defaults from our resources folder
        String path = "/languages/messages_" + language + ".yml";

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(plugin.getClass().getResourceAsStream(path), StandardCharsets.UTF_8))) {
            FileConfiguration config = YamlConfiguration.loadConfiguration(reader);
            getConfig().getConfiguration().setDefaults(config);
        } catch (IOException e) {
            Slimefun.getLogger().log(Level.SEVERE, "Failed to load language file: \"" + path + "\"", e);
        }

        save();
    }

    private void addLanguage(String id, String hash) {
        if (hasLanguage(id)) {
            FileConfiguration messages = streamConfigFile("messages_" + id + ".yml", getConfig().getConfiguration());
            FileConfiguration researches = streamConfigFile("researches_" + id + ".yml", null);

            Language language = new Language(id, hash);
            language.setMessages(messages);
            language.setResearches(researches);

            languages.put(id, language);
        }
    }

    public double getProgress(Language lang, Function<Language, FileConfiguration> method) {
        double defaultKeys = getTotalKeys(method.apply(languages.get("en")));
        if (defaultKeys == 0) return 0;
        return DoubleHandler.fixDouble(100.0 * (getTotalKeys(method.apply(lang)) / defaultKeys));
    }

    private double getTotalKeys(FileConfiguration cfg) {
        return cfg != null ? cfg.getKeys(true).size(): 0;
    }

    private FileConfiguration streamConfigFile(String file, FileConfiguration defaults) {
        String path = "/languages/" + file;

        if (plugin.getClass().getResourceAsStream(path) == null) {
            return new YamlConfiguration();
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(plugin.getClass().getResourceAsStream(path), StandardCharsets.UTF_8))) {
            FileConfiguration config = YamlConfiguration.loadConfiguration(reader);

            if (defaults != null) {
                config.setDefaults(defaults);
            }

            return config;
        } catch (IOException e) {
            Slimefun.getLogger().log(Level.SEVERE, "Failed to load language file into memory: \"" + path + "\"", e);
            return null;
        }
    }
}