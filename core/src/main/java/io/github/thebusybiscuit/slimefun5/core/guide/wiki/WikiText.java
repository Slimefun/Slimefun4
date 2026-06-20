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

    /**
     * Loads the bundled wiki resources from the jar. Missing resources are logged and skipped
     * rather than treated as fatal, mirroring the defensive IO handling used by InstallState.
     */
    public void loadBundled() {
        loadResource("/wiki/items.yml", false);
        loadResource("/wiki/mechanics.yml", true);
    }

    private void loadResource(@Nonnull String path, boolean mechanic) {
        try {
            InputStream stream = Slimefun.class.getResourceAsStream(path);

            if (stream == null) {
                Slimefun.logger().log(Level.WARNING, "Bundled wiki resource was not found: {0}", path);
                return;
            }

            YamlConfiguration config = YamlConfiguration.loadConfiguration(new InputStreamReader(stream, StandardCharsets.UTF_8));

            for (String key : config.getKeys(false)) {
                List<String> lines = config.getStringList(key);

                if (mechanic) {
                    setMechanic(key, lines);
                } else {
                    set(key, lines);
                }
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
