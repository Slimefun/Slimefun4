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

    /**
     * Generates one guide topic per installed addon (skipping core Slimefun), listing that addon's
     * items as clickable related items. Must run after all addons have registered their items.
     */
    public synchronized void generateAddonTopics() {
        Map<String, List<String>> itemsByAddon = new java.util.LinkedHashMap<>();
        Map<String, java.util.Set<String>> groupsByAddon = new HashMap<>();

        for (SlimefunItem item : Slimefun.getRegistry().getEnabledSlimefunItems()) {
            try {
                String addon = item.getAddon().getName();

                if (addon == null || addon.equalsIgnoreCase("Slimefun")) {
                    continue;
                }

                itemsByAddon.computeIfAbsent(addon, k -> new ArrayList<>()).add(item.getId());
                groupsByAddon.computeIfAbsent(addon, k -> new java.util.HashSet<>()).add(item.getItemGroup().getKey().toString());
            } catch (Exception | LinkageError ignored) {
                // An addon item with broken metadata should not break the whole wiki.
            }
        }

        for (Map.Entry<String, List<String>> entry : itemsByAddon.entrySet()) {
            String addon = entry.getKey();
            List<String> itemIds = entry.getValue();
            int groupCount = groupsByAddon.getOrDefault(addon, Collections.emptySet()).size();
            String topicId = "addon_" + addon.toLowerCase(java.util.Locale.ROOT);

            List<String> overview = authoredAddonOverview(addon);

            List<String> lines = new ArrayList<>();

            if (overview != null) {
                lines.addAll(overview);
            } else {
                lines.add("&7Items added by the &b" + addon + "&7 addon.");
            }

            lines.add("");
            lines.add("&7Adds &e" + itemIds.size() + "&7 items across");
            lines.add("&e" + groupCount + "&7 " + (groupCount == 1 ? "category" : "categories") + ".");
            lines.add("");
            lines.add("&7Click any item below to open its");
            lines.add("&7page with recipe and details.");

            String tagline = (overview != null && !overview.isEmpty()) ? overview.get(0) : ("&7" + itemIds.size() + " items from this addon");

            setMechanic(topicId, lines);
            setTopicItems(topicId, itemIds);
            registerTopic(new WikiTopic(topicId, addon, XMaterial.BOOK, tagline));
        }
    }

    /**
     * Hand-written overview lines for the well-known addons (first line doubles as the home-screen
     * tagline). Returns null for an unknown addon, in which case a generic blurb is used.
     */
    private List<String> authoredAddonOverview(@Nonnull String addon) {
        String key = addon.toLowerCase(java.util.Locale.ROOT);

        if (key.contains("infinityexpansion")) {
            return overview("&7End-game tech and storage.", "&7Infinity-tier machines, huge", "&7storage units, mob & material", "&7chambers and infinite resource", "&7generation for the late game.");
        } else if (key.contains("networks")) {
            return overview("&7Item storage & transport networks.", "&7An alternative to cargo: quantum", "&7storage cells, crafting grids and", "&7import/export nodes wired to a", "&7central Network Controller.");
        } else if (key.contains("chestterminal")) {
            return overview("&7Remote access to your cargo.", "&7Access Terminals view and withdraw", "&7items from anywhere on a cargo", "&7network; import/export buses move", "&7items in and out automatically.");
        } else if (key.contains("fluffymachines")) {
            return overview("&7Quality-of-life machines.", "&7Auto crafters, storage barrels,", "&7ender-chest links and handy utility", "&7machines that streamline automation.");
        } else if (key.contains("dynatech")) {
            return overview("&7Machines driven by motion.", "&7Water mills, wind mills and", "&7momentum-based generators, plus", "&7advanced processing machines.");
        } else if (key.contains("litexpansion")) {
            return overview("&7IndustrialCraft-style tech.", "&7UU-Matter, a Mass Fabricator,", "&7electric tools and reactors for a", "&7classic industrial progression.");
        } else if (key.contains("slimetinker")) {
            return overview("&7Modular, upgradeable tools.", "&7Build tools and armor from parts", "&7at workstations, each with traits", "&7and levels you can customise.");
        } else if (key.contains("exoticgarden")) {
            return overview("&7New crops, fruit and food.", "&7Adds berries, fruit trees, bushes", "&7and plants, plus a kitchen to cook", "&7them into new dishes.");
        } else if (key.contains("sensibletoolbox")) {
            return overview("&7Ported STB machines & gadgets.", "&7Brings Sensible Toolbox's machines", "&7(mashers, smelters and more) and", "&7gadgets in through Slimefun.");
        } else if (key.contains("luckyblock")) {
            return overview("&7Risk it for a surprise.", "&7Break a Lucky Block for a random", "&7reward - or a nasty surprise.");
        } else if (key.contains("missilewarfare")) {
            return overview("&7Build, launch and intercept.", "&7Missiles, launchers, anti-air and", "&7mines for large-scale warfare.");
        } else if (key.contains("extragear")) {
            return overview("&7Extra tools and armor sets.", "&7Additional gear and full armor", "&7sets crafted through Slimefun.");
        } else if (key.contains("galactifun")) {
            return overview("&7Explore space.", "&7Travel to planets and moons in", "&7rockets, wear space suits and", "&7harvest alien resources.");
        }

        return null;
    }

    @Nonnull
    private List<String> overview(@Nonnull String... lines) {
        return new ArrayList<>(java.util.Arrays.asList(lines));
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
