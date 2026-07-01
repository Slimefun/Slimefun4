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

import org.bukkit.configuration.file.YamlConfiguration;

import com.cryptomorin.xseries.XMaterial;

import io.github.thebusybiscuit.slimefun5.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun5.api.recipes.RecipeType;
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

    private final Map<String, List<String>> itemLines = new HashMap<>();
    private final Map<String, List<String>> mechanicLines = new HashMap<>();
    private final Map<String, List<String>> topicItems = new HashMap<>();
    private final List<WikiTopic> topics = new ArrayList<>();

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
        List<String> authored = itemLines.get(item.getId());

        if (authored != null) {
            return new ArrayList<>(authored);
        }

        return buildFallback(item);
    }

    /** Returns authored mechanic-hub lines for the topic, or an empty list. */
    @Nonnull
    public synchronized List<String> getMechanic(@Nonnull String id) {
        List<String> authored = mechanicLines.get(id);

        if (authored != null) {
            return new ArrayList<>(authored);
        }

        return Collections.emptyList();
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
        registerCoreTopics();
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

    /** Builds a short, generic explanation from the item's group and recipe type. */
    @Nonnull
    private List<String> buildFallback(@Nonnull SlimefunItem item) {
        List<String> lines = new ArrayList<>();

        NamespacedKey groupKey = item.getItemGroup().getKey();
        String groupName = groupKey != null ? groupKey.getKey() : "Slimefun";
        lines.add("&7A " + groupName + " item.");

        String recipeName = readRecipeName(item.getRecipeType());

        if (recipeName != null) {
            lines.add("&7Crafted via: &b" + recipeName);
        }

        return lines;
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
