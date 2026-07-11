package io.github.thebusybiscuit.slimefun5.core.services.localization;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
import io.github.thebusybiscuit.slimefun5.core.guide.options.ItemDescriptionsOption;
import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun5.utils.compatibility.PdcCompat;

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
        private final List<String> description;
        private final List<String> type;
        private final List<String> stats;
        private final List<String> usage;

        ItemTranslation(@Nullable String name, @Nonnull List<String> lore, @Nonnull List<String> description,
                        @Nonnull List<String> type, @Nonnull List<String> stats, @Nonnull List<String> usage) {
            this.name = name;
            this.lore = lore;
            this.description = description;
            this.type = type;
            this.stats = stats;
            this.usage = usage;
        }
    }

    private final Map<String, Map<String, ItemTranslation>> byLanguage = new HashMap<>();

    /**
     * A dynamic item family: an item id that matches {@link #pattern} (compiled from a key that used the
     * capture token {@code %MOB%}, e.g. {@code "%MOB%_SOUL_JAR"}) resolves to {@link #template} with the
     * {@code %mob%} placeholder replaced by the humanized captured segment. This lets an addon localize a
     * whole family of runtime-generated items (per entity type, etc.) from a single template entry.
     */
    private static final class Family {

        private final java.util.regex.Pattern pattern;
        private final ItemTranslation template;
        // Count of literal (non-capture) characters; families are tried most-specific-first so that e.g.
        // FILLED_%MOB%_SOUL_JAR wins over %MOB%_SOUL_JAR for "FILLED_ZOMBIE_SOUL_JAR".
        private final int specificity;

        Family(java.util.regex.Pattern pattern, ItemTranslation template, int specificity) {
            this.pattern = pattern;
            this.template = template;
            this.specificity = specificity;
        }
    }

    /** The id-capture token used in a family key ("%MOB%_SOUL_JAR") and the value placeholder ("%mob%"). */
    private static final String FAMILY_ID_TOKEN = "%MOB%";
    private static final String FAMILY_VALUE_PLACEHOLDER = "%mob%";

    private final Map<String, List<Family>> familiesByLanguage = new HashMap<>();
    // Memoizes family resolution per (language, id); a null value means "checked, no family matches".
    private final Map<String, ItemTranslation> familyResolveCache = new HashMap<>();

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

            // Derive item ids from the leaf ".name"/".lore" paths. Item ids may contain dots (e.g. a
            // SlimeTinker trait ending in "."), which YAML treats as path separators - getKeys(false)
            // would only see the section before the first dot, so those items never resolve.
            Set<String> ids = new HashSet<>();
            for (String path : config.getKeys(true)) {
                for (String leaf : new String[] { ".name", ".lore", ".description", ".type", ".stats", ".usage" }) {
                    if (path.endsWith(leaf)) {
                        ids.add(path.substring(0, path.length() - leaf.length()));
                    }
                }
            }

            for (String id : ids) {
                String name = config.getString(id + ".name");
                List<String> lore = config.getStringList(id + ".lore");
                List<String> description = config.getStringList(id + ".description");
                List<String> type = config.getStringList(id + ".type");
                List<String> stats = config.getStringList(id + ".stats");
                List<String> usage = config.getStringList(id + ".usage");

                if (name != null || !lore.isEmpty() || !description.isEmpty() || !type.isEmpty() || !stats.isEmpty() || !usage.isEmpty()) {
                    ItemTranslation translation = new ItemTranslation(name, lore, description, type, stats, usage);

                    if (id.contains(FAMILY_ID_TOKEN)) {
                        // A family template: turn "%MOB%_SOUL_JAR" into a regex "(.+)_SOUL_JAR" and store it
                        // so any concrete id (ZOMBIE_SOUL_JAR) resolves through it (see resolveFamily).
                        String regex = java.util.regex.Pattern.quote(id).replace(FAMILY_ID_TOKEN, "\\E(.+)\\Q");
                        int specificity = id.replace(FAMILY_ID_TOKEN, "").length();
                        List<Family> list = familiesByLanguage.computeIfAbsent(language, k -> new ArrayList<>());
                        list.add(new Family(java.util.regex.Pattern.compile("^" + regex + "$"), translation, specificity));
                        list.sort((a, b) -> Integer.compare(b.specificity, a.specificity));
                        familyResolveCache.clear();
                    } else {
                        map.put(id, translation);
                    }
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

                // Resolve through lookup() so item families (e.g. per-mob jars) bake too, not just exact ids.
                ItemTranslation translation = lookup(defaultLanguage.getId(), item.getId());

                if (translation != null) {
                    englishBaseline.put(item.getId(), item.getItem());

                    // Bake the fully COMPOSED block lore (Type/Description/Stats/Usage), not just the legacy
                    // flat `lore` list. This makes the physical template match the guide/per-holder display,
                    // AND lets id-only items (no hardcoded name/lore in code) get their entire display from
                    // en/items.yml. Legacy flat `lore` still serves as the fallback base for un-blocked items.
                    List<List<String>> blocks = resolveBlocks(defaultLanguage.getId(), item);

                    ItemMeta templateMeta = item.getItem().getItemMeta();
                    List<String> currentLore = (templateMeta != null && templateMeta.getLore() != null)
                        ? templateMeta.getLore() : new ArrayList<String>();
                    List<String> fallbackBase = !translation.lore.isEmpty() ? translation.lore : currentLore;

                    List<String> composed = LoreComposer.compose(
                        item, blocks.get(0), blocks.get(1), blocks.get(2), blocks.get(3), fallbackBase, true);

                    item.bakeTranslatedDisplay(translation.name, composed);
                }
            } catch (Exception | LinkageError ignored) {
                // A single broken item must not abort the whole baking pass.
            }
        }
    }

    // Package-private seams for headless tests of the item-family resolver.
    void loadTranslationsForTest(@Nonnull String language, @Nonnull InputStream stream) {
        load(language, stream);
    }

    @Nullable
    String resolveNameForTest(@Nonnull String language, @Nonnull String itemId) {
        ItemTranslation translation = lookup(language, itemId);
        return translation == null ? null : translation.name;
    }

    @Nullable
    private ItemTranslation lookup(@Nullable String language, @Nonnull String itemId) {
        if (language == null) {
            return null;
        }

        Map<String, ItemTranslation> map = byLanguage.get(language);

        if (map != null) {
            ItemTranslation exact = map.get(itemId);

            if (exact != null) {
                return exact;
            }
        }

        return resolveFamily(language, itemId);
    }

    /**
     * Resolves an id against the language's item families (see {@link Family}). On a match, the captured
     * segment is humanized ({@code ZOMBIE_PIGMAN -> "Zombie Pigman"}) and substituted for every
     * {@code %mob%} placeholder in the template. Memoized per (language, id), including negative results.
     */
    @Nullable
    private ItemTranslation resolveFamily(@Nonnull String language, @Nonnull String itemId) {
        List<Family> families = familiesByLanguage.get(language);

        if (families == null || families.isEmpty()) {
            return null;
        }

        String cacheKey = language + ' ' + itemId;

        if (familyResolveCache.containsKey(cacheKey)) {
            return familyResolveCache.get(cacheKey);
        }

        ItemTranslation resolved = null;

        for (Family family : families) {
            java.util.regex.Matcher matcher = family.pattern.matcher(itemId);

            if (matcher.matches()) {
                String mob = humanize(matcher.group(1));
                ItemTranslation t = family.template;
                resolved = new ItemTranslation(
                    substitute(t.name, mob),
                    substitute(t.lore, mob),
                    substitute(t.description, mob),
                    substitute(t.type, mob),
                    substitute(t.stats, mob),
                    substitute(t.usage, mob));
                break;
            }
        }

        familyResolveCache.put(cacheKey, resolved);
        return resolved;
    }

    /** Title-cases an enum-style name: {@code ZOMBIE_PIGMAN -> "Zombie Pigman"}. */
    @Nonnull
    private static String humanize(@Nonnull String raw) {
        String[] words = raw.toLowerCase(java.util.Locale.ROOT).split("_");
        StringBuilder sb = new StringBuilder(raw.length());

        for (String word : words) {
            if (word.isEmpty()) {
                continue;
            }

            if (sb.length() > 0) {
                sb.append(' ');
            }

            sb.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1));
        }

        return sb.toString();
    }

    @Nullable
    private static String substitute(@Nullable String value, @Nonnull String mob) {
        return value == null ? null : value.replace(FAMILY_VALUE_PLACEHOLDER, mob);
    }

    @Nonnull
    private static List<String> substitute(@Nonnull List<String> lines, @Nonnull String mob) {
        List<String> out = new ArrayList<>(lines.size());

        for (String line : lines) {
            out.add(line.replace(FAMILY_VALUE_PLACEHOLDER, mob));
        }

        return out;
    }

    private interface BlockSelector { List<String> select(ItemTranslation t); }

    private static final BlockSelector SEL_TYPE = new BlockSelector() { public List<String> select(ItemTranslation t) { return t.type; } };
    private static final BlockSelector SEL_DESCRIPTION = new BlockSelector() { public List<String> select(ItemTranslation t) { return t.description; } };
    private static final BlockSelector SEL_STATS = new BlockSelector() { public List<String> select(ItemTranslation t) { return t.stats; } };
    private static final BlockSelector SEL_USAGE = new BlockSelector() { public List<String> select(ItemTranslation t) { return t.usage; } };

    /** Resolve a block for a specific primary language: that language's block, else the server default's, else empty. */
    @Nonnull
    private List<String> blockForLanguage(@Nullable String language, @Nonnull SlimefunItem item, @Nonnull BlockSelector selector) {
        ItemTranslation primary = lookup(language, item.getId());
        if (primary != null) {
            List<String> block = selector.select(primary);
            if (!block.isEmpty()) {
                return block;
            }
        }
        Language defaultLanguage = Slimefun.getLocalization().getDefaultLanguage();
        if (defaultLanguage != null && !defaultLanguage.getId().equals(language)) {
            ItemTranslation def = lookup(defaultLanguage.getId(), item.getId());
            if (def != null) {
                return selector.select(def);
            }
        }
        return Collections.<String>emptyList();
    }

    /** [type, description, stats, usage] for a primary language, each with per-block default fallback. */
    @Nonnull
    private List<List<String>> resolveBlocks(@Nullable String language, @Nonnull SlimefunItem item) {
        List<List<String>> blocks = new ArrayList<>(4);
        blocks.add(blockForLanguage(language, item, SEL_TYPE));
        blocks.add(blockForLanguage(language, item, SEL_DESCRIPTION));
        blocks.add(blockForLanguage(language, item, SEL_STATS));
        blocks.add(blockForLanguage(language, item, SEL_USAGE));
        return blocks;
    }

    /**
     * Returns a display copy of the item with its name and lore translated into the player's language.
     * Falls back to the item's built-in (English) name/lore where no translation exists.
     */
    @Nonnull
    public ItemStack getDisplayItem(@Nonnull Player p, @Nonnull SlimefunItem item) {
        ItemTranslation translation = lookup(languageOf(p), item.getId());

        // Base display: the player's translated template if available, else the English baseline
        // (when the physical template was baked to the server default), else the item template.
        // A description-only entry (name null, lore empty) is not a usable name/lore translation, so fall
        // back to the English baseline template for the base display, then append the description below.
        boolean usableTranslation = translation != null && (translation.name != null || !translation.lore.isEmpty());

        ItemStack display;

        if (!usableTranslation) {
            ItemStack baseline = englishBaseline.get(item.getId());
            display = baseline != null ? baseline.clone() : item.getItem();
        } else {
            display = item.getItem();
        }

        ItemMeta meta = display.getItemMeta();

        if (meta != null) {
            if (translation != null && translation.name != null) {
                meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', translation.name));
            }

            List<String> fallbackBase = (translation != null && !translation.lore.isEmpty())
                ? translation.lore
                : (meta.getLore() != null ? meta.getLore() : new ArrayList<String>());

            List<List<String>> blocks = resolveBlocks(languageOf(p), item);

            List<String> composed = LoreComposer.compose(
                item,
                blocks.get(0),
                blocks.get(1),
                blocks.get(2),
                blocks.get(3),
                fallbackBase,
                true);

            if (!composed.isEmpty()) {
                meta.setLore(composed);
            }

            // Enchantments are re-rendered as lore under the Type block, so hide the vanilla tooltip.
            EnchantDisplay.hide(meta);

            display.setItemMeta(meta);
        }

        return display;
    }

    /**
     * Per-holder translation: rewrites a real {@link ItemStack}'s name (and static lore) in place into
     * the holding player's language, identifying the item by its Slimefun id so it can be re-translated
     * from any language. Preserves per-instance data (amount, durability, enchants, PDC) by editing meta
     * rather than replacing the stack. Lore is only rewritten when it is still a pristine template — i.e.
     * it matches the English baseline or one of the shipped language renderings (with or without the
     * appended description block), as determined by {@link #isPristineOrComposed}. Runtime-mutated lore
     * (charge/uses counters, backpack id, spawner type, tome owner) matches none of these and is left
     * untouched. Returns whether the stack was changed.
     */
    public boolean applyHolderTranslation(@Nonnull Player p, @Nullable ItemStack stack) {
        return applyTranslation(languageOf(p), ItemDescriptionsOption.isEnabledFor(p), stack);
    }

    /**
     * Re-skins a stack to the server's <em>default</em> language, for items that live in a block menu
     * rather than a player's inventory (a machine output slot has no single owner). Machine recipe
     * outputs are cloned from the recipe before the display is baked onto item templates, so they arrive
     * with no name/lore; this brings them to the same display the baked template shows, so they render
     * correctly in the machine's own slot. A player taking the item re-skins it to their own language on
     * pickup. Runtime-mutated lore is left untouched (see {@link #isPristineOrComposed}).
     *
     * @param stack
     *            The {@link ItemStack} to re-skin (mutated in place)
     *
     * @return Whether the stack was changed
     */
    public boolean applyServerDefaultTranslation(@Nullable ItemStack stack) {
        Language defaultLanguage = Slimefun.getLocalization().getDefaultLanguage();
        return applyTranslation(defaultLanguage != null ? defaultLanguage.getId() : null, true, stack);
    }

    private boolean applyTranslation(@Nullable String languageId, boolean includeDescription, @Nullable ItemStack stack) {
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

        ItemTranslation translation = lookup(languageId, item.getId());

        String targetName = (translation != null && translation.name != null)
            ? ChatColor.translateAlternateColorCodes('&', translation.name)
            : englishMeta.getDisplayName();

        boolean changed = false;

        if (targetName != null && !targetName.equals(meta.getDisplayName())) {
            meta.setDisplayName(targetName);
            changed = true;
        }

        // Game logic mutates the lore of some items in place (spawner "<Type>", backpack "<ID>", tome
        // owner, charge/uses counters). Those items must be left untouched, so we only rewrite lore
        // that is still recognized as a pristine template.
        List<String> englishLore = englishMeta.getLore();
        List<String> currentLore = meta.getLore();

        // A lore list is pristine when it matches the English baseline, or the base lore of any shipped
        // language rendering with or without its appended description block (see isPristineOrComposed).
        // Runtime-mutated lore matches none of these variants and is correctly skipped.
        if (isPristineOrComposed(item, currentLore, englishLore)) {
            List<String> fallbackBase = (translation != null && !translation.lore.isEmpty()) ? translation.lore
                : (englishLore != null ? englishLore : new ArrayList<String>());

            List<List<String>> blocks = resolveBlocks(languageId, item);

            List<String> targetLore = LoreComposer.compose(
                item,
                blocks.get(0),
                blocks.get(1),
                blocks.get(2),
                blocks.get(3),
                fallbackBase,
                includeDescription);

            List<String> currentForCompare = currentLore != null ? currentLore : Collections.<String>emptyList();
            if (!targetLore.equals(currentForCompare)) {
                meta.setLore(targetLore.isEmpty() ? null : targetLore);
                changed = true;
            }

            // The item's enchantments are re-rendered as lore under the Type block (only reached here for a
            // pristine template, so we aren't hiding a player's own anvil enchants); hide the vanilla tooltip.
            if (!english.getEnchantments().isEmpty()) {
                EnchantDisplay.hide(meta);
                changed = true;
            }
        }

        if (changed) {
            stack.setItemMeta(meta);
        }

        return changed;
    }

    /**
     * Whether {@code currentLore} is a pristine (not runtime-mutated) rendering for this item: null/empty,
     * the English baseline lore, or the LoreComposer output for ANY shipped language with the description
     * block either shown or hidden. Runtime-mutated lore (charge/uses counters, backpack id, spawner type,
     * tome owner) matches none of these and is left untouched.
     */
    private boolean isPristineOrComposed(@Nonnull SlimefunItem item, @Nullable List<String> currentLore, @Nullable List<String> englishLore) {
        if (currentLore == null || currentLore.isEmpty()) {
            return true;
        }

        if (currentLore.equals(englishLore)) {
            return true;
        }

        List<String> englishBase = englishLore != null ? englishLore : Collections.<String>emptyList();

        for (String language : byLanguage.keySet()) {
            List<List<String>> blocks = resolveBlocks(language, item);
            ItemTranslation t = lookup(language, item.getId());
            List<String> langBase = (t != null && !t.lore.isEmpty()) ? t.lore : englishBase;

            for (List<String> fallbackBase : java.util.Arrays.asList(langBase, englishBase)) {
                for (int i = 0; i < 2; i++) {
                    boolean includeDescription = i == 0;
                    if (currentLore.equals(LoreComposer.compose(item, blocks.get(0), blocks.get(1), blocks.get(2), blocks.get(3), fallbackBase, includeDescription))) {
                        return true;
                    }
                }
            }
        }

        return false;
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
    /**
     * Re-skins a Slimefun Guide book to the holder's language. The guide is not a {@link SlimefunItem}, so
     * {@link #applyHolderTranslation} skips it; this rebuilds its name + lore from the guide message keys.
     * Identified by the guide-mode PDC tag. Returns true only when the stack was actually changed.
     */
    public boolean applyGuideTranslation(@Nonnull Player p, @Nullable ItemStack stack) {
        if (stack == null || !stack.hasItemMeta()) {
            return false;
        }

        ItemMeta meta = stack.getItemMeta();
        String mode = PdcCompat.getString(meta, Slimefun.getRegistry().getGuideDataKey());

        if (mode == null) {
            return false;
        }

        boolean cheat = "CHEAT_MODE".equals(mode);
        String name = ChatColor.translateAlternateColorCodes('&', Slimefun.getLocalization().getMessage(p, cheat ? "guide.item.cheat-name" : "guide.item.name"));

        List<String> lore = new ArrayList<>();
        lore.add(cheat ? ChatColor.translateAlternateColorCodes('&', Slimefun.getLocalization().getMessage(p, "guide.item.cheat-only")) : "");
        lore.add(ChatColor.translateAlternateColorCodes('&', Slimefun.getLocalization().getMessage(p, "guide.item.browse")));
        lore.add(ChatColor.translateAlternateColorCodes('&', Slimefun.getLocalization().getMessage(p, "guide.item.settings")));

        // No-op when already in the right language, so the periodic inventory sweep stays cheap.
        if (name.equals(meta.getDisplayName()) && lore.equals(meta.getLore())) {
            return false;
        }

        meta.setDisplayName(name);
        meta.setLore(lore);
        stack.setItemMeta(meta);
        return true;
    }

    /**
     * Re-applies per-holder translation to every stack in the player's inventory. Called by the item
     * description toggle so a change takes effect immediately rather than on the next periodic sweep.
     */
    public void retranslateInventory(@Nonnull Player p) {
        org.bukkit.inventory.ItemStack[] contents = p.getInventory().getContents();

        for (int slot = 0; slot < contents.length; slot++) {
            org.bukkit.inventory.ItemStack stack = contents[slot];

            if (applyHolderTranslation(p, stack) | applyGuideTranslation(p, stack)) {
                p.getInventory().setItem(slot, stack);
            }
        }
    }

    /**
     * Whether the item has been migrated to the block lore system: any shipped language has a non-empty
     * type/description/stats/usage block for it. An item with only plain/hardcoded lore is NOT migrated.
     */
    private boolean hasAnyBlock(@Nonnull String itemId) {
        for (Map<String, ItemTranslation> perLanguage : byLanguage.values()) {
            ItemTranslation t = perLanguage.get(itemId);

            if (t != null && (!t.type.isEmpty() || !t.description.isEmpty() || !t.stats.isEmpty() || !t.usage.isEmpty())) {
                return true;
            }
        }

        return false;
    }

    /**
     * Boot audit: warns about every enabled item still using hardcoded/plain lore instead of the
     * en/items.yml block system (Type/Description/Stats/Usage), and writes the full per-addon list to
     * {@code out}. Runs each launch so the migration to the unified lore system stays visible.
     */
    public void auditUnmigratedLore(@Nonnull java.io.File out) {
        Map<String, List<String>> byAddon = new java.util.TreeMap<>();
        int total = 0;

        for (SlimefunItem item : Slimefun.getRegistry().getEnabledSlimefunItems()) {
            try {
                if (hasAnyBlock(item.getId())) {
                    continue;
                }

                ItemStack template = item.getItem();
                List<String> lore = (template != null && template.hasItemMeta()) ? template.getItemMeta().getLore() : null;

                if (lore != null && !lore.isEmpty()) {
                    byAddon.computeIfAbsent(item.getAddon().getName(), k -> new ArrayList<>()).add(item.getId());
                    total++;
                }
            } catch (Exception | LinkageError ignored) {
                // A single broken item must not abort the audit.
            }
        }

        if (total == 0) {
            return;
        }

        Slimefun.logger().log(Level.WARNING, "[lore] {0} item(s) still use the DEPRECATED hardcoded name/lore constructors instead of the block system - move them to en/items.yml (type/description/stats/usage). Full list: {1}", new Object[] { total, out.getName() });

        for (Map.Entry<String, List<String>> entry : byAddon.entrySet()) {
            Slimefun.logger().log(Level.WARNING, "[lore]   {0}: {1} deprecated item(s)", new Object[] { entry.getKey(), entry.getValue().size() });
        }

        org.bukkit.configuration.file.YamlConfiguration config = new org.bukkit.configuration.file.YamlConfiguration();
        config.options().pathSeparator('');

        for (Map.Entry<String, List<String>> entry : byAddon.entrySet()) {
            config.set(entry.getKey(), entry.getValue());
        }

        try {
            config.save(out);
        } catch (java.io.IOException e) {
            Slimefun.logger().log(Level.WARNING, "Failed to write hardcoded-lore audit: {0}", e.getMessage());
        }
    }

    /**
     * Development helper: writes, per language, every enabled item id that has no translation, grouped
     * by addon - the exact remaining gap to fill. Used to audit localization coverage across all loaded
     * addons in one pass.
     */
    public void dumpUntranslated(@Nonnull java.io.File out, @Nonnull List<String> languages) {
        org.bukkit.configuration.file.YamlConfiguration config = new org.bukkit.configuration.file.YamlConfiguration();
        config.options().pathSeparator('\u001F'); // dot-safe separator for addon/id keys

        for (String language : languages) {
            if ("en".equalsIgnoreCase(language)) {
                ensureEnglishBaseline();
            }

            Map<String, ItemTranslation> translated = byLanguage.getOrDefault(language, new HashMap<>());
            Map<String, List<String>> byAddon = new java.util.TreeMap<>();
            int total = 0;

            for (SlimefunItem item : Slimefun.getRegistry().getEnabledSlimefunItems()) {
                try {
                    // Skip items deliberately tagged English-everywhere — the dump lists only real gaps.
                    if (!translated.containsKey(item.getId()) && !FallbackSafe.itemIds().contains(item.getId())) {
                        byAddon.computeIfAbsent(item.getAddon().getName(), k -> new ArrayList<>())
                            .add(item.getId() + "\t" + englishName(item).replace('§', '&'));
                        total++;
                    }
                } catch (Exception | LinkageError ignored) {
                    // A broken item must not break the audit.
                }
            }

            config.set(language + "_total_untranslated", total);

            for (Map.Entry<String, List<String>> entry : byAddon.entrySet()) {
                config.set(language + "\u001F" + entry.getKey(), entry.getValue());
            }
        }

        try {
            config.save(out);
            Slimefun.logger().log(Level.INFO, "Dumped untranslated-item audit to {0}", out.getPath());
        } catch (java.io.IOException e) {
            Slimefun.logger().log(Level.WARNING, "Failed to dump untranslated audit: {0}", e.getMessage());
        }
    }

    public Map<String, int[]> getCoverage(@Nonnull String language) {
        if ("en".equalsIgnoreCase(language)) {
            // English is the language items are authored in. Register each item's built-in English name
            // as a real "en" translation entry so English is counted exactly like any other language -
            // no hardcoded percentage. An en/items.yml, if shipped, is loaded by loadBundled() and wins.
            ensureEnglishBaseline();
        }

        Map<String, ItemTranslation> translated = byLanguage.getOrDefault(language, new HashMap<>());
        Map<String, int[]> coverage = new LinkedHashMap<>();

        for (SlimefunItem item : Slimefun.getRegistry().getEnabledSlimefunItems()) {
            try {
                String plugin = item.getAddon().getName();
                int[] counts = coverage.computeIfAbsent(plugin, k -> new int[2]);
                counts[1]++;

                // A real translation, or an item deliberately tagged as English-everywhere, counts.
                if (translated.containsKey(item.getId()) || FallbackSafe.itemIds().contains(item.getId())) {
                    counts[0]++;
                }
            } catch (Exception | LinkageError ignored) {
                // A broken item should not break the coverage report.
            }
        }

        return coverage;
    }

    /**
     * Tops up the "en" translation map with every enabled item's built-in (authored) English name,
     * without overwriting any explicit en/items.yml entry. Items register over time, so this runs lazily
     * and idempotently. After it, English is just another fully data-backed language - an item with no
     * resolvable English name is genuinely counted as untranslated, rather than English being assumed 100%.
     */
    private void ensureEnglishBaseline() {
        Map<String, ItemTranslation> map = byLanguage.computeIfAbsent("en", k -> new HashMap<>());

        for (SlimefunItem item : Slimefun.getRegistry().getEnabledSlimefunItems()) {
            try {
                if (map.containsKey(item.getId())) {
                    continue;
                }

                String name = englishName(item);

                if (name != null && !ChatColor.stripColor(name).trim().isEmpty()) {
                    map.put(item.getId(), new ItemTranslation(name, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>()));
                }
            } catch (Exception | LinkageError ignored) {
                // A broken item must not break the English baseline.
            }
        }
    }

    /** The authored English name of an item: its pre-bake baseline if it was re-skinned, else its current name. */
    @Nullable
    private String englishName(@Nonnull SlimefunItem item) {
        ItemStack english = englishBaseline.containsKey(item.getId()) ? englishBaseline.get(item.getId()) : item.getItem();

        if (english != null && english.hasItemMeta() && english.getItemMeta().hasDisplayName()) {
            return english.getItemMeta().getDisplayName();
        }

        return item.getItemName();
    }
}
