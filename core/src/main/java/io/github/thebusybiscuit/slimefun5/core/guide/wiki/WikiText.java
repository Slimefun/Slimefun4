package io.github.thebusybiscuit.slimefun5.core.guide.wiki;

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

import org.bukkit.configuration.file.YamlConfiguration;

import com.cryptomorin.xseries.XMaterial;

import io.github.thebusybiscuit.slimefun5.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun5.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun5.core.services.localization.Language;
import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun5.libraries.keys.NamespacedKey;

/**
 * Holds authored, human-written explanation lines for the in-game wiki.
 *
 * Lines are authored per item id (and per mechanic-hub topic) in bundled YAML resources
 * (wiki/items.yml, wiki/mechanics.yml) and may also be supplied by addons at runtime.
 * When no authored text exists for an item, a generic auto-generated fallback is produced.
 */
public final class WikiText {

    private static final String FALLBACK_ITEM_KEY = "guide.wiki.fallback.item";
    private static final String FALLBACK_RECIPE_KEY = "guide.wiki.fallback.recipe";

    private final Map<String, List<String>> itemLines = new HashMap<>();
    private final Map<String, List<String>> mechanicLines = new HashMap<>();
    private final Map<String, List<String>> topicItems = new HashMap<>();
    private final List<WikiTopic> topics = new ArrayList<>();

    /** Per-language overrides for {@link #itemLines}/{@link #mechanicLines}: langId -> id -> lines. */
    private final Map<String, Map<String, List<String>>> itemLinesByLanguage = new HashMap<>();
    private final Map<String, Map<String, List<String>>> mechanicLinesByLanguage = new HashMap<>();

    /** guide.wiki.fallback.item / .recipe, per language id, loaded straight from messages.yml. */
    private final Map<String, String> fallbackItemMessage = new HashMap<>();
    private final Map<String, String> fallbackRecipeMessage = new HashMap<>();

    /** Stores authored explanation lines for the given item id. */
    public synchronized void set(@Nonnull String id, @Nonnull List<String> lines) {
        itemLines.put(id, new ArrayList<>(lines));
    }

    /** Stores authored explanation lines for the given mechanic-hub topic. */
    public synchronized void setMechanic(@Nonnull String id, @Nonnull List<String> lines) {
        mechanicLines.put(id, new ArrayList<>(lines));
    }

    /** Whether authored lines exist for the given item id. */
    public synchronized boolean has(@Nonnull String id) {
        return itemLines.containsKey(id);
    }

    /**
     * Returns authored explanation lines for the given item, or a generic auto-generated
     * fallback derived from the item's group and recipe type if no authored lines exist.
     */
    @Nonnull
    public synchronized List<String> get(@Nonnull SlimefunItem item) {
        return get(item, null);
    }

    /**
     * Returns authored explanation lines for the given item in {@code languageId}, falling back
     * per-key to the English entry, and finally to a generic auto-generated (but still localized)
     * fallback derived from the item's group and recipe type if no authored lines exist in either.
     *
     * @param languageId
     *            the viewing player's language id, or null to use English/the server default
     */
    @Nonnull
    public synchronized List<String> get(@Nonnull SlimefunItem item, @Nullable String languageId) {
        List<String> localized = lookup(itemLinesByLanguage, languageId, item.getId());

        if (localized != null) {
            return new ArrayList<>(localized);
        }

        List<String> authored = itemLines.get(item.getId());

        if (authored != null) {
            return new ArrayList<>(authored);
        }

        return buildFallback(item, languageId);
    }

    /** Returns authored mechanic-hub lines for the topic, or an empty list. */
    @Nonnull
    public synchronized List<String> getMechanic(@Nonnull String id) {
        return getMechanic(id, null);
    }

    /**
     * Returns authored mechanic-hub lines for the topic in {@code languageId}, falling back per-key
     * to the English entry, and finally to an empty list if neither has an entry for this topic.
     */
    @Nonnull
    public synchronized List<String> getMechanic(@Nonnull String id, @Nullable String languageId) {
        List<String> localized = lookup(mechanicLinesByLanguage, languageId, id);

        if (localized != null) {
            return new ArrayList<>(localized);
        }

        List<String> authored = mechanicLines.get(id);

        if (authored != null) {
            return new ArrayList<>(authored);
        }

        return Collections.emptyList();
    }

    @Nullable
    private static List<String> lookup(@Nonnull Map<String, Map<String, List<String>>> byLanguage, @Nullable String languageId, @Nonnull String id) {
        if (languageId == null) {
            return null;
        }

        Map<String, List<String>> perLanguage = byLanguage.get(languageId);
        return perLanguage != null ? perLanguage.get(id) : null;
    }

    /** Stores the list of relevant item ids shown alongside a topic guide. */
    public synchronized void setTopicItems(@Nonnull String topicId, @Nonnull List<String> itemIds) {
        topicItems.put(topicId, new ArrayList<>(itemIds));
    }

    /** Returns the item ids relevant to a topic (rendered as clickable icons), or an empty list. */
    @Nonnull
    public synchronized List<String> getTopicItems(@Nonnull String topicId) {
        List<String> ids = topicItems.get(topicId);

        if (ids != null) {
            return new ArrayList<>(ids);
        }

        return Collections.emptyList();
    }

    /**
     * Loads the bundled wiki resources from the jar. Missing resources are logged and skipped
     * rather than treated as fatal, mirroring the defensive IO handling used by InstallState.
     */
    public void loadBundled() {
        loadResource("/wiki/items.yml", itemLines);
        loadResource("/wiki/mechanics.yml", mechanicLines);
        loadResource("/wiki/topic-items.yml", topicItems);
        loadLanguageOverrides();
        loadFallbackMessages();
        registerCoreTopics();
    }

    /**
     * Loads optional per-language wiki-body overrides ({@code /wiki/<langId>/items.yml} and
     * {@code /wiki/<langId>/mechanics.yml}) for every loaded {@link Language}. A language that ships
     * neither file is simply skipped - this is a clean no-op while no addon/core ships such a file.
     */
    private void loadLanguageOverrides() {
        for (Language language : Slimefun.getLocalization().getLanguages()) {
            String langId = language.getId();
            loadLanguageResource("/wiki/" + langId + "/items.yml", langId, itemLinesByLanguage);
            loadLanguageResource("/wiki/" + langId + "/mechanics.yml", langId, mechanicLinesByLanguage);
        }
    }

    private void loadLanguageResource(@Nonnull String path, @Nonnull String langId, @Nonnull Map<String, Map<String, List<String>>> target) {
        InputStream stream = Slimefun.class.getResourceAsStream(path);

        if (stream == null) {
            return; // no override shipped for this language - expected for every language today
        }

        loadLanguageResource(stream, langId, target, path);
    }

    private synchronized void loadLanguageResource(@Nonnull InputStream stream, @Nonnull String langId, @Nonnull Map<String, Map<String, List<String>>> target) {
        loadLanguageResource(stream, langId, target, "<test>");
    }

    private synchronized void loadLanguageResource(@Nonnull InputStream stream, @Nonnull String langId, @Nonnull Map<String, Map<String, List<String>>> target, @Nonnull String sourceForLogging) {
        try {
            YamlConfiguration config = YamlConfiguration.loadConfiguration(new InputStreamReader(stream, StandardCharsets.UTF_8));
            Map<String, List<String>> perLanguage = target.computeIfAbsent(langId, k -> new HashMap<>());

            for (String key : config.getKeys(false)) {
                perLanguage.put(key, new ArrayList<>(config.getStringList(key)));
            }
        } catch (RuntimeException e) {
            Slimefun.logger().log(Level.WARNING, "Failed to load wiki language override {0}: {1}", new Object[] { sourceForLogging, e.getMessage() });
        }
    }

    /** Loads the guide.wiki.fallback.item/.recipe messages for every loaded {@link Language}. */
    private void loadFallbackMessages() {
        for (Language language : Slimefun.getLocalization().getLanguages()) {
            InputStream stream = Slimefun.class.getResourceAsStream("/languages/" + language.getId() + "/messages.yml");

            if (stream != null) {
                loadFallbackMessage(language.getId(), stream);
            }
        }
    }

    private synchronized void loadFallbackMessage(@Nonnull String langId, @Nonnull InputStream stream) {
        try {
            YamlConfiguration config = YamlConfiguration.loadConfiguration(new InputStreamReader(stream, StandardCharsets.UTF_8));
            String item = config.getString(FALLBACK_ITEM_KEY);
            String recipe = config.getString(FALLBACK_RECIPE_KEY);

            if (item != null) {
                fallbackItemMessage.put(langId, item);
            }

            if (recipe != null) {
                fallbackRecipeMessage.put(langId, recipe);
            }
        } catch (RuntimeException e) {
            Slimefun.logger().log(Level.WARNING, "Failed to load wiki fallback messages for {0}: {1}", new Object[] { langId, e.getMessage() });
        }
    }

    // Package-private seams for headless tests: Slimefun.getLocalization().getLanguages() is always
    // empty under the MockBukkit unit-test harness (see Slimefun#onUnitTestStart), so loadBundled()
    // alone never populates these per-language maps there.
    void loadItemLanguageOverrideForTest(@Nonnull String langId, @Nonnull InputStream stream) {
        loadLanguageResource(stream, langId, itemLinesByLanguage);
    }

    void loadMechanicLanguageOverrideForTest(@Nonnull String langId, @Nonnull InputStream stream) {
        loadLanguageResource(stream, langId, mechanicLinesByLanguage);
    }

    void loadFallbackMessagesForTest(@Nonnull String langId, @Nonnull InputStream stream) {
        loadFallbackMessage(langId, stream);
    }

    /** Registers a guide topic shown on the wiki home. Addons may call this to add their own topics. */
    public synchronized void registerTopic(@Nonnull WikiTopic topic) {
        for (WikiTopic existing : topics) {
            if (existing.getId().equals(topic.getId())) {
                return;
            }
        }

        topics.add(topic);
    }

    /** All registered guide topics, in registration order (core first, then addons). */
    @Nonnull
    public synchronized List<WikiTopic> getTopics() {
        return new ArrayList<>(topics);
    }

    /** The fixed set of core Slimefun guide topics. Their text/items live in the bundled YAML. */
    private void registerCoreTopics() {
        registerTopic(new WikiTopic("getting_started", "Getting Started", XMaterial.MAP, "&7Your first steps in Slimefun"));
        registerTopic(new WikiTopic("research", "Research & Unlocking", XMaterial.EXPERIENCE_BOTTLE, "&7Unlock items with experience"));
        registerTopic(new WikiTopic("multiblocks", "Multiblock Machines", XMaterial.BRICKS, "&7Build structures to craft"));
        registerTopic(new WikiTopic("ore_processing", "Ore Processing", XMaterial.IRON_ORE, "&7Double your ore yields"));
        registerTopic(new WikiTopic("smeltery", "Smeltery & Alloys", XMaterial.FURNACE, "&7Smelt dusts and forge alloys"));
        registerTopic(new WikiTopic("energy", "Energy Networks", XMaterial.REDSTONE, "&7Power your machines"));
        registerTopic(new WikiTopic("power_generation", "Power Generation", XMaterial.COAL_BLOCK, "&7Generators, reactors, capacitors"));
        registerTopic(new WikiTopic("electric_machines", "Electric Machines", XMaterial.IRON_BLOCK, "&7Powered automatic machines"));
        registerTopic(new WikiTopic("cargo", "Cargo Networks", XMaterial.HOPPER, "&7Move items automatically"));
        registerTopic(new WikiTopic("androids", "Programmable Androids", XMaterial.ARMOR_STAND, "&7Automate tasks with robots"));
        registerTopic(new WikiTopic("geo_mining", "GEO Mining & Oil", XMaterial.BUCKET, "&7Extract oil and resources"));
        registerTopic(new WikiTopic("gps", "GPS & Teleportation", XMaterial.COMPASS, "&7Waypoints and teleporters"));
        registerTopic(new WikiTopic("talismans", "Talismans", XMaterial.EMERALD, "&7Passive luck and protection"));
        registerTopic(new WikiTopic("magic", "Magic & the Altar", XMaterial.ENDER_EYE, "&7Runes, staves and rituals"));
        registerTopic(new WikiTopic("armor_gadgets", "Armor & Gadgets", XMaterial.DIAMOND_CHESTPLATE, "&7Jetpacks, sets and tools"));
        registerTopic(new WikiTopic("backpacks", "Backpacks & Storage", XMaterial.CHEST, "&7Portable storage on the go"));
        registerTopic(new WikiTopic("food_farming", "Food & Farming", XMaterial.BREAD, "&7Juices, jerky and auto-farms"));
        registerTopic(new WikiTopic("soulbound", "Soulbound Items", XMaterial.NETHER_STAR, "&7Keep items when you die"));
    }

    private synchronized void loadResource(@Nonnull String path, @Nonnull Map<String, List<String>> target) {
        try {
            InputStream stream = Slimefun.class.getResourceAsStream(path);

            if (stream == null) {
                Slimefun.logger().log(Level.WARNING, "Bundled wiki resource was not found: {0}", path);
                return;
            }

            YamlConfiguration config = YamlConfiguration.loadConfiguration(new InputStreamReader(stream, StandardCharsets.UTF_8));

            for (String key : config.getKeys(false)) {
                target.put(key, new ArrayList<>(config.getStringList(key)));
            }
        } catch (RuntimeException e) {
            Slimefun.logger().log(Level.WARNING, "Failed to load bundled wiki resource {0}: {1}", new Object[] { path, e.getMessage() });
        }
    }

    /** Builds a short, generic explanation from the item's group and recipe type, in {@code languageId}. */
    @Nonnull
    private List<String> buildFallback(@Nonnull SlimefunItem item, @Nullable String languageId) {
        List<String> lines = new ArrayList<>();

        NamespacedKey groupKey = item.getItemGroup().getKey();
        String groupName = groupKey != null ? groupKey.getKey() : "Slimefun";
        lines.add(fallbackMessage(fallbackItemMessage, languageId).replace("%group%", groupName));

        String recipeName = readRecipeName(item.getRecipeType());

        if (recipeName != null) {
            lines.add(fallbackMessage(fallbackRecipeMessage, languageId).replace("%recipe%", recipeName));
        }

        return lines;
    }

    /** {@code languageId}'s message, else English, else a "missing key" marker (never null). */
    @Nonnull
    private static String fallbackMessage(@Nonnull Map<String, String> byLanguage, @Nullable String languageId) {
        String message = languageId != null ? byLanguage.get(languageId) : null;

        if (message != null) {
            return message;
        }

        String english = byLanguage.get("en");
        return english != null ? english : "! Missing wiki fallback message";
    }

    /** Null-safe extraction of a recipe type's key for display. */
    private String readRecipeName(RecipeType recipeType) {
        if (recipeType == null) {
            return null;
        }

        NamespacedKey key = recipeType.getKey();
        return key != null ? key.getKey() : null;
    }
}
