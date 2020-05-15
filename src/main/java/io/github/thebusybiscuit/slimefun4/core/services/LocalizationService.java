package io.github.thebusybiscuit.slimefun4.core.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;

import org.bukkit.NamespacedKey;
import org.bukkit.Server;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import io.github.thebusybiscuit.cscorelib2.math.DoubleHandler;
import io.github.thebusybiscuit.slimefun4.core.services.localization.Language;
import io.github.thebusybiscuit.slimefun4.core.services.localization.SlimefunLocalization;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.api.Slimefun;

/**
 * As the name suggests, this Service is responsible for Localization.
 * It is used for managing the {@link Language} of a {@link Player} and the entire {@link Server}.
 * 
 * @author TheBusyBiscuit
 * 
 * @see Language
 *
 */
public class LocalizationService extends SlimefunLocalization implements PersistentDataService {

    private static final String LANGUAGE_PATH = "language";

    // All supported languages are stored in this LinkedHashMap, it is Linked so we keep the order
    private final Map<String, Language> languages = new LinkedHashMap<>();
    private final boolean translationsEnabled;
    private final SlimefunPlugin plugin;
    private final String prefix;
    private final NamespacedKey languageKey;
    private final Language defaultLanguage;

    public LocalizationService(SlimefunPlugin plugin, String prefix, String serverDefaultLanguage) {
        super(plugin);

        this.plugin = plugin;
        this.prefix = prefix;
        languageKey = new NamespacedKey(plugin, LANGUAGE_PATH);

        if (serverDefaultLanguage != null) {
            translationsEnabled = SlimefunPlugin.getCfg().getBoolean("options.enable-translations");
            
            defaultLanguage = new Language(serverDefaultLanguage, "11b3188fd44902f72602bd7c2141f5a70673a411adb3d81862c69e536166b");
            defaultLanguage.setMessages(getConfig().getConfiguration());

            loadEmbeddedLanguages();

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
            save();
        }
        else {
            translationsEnabled = false;
            defaultLanguage = null;
        }
    }

    /**
     * This method returns whether translations are enabled on this {@link Server}.
     * 
     * @return Whether translations are enabled
     */
    public boolean isEnabled() {
        return translationsEnabled;
    }

    @Override
    public String getPrefix() {
        return prefix;
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
        // Checks if our jar files contains a messages.yml file for that language
        return containsResource("messages_" + language);
    }

    public boolean isLanguageLoaded(String id) {
        return languages.containsKey(id);
    }

    private boolean containsResource(String file) {
        return plugin.getClass().getResource("/languages/" + file + ".yml") != null;
    }

    @Override
    public Language getDefaultLanguage() {
        return defaultLanguage;
    }

    @Override
    public Language getLanguage(Player p) {
        Optional<String> language = getString(p, languageKey);

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

        defaultLanguage.setResearches(streamConfigFile("researches_" + language + ".yml", null));
        defaultLanguage.setResources(streamConfigFile("resources_" + language + ".yml", null));
        defaultLanguage.setCategories(streamConfigFile("categories_" + language + ".yml", null));
        defaultLanguage.setRecipeTypes(streamConfigFile("recipes_" + language + ".yml", null));

        Slimefun.getLogger().log(Level.INFO, "Loaded language \"{0}\"", language);
        getConfig().setValue(LANGUAGE_PATH, language);

        // Loading in the defaults from our resources folder
        String path = "/languages/messages_" + language + ".yml";

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(plugin.getClass().getResourceAsStream(path), StandardCharsets.UTF_8))) {
            FileConfiguration config = YamlConfiguration.loadConfiguration(reader);
            getConfig().getConfiguration().setDefaults(config);
        }
        catch (IOException e) {
            Slimefun.getLogger().log(Level.SEVERE, e, () -> "Failed to load language file: \"" + path + "\"");
        }

        save();
    }

    @Override
    protected void addLanguage(String id, String hash) {
        if (hasLanguage(id)) {
            FileConfiguration messages = streamConfigFile("messages_" + id + ".yml", getConfig().getConfiguration());
            FileConfiguration researches = streamConfigFile("researches_" + id + ".yml", null);
            FileConfiguration resources = streamConfigFile("resources_" + id + ".yml", null);
            FileConfiguration categories = streamConfigFile("categories_" + id + ".yml", null);
            FileConfiguration recipes = streamConfigFile("recipes_" + id + ".yml", null);

            Language language = new Language(id, hash);
            language.setMessages(messages);
            language.setResearches(researches);
            language.setResources(resources);
            language.setCategories(categories);
            language.setRecipeTypes(recipes);

            languages.put(id, language);
        }
    }

    /**
     * This returns the progress of translation for any given {@link Language}.
     * The progress is determined by the amount of translated strings divided by the amount
     * of strings in the english {@link Language} file and multiplied by 100.0
     * 
     * @param lang
     *            The {@link Language} to get the progress of
     * 
     * @return A percentage {@code (0.0 - 100.0)} for the progress of translation of that {@link Language}
     */
    public double getProgress(Language lang) {
        int defaultKeys = getTotalKeys(languages.get("en"));
        if (defaultKeys == 0) return 0;

        return Math.min(DoubleHandler.fixDouble(100.0 * (getTotalKeys(lang) / (double) defaultKeys)), 100.0);
    }

    private int getTotalKeys(Language lang) {
        return getKeys(lang.getMessages(), lang.getResearches(), lang.getResources(), lang.getCategories(), lang.getRecipeTypes());
    }

    private int getKeys(FileConfiguration... files) {
        int keys = 0;

        for (FileConfiguration cfg : files) {
            keys += cfg != null ? cfg.getKeys(true).size() : 0;
        }

        return keys;
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
        }
        catch (IOException e) {
            Slimefun.getLogger().log(Level.SEVERE, e, () -> "Failed to load language file into memory: \"" + path + "\"");
            return null;
        }
    }
}
