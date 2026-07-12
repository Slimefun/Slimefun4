package io.github.thebusybiscuit.slimefun5.core.services.localization;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;

import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;

/**
 * Translates the decorative info items inside a block's {@link BlockMenu} (titles, descriptions and
 * background labels) per language. These strings are hardcoded English literals in each machine's
 * preset; this service keeps a per-language override keyed by preset id (= item id) and slot, loaded
 * from {@code languages/<lang>/menus.yml} shipped by core and addons.
 *
 * Block menu inventories are shared per placed block, so the translation is applied for the viewing
 * player when they open the menu. Only the preset's decorative slots are rewritten - input/output
 * slots (the only slots persisted to disk) are never touched, so stored inventories are unaffected.
 */
public class MenuTranslationService {

    /** A single decorative slot's translated name and lore. Either field may be null/empty. */
    private static final class MenuItemTranslation {

        private final String name;
        private final List<String> lore;

        MenuItemTranslation(@Nullable String name, @Nonnull List<String> lore) {
            this.name = name;
            this.lore = lore;
        }
    }

    // language -> presetId -> slot -> translation
    private final Map<String, Map<String, Map<Integer, MenuItemTranslation>>> byLanguage = new HashMap<>();

    /** Loads the bundled core menu translations for every supported language. */
    public void loadBundled() {
        for (Language language : Slimefun.getLocalization().getLanguages()) {
            InputStream stream = Slimefun.class.getResourceAsStream("/languages/" + language.getId() + "/menus.yml");

            if (stream != null) {
                load(language.getId(), stream);
            }
        }
    }

    /**
     * Lets an addon contribute its own {@code languages/<lang>/menus.yml} translations. Call from the
     * addon's {@code onEnable} after its items are registered.
     */
    public void registerTranslations(@Nonnull JavaPlugin addon) {
        for (Language language : Slimefun.getLocalization().getLanguages()) {
            InputStream stream = addon.getResource("languages/" + language.getId() + "/menus.yml");

            if (stream != null) {
                load(language.getId(), stream);
            }
        }
    }

    private void load(@Nonnull String language, @Nonnull InputStream stream) {
        try {
            YamlConfiguration config = YamlConfiguration.loadConfiguration(new InputStreamReader(stream, StandardCharsets.UTF_8));
            Map<String, Map<Integer, MenuItemTranslation>> presets = byLanguage.computeIfAbsent(language, k -> new HashMap<>());

            for (String presetId : config.getKeys(false)) {
                ConfigurationSection presetSection = config.getConfigurationSection(presetId);

                if (presetSection == null) {
                    continue;
                }

                Map<Integer, MenuItemTranslation> slots = presets.computeIfAbsent(presetId, k -> new HashMap<>());

                for (String slotKey : presetSection.getKeys(false)) {
                    Integer slot = parseSlot(slotKey);

                    if (slot == null) {
                        continue;
                    }

                    String name = presetSection.getString(slotKey + ".name");
                    List<String> lore = presetSection.getStringList(slotKey + ".lore");

                    if (name != null || !lore.isEmpty()) {
                        slots.put(slot, new MenuItemTranslation(name, lore));
                    }
                }
            }
        } catch (RuntimeException e) {
            Slimefun.logger().log(Level.WARNING, "Failed to load menu translations for {0}: {1}", new Object[] { language, e.getMessage() });
        }
    }

    @Nullable
    private static Integer parseSlot(@Nonnull String key) {
        try {
            return Integer.parseInt(key);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * Rewrites the decorative info items of an opened {@link BlockMenu} into the viewing player's
     * language. Falls back to the English ({@code en}) entry so a player whose language lacks an entry
     * (or an English player after a German player opened the same block) sees the correct language.
     * Slots with no translation in either the player's language or English are left untouched.
     */
    public void applyToMenu(@Nonnull BlockMenu menu, @Nonnull Player p) {
        String presetId = menu.getPreset().getID();
        Map<Integer, MenuItemTranslation> playerSlots = slotsFor(languageOf(p), presetId);
        Map<Integer, MenuItemTranslation> englishSlots = slotsFor("en", presetId);

        if (playerSlots == null && englishSlots == null) {
            return;
        }

        java.util.Set<Integer> targets = new java.util.HashSet<>();

        if (playerSlots != null) {
            targets.addAll(playerSlots.keySet());
        }

        if (englishSlots != null) {
            targets.addAll(englishSlots.keySet());
        }

        for (int slot : targets) {
            MenuItemTranslation translation = playerSlots != null ? playerSlots.get(slot) : null;

            if (translation == null && englishSlots != null) {
                translation = englishSlots.get(slot);
            }

            if (translation != null) {
                applyToSlot(menu, slot, translation);
            }
        }
    }

    private void applyToSlot(@Nonnull BlockMenu menu, int slot, @Nonnull MenuItemTranslation translation) {
        ItemStack current = menu.getItemInSlot(slot);

        if (current == null) {
            return;
        }

        ItemMeta meta = current.getItemMeta();

        if (meta == null) {
            return;
        }

        String targetName = translation.name != null
            ? ChatColor.translateAlternateColorCodes('&', translation.name)
            : meta.getDisplayName();

        List<String> targetLore = null;

        if (!translation.lore.isEmpty()) {
            targetLore = new ArrayList<>();

            for (String line : translation.lore) {
                targetLore.add(ChatColor.translateAlternateColorCodes('&', line));
            }
        }

        // Skip the rewrite when the slot is already in the right language, so re-opening is cheap and
        // does not needlessly dirty the menu.
        boolean nameSame = targetName == null || targetName.equals(meta.getDisplayName());
        boolean loreSame = targetLore == null || targetLore.equals(meta.getLore());

        if (nameSame && loreSame) {
            return;
        }

        ItemStack copy = current.clone();
        ItemMeta copyMeta = copy.getItemMeta();

        if (copyMeta == null) {
            return;
        }

        if (translation.name != null) {
            copyMeta.setDisplayName(targetName);
        }

        if (targetLore != null) {
            copyMeta.setLore(targetLore);
        }

        copy.setItemMeta(copyMeta);
        menu.replaceExistingItem(slot, copy, false);
    }

    /**
     * Development helper: writes the English baseline of every currently-registered block-menu preset
     * to the given file as {@code menus.yml}, capturing each decorative slot's name and lore. Pure
     * background panes (blank name, no lore) are skipped. This covers core and all loaded addons in one
     * pass, giving an exact preset-id/slot baseline to translate. Call once after all items are loaded.
     */
    public void dumpBaseline(@Nonnull java.io.File out) {
        YamlConfiguration config = new YamlConfiguration();

        for (Map.Entry<String, me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset> entry : Slimefun.getRegistry().getMenuPresets().entrySet()) {
            String presetId = entry.getKey();
            me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset preset = entry.getValue();

            for (int slot : preset.getPresetSlots()) {
                ItemStack item = preset.getItemInSlot(slot);

                if (item == null || !item.hasItemMeta()) {
                    continue;
                }

                ItemMeta meta = item.getItemMeta();
                String name = meta != null && meta.hasDisplayName() ? meta.getDisplayName() : null;
                List<String> lore = meta != null && meta.hasLore() ? meta.getLore() : new ArrayList<>();

                boolean blankName = name == null || ChatColor.stripColor(name).trim().isEmpty();

                if (blankName && lore.isEmpty()) {
                    continue;
                }

                String base = presetId + "." + slot;

                if (name != null) {
                    config.set(base + ".name", name.replace('§', '&'));
                }

                if (!lore.isEmpty()) {
                    List<String> converted = new ArrayList<>();

                    for (String line : lore) {
                        converted.add(line.replace('§', '&'));
                    }

                    config.set(base + ".lore", converted);
                }
            }
        }

        try {
            config.save(out);
            Slimefun.logger().log(Level.INFO, "Dumped menu baseline to {0}", out.getPath());
        } catch (java.io.IOException e) {
            Slimefun.logger().log(Level.WARNING, "Failed to dump menu baseline: {0}", e.getMessage());
        }
    }

    /**
     * Key coverage of a language: {@code {covered, total}} where total is the number of English
     * (presetId, slot) menu entries and covered is how many of those the given language also defines
     * (an entry only ever exists non-empty - see {@link #load}). English returns {@code {total, total}}.
     */
    @Nonnull
    public int[] getKeyCoverage(@Nonnull String languageId) {
        Map<String, Map<Integer, MenuItemTranslation>> english = byLanguage.getOrDefault("en", Collections.<String, Map<Integer, MenuItemTranslation>>emptyMap());
        boolean isEnglish = "en".equalsIgnoreCase(languageId);
        Map<String, Map<Integer, MenuItemTranslation>> lang = isEnglish ? english : byLanguage.getOrDefault(languageId, Collections.<String, Map<Integer, MenuItemTranslation>>emptyMap());

        int total = 0;
        int covered = 0;

        for (Map.Entry<String, Map<Integer, MenuItemTranslation>> presetEntry : english.entrySet()) {
            Map<Integer, MenuItemTranslation> langSlots = lang.get(presetEntry.getKey());

            for (Integer slot : presetEntry.getValue().keySet()) {
                total++;

                if (isEnglish || (langSlots != null && langSlots.containsKey(slot))) {
                    covered++;
                }
            }
        }

        return new int[] { covered, total };
    }

    @Nullable
    private Map<Integer, MenuItemTranslation> slotsFor(@Nullable String language, @Nonnull String presetId) {
        if (language == null) {
            return null;
        }

        Map<String, Map<Integer, MenuItemTranslation>> presets = byLanguage.get(language);
        return presets != null ? presets.get(presetId) : null;
    }

    @Nullable
    private String languageOf(@Nonnull Player p) {
        Language language = Slimefun.getLocalization().getLanguage(p);
        return language != null ? language.getId() : null;
    }
}
