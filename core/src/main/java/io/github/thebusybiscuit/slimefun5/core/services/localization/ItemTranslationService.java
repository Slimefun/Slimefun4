package io.github.thebusybiscuit.slimefun5.core.services.localization;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import io.github.thebusybiscuit.slimefun5.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;

/**
 * Translates Slimefun item names and lore per language. Translations live in
 * {@code languages/<lang>/items.yml} (keyed by item id -> name + lore), shipped by core and by
 * addons. The current hardcoded names remain the built-in English fallback, so a missing translation
 * simply shows English. Used by the Guide and other Slimefun UIs to display items in the viewing
 * player's language; the coverage map powers the per-plugin translation percentage in the UI.
 */
public class ItemTranslationService {

    /** A single item's translated name and lore. Either field may be empty/null (partial entry). */
    public static final class ItemTranslation {

        private final String name;
        private final List<String> lore;

        ItemTranslation(@Nullable String name, @Nonnull List<String> lore) {
            this.name = name;
            this.lore = lore;
        }
    }

    private final Map<String, Map<String, ItemTranslation>> byLanguage = new HashMap<>();

    /** Loads the bundled core translations for every supported language. */
    public void loadBundled() {
        for (Language language : Slimefun.getLocalization().getLanguages()) {
            InputStream stream = Slimefun.class.getResourceAsStream("/languages/" + language.getId() + "/items.yml");

            if (stream != null) {
                load(language.getId(), stream);
            }
        }
    }

    /** Lets an addon contribute its own {@code languages/<lang>/items.yml} translations. */
    public void registerTranslations(@Nonnull JavaPlugin addon) {
        for (Language language : Slimefun.getLocalization().getLanguages()) {
            InputStream stream = addon.getResource("languages/" + language.getId() + "/items.yml");

            if (stream != null) {
                load(language.getId(), stream);
            }
        }
    }

    private void load(@Nonnull String language, @Nonnull InputStream stream) {
        try {
            YamlConfiguration config = YamlConfiguration.loadConfiguration(new InputStreamReader(stream, StandardCharsets.UTF_8));
            Map<String, ItemTranslation> map = byLanguage.computeIfAbsent(language, k -> new HashMap<>());

            for (String id : config.getKeys(false)) {
                String name = config.getString(id + ".name");
                List<String> lore = config.getStringList(id + ".lore");

                if (name != null || !lore.isEmpty()) {
                    map.put(id, new ItemTranslation(name, lore));
                }
            }
        } catch (RuntimeException e) {
            Slimefun.logger().log(Level.WARNING, "Failed to load item translations for {0}: {1}", new Object[] { language, e.getMessage() });
        }
    }

    @Nullable
    private ItemTranslation lookup(@Nullable String language, @Nonnull String itemId) {
        if (language == null) {
            return null;
        }

        Map<String, ItemTranslation> map = byLanguage.get(language);
        return map != null ? map.get(itemId) : null;
    }

    /**
     * Returns a display copy of the item with its name and lore translated into the player's language.
     * Falls back to the item's built-in (English) name/lore where no translation exists.
     */
    @Nonnull
    public ItemStack getDisplayItem(@Nonnull Player p, @Nonnull SlimefunItem item) {
        ItemStack display = item.getItem().clone();
        ItemTranslation translation = lookup(languageOf(p), item.getId());

        if (translation == null) {
            return display;
        }

        ItemMeta meta = display.getItemMeta();

        if (meta != null) {
            if (translation.name != null) {
                meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', translation.name));
            }

            if (!translation.lore.isEmpty()) {
                List<String> lore = new ArrayList<>();

                for (String line : translation.lore) {
                    lore.add(ChatColor.translateAlternateColorCodes('&', line));
                }

                meta.setLore(lore);
            }

            display.setItemMeta(meta);
        }

        return display;
    }

    @Nullable
    private String languageOf(@Nonnull Player p) {
        Language language = Slimefun.getLocalization().getLanguage(p);
        return language != null ? language.getId() : null;
    }

    /**
     * Coverage of a language per plugin: pluginName -> [translatedItems, totalItems], over all enabled
     * items grouped by their addon. Powers the translation-percentage UI.
     */
    @Nonnull
    public Map<String, int[]> getCoverage(@Nonnull String language) {
        Map<String, ItemTranslation> translated = byLanguage.getOrDefault(language, new HashMap<>());
        Map<String, int[]> coverage = new LinkedHashMap<>();

        for (SlimefunItem item : Slimefun.getRegistry().getEnabledSlimefunItems()) {
            try {
                String plugin = item.getAddon().getName();
                int[] counts = coverage.computeIfAbsent(plugin, k -> new int[2]);
                counts[1]++;

                if (translated.containsKey(item.getId())) {
                    counts[0]++;
                }
            } catch (Exception | LinkageError ignored) {
                // A broken item should not break the coverage report.
            }
        }

        return coverage;
    }
}
