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
import org.bukkit.Material;
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

    // Pre-bake (English) copies of items whose physical template was re-skinned to the server default.
    // Lets the Guide still show English to a player whose language has no translation.
    private final Map<String, ItemStack> englishBaseline = new HashMap<>();

    /** Loads the bundled core translations for every supported language. */
    public void loadBundled() {
        for (Language language : Slimefun.getLocalization().getLanguages()) {
            InputStream stream = Slimefun.class.getResourceAsStream("/languages/" + language.getId() + "/items.yml");

            if (stream != null) {
                load(language.getId(), stream);
            }
        }
    }

    /**
     * Lets an addon contribute its own {@code languages/<lang>/items.yml} translations. Call this from
     * the addon's {@code onEnable} after its items are registered. The addon's items are then also
     * baked to the server default language (where a translation exists).
     */
    public void registerTranslations(@Nonnull JavaPlugin addon) {
        for (Language language : Slimefun.getLocalization().getLanguages()) {
            InputStream stream = addon.getResource("languages/" + language.getId() + "/items.yml");

            if (stream != null) {
                load(language.getId(), stream);
            }
        }

        applyServerDefaults();
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

    /**
     * Bakes the server's default-language translation into every enabled item's physical template, so
     * world/inventory items render in the server language. Items without a translation are left as-is
     * (so an English server, which has no language file, is completely unaffected). Call once after
     * {@link #loadBundled()} and after all items have registered.
     */
    public void applyServerDefaults() {
        Language defaultLanguage = Slimefun.getLocalization().getDefaultLanguage();

        if (defaultLanguage == null) {
            return;
        }

        Map<String, ItemTranslation> map = byLanguage.get(defaultLanguage.getId());

        if (map == null || map.isEmpty()) {
            return;
        }

        for (SlimefunItem item : Slimefun.getRegistry().getEnabledSlimefunItems()) {
            try {
                // Skip items already baked, so this stays idempotent (addons may trigger it again) and
                // never captures an already-translated template as the English baseline.
                if (englishBaseline.containsKey(item.getId())) {
                    continue;
                }

                ItemTranslation translation = map.get(item.getId());

                if (translation != null) {
                    englishBaseline.put(item.getId(), item.getItem());
                    item.bakeTranslatedDisplay(translation.name, translation.lore);
                }
            } catch (Exception | LinkageError ignored) {
                // A single broken item must not abort the whole baking pass.
            }
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
        ItemStack display = item.getItem();
        ItemTranslation translation = lookup(languageOf(p), item.getId());

        if (translation == null) {
            // No translation for the player's language: show the English baseline if the physical
            // template was baked to the server default, otherwise the (English) template as-is.
            ItemStack baseline = englishBaseline.get(item.getId());
            return baseline != null ? baseline.clone() : display;
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

    /**
     * Per-holder translation: rewrites a real {@link ItemStack}'s name (and static lore) in place into
     * the holding player's language, identifying the item by its Slimefun id so it can be re-translated
     * from any language. Preserves per-instance data (amount, durability, enchants, PDC) by editing meta
     * rather than replacing the stack. Lore is only swapped when the item carries no dynamic lore
     * (its line count still matches the canonical template), so charge/soulbound/backpack lore is left
     * untouched. Returns whether the stack was changed.
     */
    public boolean applyHolderTranslation(@Nonnull Player p, @Nullable ItemStack stack) {
        if (stack == null || stack.getType() == Material.AIR) {
            return false;
        }

        SlimefunItem item;

        try {
            item = SlimefunItem.getByItem(stack);
        } catch (Exception | LinkageError e) {
            return false;
        }

        if (item == null) {
            return false; // Vanilla item - nothing to translate.
        }

        // Canonical English copy: the pre-bake baseline if this item was baked, else its template.
        ItemStack english = englishBaseline.containsKey(item.getId()) ? englishBaseline.get(item.getId()) : item.getItem();
        ItemMeta englishMeta = english.getItemMeta();
        ItemMeta meta = stack.getItemMeta();

        if (meta == null || englishMeta == null) {
            return false;
        }

        ItemTranslation translation = lookup(languageOf(p), item.getId());

        String targetName = (translation != null && translation.name != null)
            ? ChatColor.translateAlternateColorCodes('&', translation.name)
            : englishMeta.getDisplayName();

        boolean changed = false;

        if (targetName != null && !targetName.equals(meta.getDisplayName())) {
            meta.setDisplayName(targetName);
            changed = true;
        }

        // Only translate lore for an unmodified item (no dynamic lines added), to avoid clobbering
        // charge/soulbound/backpack lore.
        List<String> englishLore = englishMeta.getLore();
        List<String> currentLore = meta.getLore();
        int englishCount = englishLore != null ? englishLore.size() : 0;
        int currentCount = currentLore != null ? currentLore.size() : 0;

        if (englishCount == currentCount) {
            List<String> targetLore = englishLore;

            if (translation != null && !translation.lore.isEmpty()) {
                targetLore = new ArrayList<>();

                for (String line : translation.lore) {
                    targetLore.add(ChatColor.translateAlternateColorCodes('&', line));
                }
            }

            if (targetLore != null && !targetLore.equals(currentLore)) {
                meta.setLore(targetLore);
                changed = true;
            }
        }

        if (changed) {
            stack.setItemMeta(meta);
        }

        return changed;
    }

    /**
     * Returns the item's display name in the player's language (without colour-stripping), falling back
     * to the English baseline and then the item's built-in name. Useful where only the name string is
     * needed (e.g. locked/not-researched guide entries) rather than a full display ItemStack.
     */
    @Nonnull
    public String getName(@Nonnull Player p, @Nonnull SlimefunItem item) {
        ItemTranslation translation = lookup(languageOf(p), item.getId());

        if (translation != null && translation.name != null) {
            return ChatColor.translateAlternateColorCodes('&', translation.name);
        }

        ItemStack baseline = englishBaseline.get(item.getId());

        if (baseline != null) {
            ItemMeta meta = baseline.getItemMeta();

            if (meta != null && meta.hasDisplayName()) {
                return meta.getDisplayName();
            }
        }

        return item.getItemName();
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
