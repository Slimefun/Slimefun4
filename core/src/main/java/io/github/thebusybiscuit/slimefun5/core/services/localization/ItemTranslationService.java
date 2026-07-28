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
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import io.github.thebusybiscuit.slimefun5.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun5.implementation.items.VanillaItem;

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

    // renderForPacket() runs on the Netty thread and reads this map + its per-language submaps
    // concurrently with ensureEnglishBaseline() (called from getCoverage()/dumpUntranslated() on the
    // main thread post-boot), which structurally mutates both the outer map (computeIfAbsent("en", ...))
    // and the "en" submap (map.put). The outer map must therefore be a ConcurrentHashMap (it holds no
    // null values - keys are language ids, values are submaps), and every submap must itself be a
    // synchronized wrapper (see the two computeIfAbsent creation sites below), so both the read side and
    // the write side go through thread-safe collections.
    private final Map<String, Map<String, ItemTranslation>> byLanguage = new java.util.concurrent.ConcurrentHashMap<>();

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

    // renderForPacket() reads this on the Netty thread (via resolveFamily) concurrently with load()
    // (addon registerTranslations() can run post-boot on the main thread), so this must be a
    // ConcurrentHashMap, and each per-language List<Family> must be published as an immutable,
    // fully-built copy (see load()) rather than mutated in place.
    private final Map<String, List<Family>> familiesByLanguage = new java.util.concurrent.ConcurrentHashMap<>();
    // Memoizes family resolution per (language, id); a null value means "checked, no family matches".
    // renderForPacket() runs on the Netty thread and can call this concurrently with other viewers, so
    // this must be thread-safe. Wrapped (not a ConcurrentHashMap) because it stores null values to
    // memoize negative results, which ConcurrentHashMap forbids; synchronizedMap makes every individual
    // get/put/containsKey atomic, and the resulting check-then-act race (two threads both miss the cache
    // and both recompute) is benign since resolveFamily() is a pure, deterministic function of its input.
    private final Map<String, ItemTranslation> familyResolveCache = Collections.synchronizedMap(new HashMap<>());

    // Pre-bake (English) copies of items whose physical template was re-skinned to the server default.
    // Lets the Guide still show English to a player whose language has no translation. Read by
    // renderForPacket() on the Netty thread, so this must be thread-safe.
    private final Map<String, ItemStack> englishBaseline = new java.util.concurrent.ConcurrentHashMap<>();

    // Ids that have an explicit `name:` entry loaded from some language's items.yml (NOT the authored-name
    // baseline that ensureEnglishBaseline() injects into the "en" map). The boot audit uses this to tell a
    // genuinely localized name from an item that merely keeps its hardcoded English display name. Written
    // on the main thread during load(); read by the audit on the main thread post-boot.
    private final Set<String> explicitNameIds = java.util.concurrent.ConcurrentHashMap.newKeySet();

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
     * canonicalized to their id-name (the packet layer renders the per-viewer translated display).
     */
    public void registerTranslations(@Nonnull JavaPlugin addon) {
        for (Language language : Slimefun.getLocalization().getLanguages()) {
            InputStream stream = addon.getResource("languages/" + language.getId() + "/items.yml");

            if (stream != null) {
                load(language.getId(), stream);
            }
        }

        canonicalizeToId();

        // New translations just became available; drop any renders cached before this addon loaded (an
        // item shown - and cached - as its raw id, or with only-English lore) so they re-render fresh.
        clearRenderCache();
    }

    private void load(@Nonnull String language, @Nonnull InputStream stream) {
        try {
            YamlConfiguration config = YamlConfiguration.loadConfiguration(new InputStreamReader(stream, StandardCharsets.UTF_8));
            // Synchronized (not plain HashMap): renderForPacket() on the Netty thread reads this same
            // submap concurrently with this load() call (addon registerTranslations() can run post-boot).
            Map<String, ItemTranslation> map = byLanguage.computeIfAbsent(language, k -> Collections.synchronizedMap(new HashMap<>()));

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
                        // Copy-on-write: build a new list (old contents + the new Family), sort it, then
                        // publish it as a single immutable replacement - the Netty thread (resolveFamily)
                        // only ever sees a fully-built, stable list, never one being mutated in place.
                        String regex = java.util.regex.Pattern.quote(id).replace(FAMILY_ID_TOKEN, "\\E(.+)\\Q");
                        int specificity = id.replace(FAMILY_ID_TOKEN, "").length();
                        List<Family> existing = familiesByLanguage.get(language);
                        List<Family> updated = new ArrayList<>(existing != null ? existing : Collections.<Family>emptyList());
                        updated.add(new Family(java.util.regex.Pattern.compile("^" + regex + "$"), translation, specificity));
                        updated.sort((a, b) -> Integer.compare(b.specificity, a.specificity));
                        familiesByLanguage.put(language, Collections.unmodifiableList(updated));
                        familyResolveCache.clear();
                    } else {
                        map.put(id, translation);

                        if (name != null) {
                            explicitNameIds.add(id);
                        }
                    }
                }
            }
        } catch (RuntimeException e) {
            Slimefun.logger().log(Level.WARNING, "Failed to load item translations for {0}: {1}", new Object[] { language, e.getMessage() });
        }
    }

    /**
     * Bakes every registered item's template to its display in the server's default language (name +
     * composed lore). The packet layer still renders per-viewer at send time and overrides this for
     * covered surfaces; this baked display is what shows on every surface the packet layer does NOT reach
     * (dropped items, item frames, entity equipment, villager trades, unsupported server versions, or when
     * {@code translation.packets=false}). Baking the translated name here - rather than the raw id - is
     * what stops those surfaces from leaking the raw Slimefun id to players.
     * <p>
     * If nothing resolves yet (e.g. an addon whose translations load later), {@link #renderForPacket} falls
     * back to the English baseline name and, failing that, the raw id; the per-addon re-run of this pass
     * after that addon's translations load then re-bakes it with the real name.
     */
    public void canonicalizeToId() {
        TranslationConfig.FallbackMode fallback = TranslationConfig.fallback();

        for (SlimefunItem item : Slimefun.getRegistry().getEnabledSlimefunItems()) {
            if (item instanceof VanillaItem) {
                continue; // deliberately no custom name/lore so the vanilla client localizes it
            }

            try {
                // Capture the pre-bake authored display first so renderForPacket's English fallback (and the
                // coverage UI) always sees the original name, never a previously baked one.
                englishBaseline.putIfAbsent(item.getId(), item.getItem().clone());

                RenderedDisplay display = renderForPacket(item.getId(), null, fallback, true);

                if (display != null) {
                    item.bakeTranslatedDisplay(display.name, display.lore);
                } else {
                    item.bakeTranslatedDisplay(item.getId(), new ArrayList<String>());
                }
            } catch (Exception | LinkageError ignored) {
                // a single broken item must not abort the pass
            }
        }

        try {
            ensureEnglishBaseline();
        } catch (Exception | LinkageError ignored) {
            // must not abort boot
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

        String cacheKey = language + '|' + itemId;

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
    private static final BlockSelector[] COVERAGE_BLOCKS = { SEL_TYPE, SEL_DESCRIPTION, SEL_STATS, SEL_USAGE };

    /**
     * Resolve a block for a specific primary language: that language's block, else {@code fallbackLanguage}'s,
     * else empty. The holder path passes the server default language as the fallback; the packet path passes
     * english, so its lore falls back through the same chain as its name (see renderForPacket).
     */
    @Nonnull
    private List<String> blockForLanguage(@Nullable String language, @Nullable String fallbackLanguage, @Nonnull SlimefunItem item, @Nonnull BlockSelector selector) {
        ItemTranslation primary = lookup(language, item.getId());
        if (primary != null) {
            List<String> block = selector.select(primary);
            if (!block.isEmpty()) {
                return block;
            }
        }
        if (fallbackLanguage != null && !fallbackLanguage.equals(language)) {
            ItemTranslation def = lookup(fallbackLanguage, item.getId());
            if (def != null) {
                return selector.select(def);
            }
        }
        return Collections.<String>emptyList();
    }

    /** Thin delegate: fallback language is the server default (unchanged behavior for the holder path). */
    @Nonnull
    private List<String> blockForLanguage(@Nullable String language, @Nonnull SlimefunItem item, @Nonnull BlockSelector selector) {
        Language defaultLanguage = Slimefun.getLocalization().getDefaultLanguage();
        return blockForLanguage(language, defaultLanguage != null ? defaultLanguage.getId() : null, item, selector);
    }

    /** [type, description, stats, usage] for a primary language, each falling back to {@code fallbackLanguage}. */
    @Nonnull
    private List<List<String>> resolveBlocks(@Nullable String language, @Nullable String fallbackLanguage, @Nonnull SlimefunItem item) {
        List<List<String>> blocks = new ArrayList<>(4);
        blocks.add(blockForLanguage(language, fallbackLanguage, item, SEL_TYPE));
        blocks.add(blockForLanguage(language, fallbackLanguage, item, SEL_DESCRIPTION));
        blocks.add(blockForLanguage(language, fallbackLanguage, item, SEL_STATS));
        blocks.add(blockForLanguage(language, fallbackLanguage, item, SEL_USAGE));
        return blocks;
    }

    /** Thin delegate: fallback language is the server default (unchanged behavior for the holder path). */
    @Nonnull
    private List<List<String>> resolveBlocks(@Nullable String language, @Nonnull SlimefunItem item) {
        Language defaultLanguage = Slimefun.getLocalization().getDefaultLanguage();
        return resolveBlocks(language, defaultLanguage != null ? defaultLanguage.getId() : null, item);
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

    /** A rendered per-viewer display: translated name + composed block lore. */
    public static final class RenderedDisplay {
        public final String name;
        public final List<String> lore;

        RenderedDisplay(String name, List<String> lore) {
            this.name = name;
            // Defensive unmodifiable copy: this instance is cached and handed out to every caller that
            // hits the cache, so a downstream mutation of a plain mutable list would corrupt the shared
            // copy for every other viewer.
            this.lore = Collections.unmodifiableList(new ArrayList<>(lore));
        }
    }

    private final Map<String, RenderedDisplay> renderCache = new java.util.concurrent.ConcurrentHashMap<>();

    /** Empties the per-(id,language,fallback) render cache (call on reload). */
    public void clearRenderCache() {
        renderCache.clear();
    }

    /**
     * Renders an item's per-viewer display (name + composed lore) for the given language. Pure and
     * thread-safe: reads only the loaded translation data (populated once at boot, read-only afterward)
     * plus the thread-safe render cache, so it is safe to call from the Netty thread. Returns null if
     * the id is not a registered Slimefun item.
     *
     * <p>The viewer's language is resolved ONCE, up front, into a single effective language: the given
     * {@code languageId} if non-null, otherwise the server's default language id (or null if there is
     * no default language). That single effective language is then used for BOTH the name lookup and
     * the lore blocks, so the two can never resolve against different languages. Only once that lookup
     * comes back empty does the {@code fallback} mode decide the missing label (the raw id, or the
     * English baseline name/lore).
     *
     * @param id         the Slimefun item id
     * @param languageId the viewer's language id, or null to use the server's default language
     * @param fallback   what a missing label becomes (ENGLISH or ID)
     */
    public RenderedDisplay renderForPacket(@Nonnull String id, @Nullable String languageId, @Nonnull TranslationConfig.FallbackMode fallback, boolean includeDescription) {
        SlimefunItem item = SlimefunItem.getById(id);
        if (item == null) {
            return null;
        }

        String cacheKey = id + '|' + languageId + '|' + fallback + '|' + includeDescription;
        RenderedDisplay cached = renderCache.get(cacheKey);
        if (cached != null) {
            return cached;
        }

        // Resolve once so the name and the lore blocks below can never disagree about which language
        // they rendered (previously: a null languageId made the name skip straight to the ENGLISH/ID
        // fallback while the lore, via resolveBlocks -> blockForLanguage, still fell back to the server
        // default language - an inconsistent pair of displays for the exact same render call).
        String effectiveLanguage = resolveEffectiveLanguage(languageId);
        ItemTranslation translation = lookup(effectiveLanguage, id);
        ItemStack english = englishBaseline.get(id);

        // Name: language label -> (missing) fallback english baseline or raw id.
        String name;
        if (translation != null && translation.name != null) {
            name = ChatColor.translateAlternateColorCodes('&', translation.name);
        } else if (fallback == TranslationConfig.FallbackMode.ID) {
            name = id;
        } else {
            ItemTranslation en = lookup("en", id);
            if (en != null && en.name != null) {
                name = ChatColor.translateAlternateColorCodes('&', en.name);
            } else {
                ItemMeta englishNameMeta = english != null ? english.getItemMeta() : item.getItem().getItemMeta();
                name = (englishNameMeta != null && englishNameMeta.hasDisplayName()) ? englishNameMeta.getDisplayName() : id;
            }
        }

        // Lore: composed blocks in the SAME effective language, falling back to ENGLISH - never the
        // server default - so a missing label can't show an english name with server-default lore.
        List<List<String>> blocks = resolveBlocks(effectiveLanguage, "en", item);
        ItemMeta englishMeta = english != null ? english.getItemMeta() : null;
        List<String> englishLore = (englishMeta != null && englishMeta.getLore() != null) ? englishMeta.getLore() : new ArrayList<String>();
        ItemTranslation englishTranslation = (translation == null) ? lookup("en", id) : null;
        List<String> fallbackBase = (translation != null && !translation.lore.isEmpty()) ? translation.lore
            : (englishTranslation != null && !englishTranslation.lore.isEmpty()) ? englishTranslation.lore
            : englishLore;
        List<String> lore = LoreComposer.compose(item, blocks.get(0), blocks.get(1), blocks.get(2), blocks.get(3), fallbackBase, includeDescription, effectiveLanguage);

        RenderedDisplay result = new RenderedDisplay(name, lore);

        // The raw id is only ever a fallback signal: no real translation (or English baseline name) was
        // resolvable yet - e.g. a packet rendered the item during boot, before an addon's translations
        // loaded. Never memoize that: a cached raw id would stick for that (id, language) until a full
        // restart (clearRenderCache is not called at runtime), which is exactly the "item intermittently
        // shows its raw id" bug. Leaving it uncached lets the next packet re-render it correctly the moment
        // the translation/baseline becomes available.
        if (!name.equals(id)) {
            renderCache.put(cacheKey, result);
        }

        return result;
    }

    /** The given language id, or - when null - the server default language's id (or null if there is none). */
    @Nullable
    private String resolveEffectiveLanguage(@Nullable String languageId) {
        if (languageId != null) {
            return languageId;
        }

        Language defaultLanguage = Slimefun.getLocalization().getDefaultLanguage();
        return defaultLanguage != null ? defaultLanguage.getId() : null;
    }

    /**
     * Whether the item's author marked its enchantment(s) hidden ({@code ItemFlag.HIDE_ENCHANTS}) on the
     * original, pre-bake template - the standard "glow only" trick, where a meaningless enchant is added
     * purely for the enchanted glint. Read from the English baseline (captured before our own bake adds
     * HIDE_ENCHANTS to re-render enchants) so the re-render pass never mistakes its own flag for the
     * author's intent. {@link EnchantDisplay} keeps such enchants hidden (no lore line), leaving only the glint.
     */
    public boolean wasAuthorEnchantHidden(@Nonnull String id) {
        if (io.github.thebusybiscuit.slimefun5.utils.compatibility.VersionedItemFlag.HIDE_ENCHANTS == null) {
            return false;
        }

        ItemStack baseline = englishBaseline.get(id);

        if (baseline == null || !baseline.hasItemMeta()) {
            return false;
        }

        ItemMeta meta = baseline.getItemMeta();
        return meta != null
            && meta.hasItemFlag(io.github.thebusybiscuit.slimefun5.utils.compatibility.VersionedItemFlag.HIDE_ENCHANTS);
    }

    /**
     * Whether the item has been migrated to the block lore system: any shipped language has a non-empty
     * type/description/stats/usage block for it. An item with only plain/hardcoded lore is NOT migrated.
     */
    private boolean hasAnyBlock(@Nonnull String itemId) {
        for (Map<String, ItemTranslation> perLanguage : byLanguage.values()) {
            ItemTranslation t = perLanguage.get(itemId);

            if (t != null && hasBlockContent(t)) {
                return true;
            }
        }

        // Family-covered ids (e.g. ZOMBIE_SOUL_JAR resolved from a %MOB%_SOUL_JAR template) have no direct
        // map entry - their blocks come from the template, so check families too or they read as unmigrated.
        for (String language : familiesByLanguage.keySet()) {
            ItemTranslation t = resolveFamily(language, itemId);

            if (t != null && hasBlockContent(t)) {
                return true;
            }
        }

        return false;
    }

    private static boolean hasBlockContent(@Nonnull ItemTranslation t) {
        return !t.type.isEmpty() || !t.description.isEmpty() || !t.stats.isEmpty() || !t.usage.isEmpty();
    }

    /**
     * Whether the item has an explicit translated name: either a direct {@code name:} entry loaded from
     * some language's items.yml, or a family template that resolves a name for this id. The authored
     * English baseline injected by {@link #ensureEnglishBaseline()} does NOT count - an item that merely
     * keeps its hardcoded English display name is still an un-localized gap the audit should surface.
     */
    private boolean hasNameTranslation(@Nonnull String itemId) {
        if (explicitNameIds.contains(itemId)) {
            return true;
        }

        for (String language : familiesByLanguage.keySet()) {
            ItemTranslation t = resolveFamily(language, itemId);

            if (t != null && t.name != null) {
                return true;
            }
        }

        return false;
    }

    /**
     * Boot audit of translation coverage: for every enabled item (except {@link VanillaItem}), flags two
     * independent gaps and writes the full per-addon breakdown to {@code out}. Runs each launch so the
     * migration to the unified name/lore system stays visible.
     *
     * <ul>
     *   <li><b>untranslated-name</b> - no explicit {@code name:} entry in any language's items.yml (and not
     *       deliberately English-everywhere via {@link FallbackSafe}). Such an item shows its hardcoded
     *       English name, or - if it has none - the raw vanilla/id name to every viewer. This is why e.g.
     *       SlimeTinker's assembled tools look untranslated even though the addon ships an items.yml: the
     *       registered id simply isn't a key in it.</li>
     *   <li><b>hardcoded-lore</b> - the template still carries hardcoded lore and has no
     *       type/description/stats/usage block, so its lore can't be localized.</li>
     * </ul>
     *
     * Both checks are family-aware (an id resolved from a {@code %MOB%} template counts as covered), so
     * runtime-generated item families are not false-positived.
     */
    public void auditUnmigratedLore(@Nonnull java.io.File out) {
        Set<String> fallbackSafe = FallbackSafe.itemIds();
        Map<String, List<String>> nameGaps = new java.util.TreeMap<>();
        Map<String, List<String>> loreGaps = new java.util.TreeMap<>();
        int totalName = 0;
        int totalLore = 0;

        for (SlimefunItem item : Slimefun.getRegistry().getEnabledSlimefunItems()) {
            try {
                if (item instanceof VanillaItem) {
                    continue; // deliberately no custom name/lore - the vanilla client localizes it
                }

                String id = item.getId();
                String addon = item.getAddon().getName();

                if (!hasNameTranslation(id) && !fallbackSafe.contains(id)) {
                    nameGaps.computeIfAbsent(addon, k -> new ArrayList<>()).add(id);
                    totalName++;
                }

                if (!hasAnyBlock(id)) {
                    ItemStack template = item.getItem();
                    List<String> lore = (template != null && template.hasItemMeta()) ? template.getItemMeta().getLore() : null;

                    if (lore != null && !lore.isEmpty()) {
                        loreGaps.computeIfAbsent(addon, k -> new ArrayList<>()).add(id);
                        totalLore++;
                    }
                }
            } catch (Exception | LinkageError ignored) {
                // A single broken item must not abort the audit.
            }
        }

        if (totalName == 0 && totalLore == 0) {
            return;
        }

        Slimefun.logger().log(Level.WARNING, "[lore] {0} item(s) with an untranslated name and {1} item(s) still using hardcoded lore (move both to en/items.yml: name + type/description/stats/usage). Full list: {2}", new Object[] { totalName, totalLore, out.getName() });

        Set<String> auditedAddons = new java.util.TreeSet<>();
        auditedAddons.addAll(nameGaps.keySet());
        auditedAddons.addAll(loreGaps.keySet());

        for (String addon : auditedAddons) {
            int names = nameGaps.getOrDefault(addon, Collections.<String>emptyList()).size();
            int lores = loreGaps.getOrDefault(addon, Collections.<String>emptyList()).size();
            Slimefun.logger().log(Level.WARNING, "[lore]   {0}: {1} untranslated-name, {2} hardcoded-lore", new Object[] { addon, names, lores });
        }

        org.bukkit.configuration.file.YamlConfiguration config = new org.bukkit.configuration.file.YamlConfiguration();
        config.options().pathSeparator('');

        char sep = config.options().pathSeparator();

        for (String addon : auditedAddons) {
            List<String> names = nameGaps.get(addon);
            List<String> lores = loreGaps.get(addon);

            if (names != null) {
                config.set(addon + sep + "untranslated_name", names);
            }

            if (lores != null) {
                config.set(addon + sep + "hardcoded_lore", lores);
            }
        }

        try {
            config.save(out);
        } catch (java.io.IOException e) {
            Slimefun.logger().log(Level.WARNING, "Failed to write translation-coverage audit: {0}", e.getMessage());
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

    private static boolean nonEmpty(@Nullable String s) {
        return s != null && !s.trim().isEmpty();
    }

    private static boolean nonEmpty(@Nonnull List<String> lines) {
        return !lines.isEmpty();
    }

    /**
     * Leaf-based coverage of a language per plugin: pluginName -> [coveredUnits, totalUnits], over all
     * enabled items grouped by their addon. A unit is one of an item's translatable leaves: {@code name}
     * (if English has one) plus each of {@code type}/{@code description}/{@code stats}/{@code usage} that
     * English defines non-empty. This counts real block coverage (not mere entry presence), so a language
     * with translated names but untranslated lore blocks scores well below 100%. An item id tagged in
     * {@link FallbackSafe#itemIds()} counts every one of its English units as covered (deliberately
     * English-everywhere). Items with zero English units contribute nothing (they can't inflate or
     * deflate the percentage). Powers the translation-percentage UI.
     */
    @Nonnull
    public Map<String, int[]> getItemUnitCoverage(@Nonnull String language) {
        // English units are the yardstick for every language (including English itself), so the baseline
        // must exist regardless of which language's coverage is being computed.
        ensureEnglishBaseline();

        boolean isEnglish = "en".equalsIgnoreCase(language);
        Map<String, ItemTranslation> englishMap = byLanguage.getOrDefault("en", Collections.<String, ItemTranslation>emptyMap());
        Map<String, ItemTranslation> langMap = isEnglish ? englishMap : byLanguage.getOrDefault(language, Collections.<String, ItemTranslation>emptyMap());
        Set<String> fallbackSafe = FallbackSafe.itemIds();
        Map<String, int[]> coverage = new LinkedHashMap<>();

        for (SlimefunItem item : Slimefun.getRegistry().getEnabledSlimefunItems()) {
            try {
                String id = item.getId();
                ItemTranslation english = englishMap.get(id);

                if (english == null) {
                    continue;
                }

                boolean safe = isEnglish || fallbackSafe.contains(id);
                ItemTranslation lang = langMap.get(id);

                int englishUnits = 0;
                int coveredUnits = 0;

                if (nonEmpty(english.name)) {
                    englishUnits++;

                    if (safe || (lang != null && nonEmpty(lang.name))) {
                        coveredUnits++;
                    }
                }

                for (BlockSelector selector : COVERAGE_BLOCKS) {
                    if (nonEmpty(selector.select(english))) {
                        englishUnits++;

                        if (safe || (lang != null && nonEmpty(selector.select(lang)))) {
                            coveredUnits++;
                        }
                    }
                }

                if (englishUnits == 0) {
                    continue;
                }

                int[] counts = coverage.computeIfAbsent(item.getAddon().getName(), k -> new int[2]);
                counts[0] += coveredUnits;
                counts[1] += englishUnits;
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
        // Synchronized (not plain HashMap): this runs post-boot on the main thread (from getCoverage()/
        // dumpUntranslated()) while renderForPacket() may concurrently read this same "en" submap on the
        // Netty thread.
        Map<String, ItemTranslation> map = byLanguage.computeIfAbsent("en", k -> Collections.synchronizedMap(new HashMap<>()));

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
