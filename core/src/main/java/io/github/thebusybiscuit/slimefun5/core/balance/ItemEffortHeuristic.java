package io.github.thebusybiscuit.slimefun5.core.balance;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun5.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun5.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun5.api.researches.Research;

/**
 * Estimates how HARD an item is to obtain (0 = trivial, 100 = maximal grind) - the "effort" axis that
 * pairs with the authored power score. Balance is power RELATIVE TO effort: a strong item that is cheap
 * to make is overpowered, while an equally strong item gated behind a deep tech tree is merely endgame.
 *
 * <p>Effort is derived objectively from the item itself - the crafting-tree depth (how many layers of
 * intermediate Slimefun items it takes), rare vanilla ingredients, the machine tier required, and any
 * research gate - so it needs no manual authoring (unlike the power score).
 */
public final class ItemEffortHeuristic {

    private static final int MAX_DEPTH = 6;

    /** Rare/expensive vanilla materials that meaningfully raise the cost of any recipe using them. */
    private static final Set<Material> RARE = new HashSet<>();

    static {
        for (String name : new String[] {
            "NETHERITE_INGOT", "NETHERITE_SCRAP", "NETHERITE_BLOCK", "ANCIENT_DEBRIS", "NETHER_STAR",
            "DRAGON_EGG", "DRAGON_BREATH", "ELYTRA", "BEACON", "CONDUIT", "HEART_OF_THE_SEA",
            "ENCHANTED_GOLDEN_APPLE", "TOTEM_OF_UNDYING", "SHULKER_SHELL", "TRIDENT", "NETHERITE_UPGRADE_SMITHING_TEMPLATE"
        }) {
            Material m = matchMaterial(name);

            if (m != null) {
                RARE.add(m);
            }
        }
    }

    @Nullable
    private static Material matchMaterial(@Nonnull String name) {
        try {
            return Material.valueOf(name);
        } catch (IllegalArgumentException e) {
            return null; // material not present on this Minecraft version
        }
    }

    /** Effort score in 0..100 for the given item. */
    public int estimate(@Nonnull SlimefunItem item) {
        int effort = walk(item, new HashSet<String>(), 0);

        // A research/unlock gate makes an item harder to reach than one you can craft immediately.
        Research research = item.getResearch();

        if (research != null) {
            effort += 10 + Math.min(20, research.getCost());
        }

        return Math.max(0, Math.min(100, effort));
    }

    /**
     * Recursively scores the crafting sub-tree: the crafting machine's tier is a base cost, each rare
     * vanilla ingredient adds cost, and every intermediate Slimefun ingredient is a crafting step of its
     * own (a flat cost plus a halved share of its own sub-tree, so deeper layers matter progressively
     * less). The visited set (by id) guards against recipe cycles; {@link #MAX_DEPTH} caps recursion.
     */
    private int walk(@Nonnull SlimefunItem item, @Nonnull Set<String> visited, int depth) {
        if (depth >= MAX_DEPTH || !visited.add(item.getId())) {
            return 0;
        }

        int effort = machineCost(item.getRecipeType());
        ItemStack[] recipe = item.getRecipe();

        if (recipe != null) {
            for (ItemStack ingredient : recipe) {
                if (ingredient == null) {
                    continue;
                }

                if (RARE.contains(ingredient.getType())) {
                    effort += 12;
                }

                SlimefunItem sub = safeResolve(ingredient);

                if (sub != null && !sub.getId().equals(item.getId())) {
                    effort += 6 + walk(sub, visited, depth + 1) / 2;
                }
            }
        }

        return effort;
    }

    @Nullable
    private SlimefunItem safeResolve(@Nonnull ItemStack ingredient) {
        try {
            return SlimefunItem.getByItem(ingredient);
        } catch (Exception | LinkageError e) {
            return null;
        }
    }

    /** Rough base cost of the machine an item is crafted in (hand/table cheap, advanced machines dear). */
    private int machineCost(@Nullable RecipeType type) {
        if (type == null || type.getKey() == null) {
            return 0;
        }

        switch (type.getKey().getKey()) {
            case "enhanced_crafting_table":
            case "grind_stone":
            case "gold_pan":
            case "juicer":
            case "ore_crusher":
            case "mob_drop":
            case "barter_drop":
                return 2;
            case "compressor":
            case "ore_washer":
            case "smeltery":
            case "armor_forge":
            case "magic_workbench":
                return 6;
            case "pressure_chamber":
            case "heated_pressure_chamber":
            case "food_fabricator":
            case "food_composter":
            case "freezer":
            case "refinery":
            case "geo_miner":
                return 10;
            case "ancient_altar":
            case "nuclear_reactor":
                return 16;
            default:
                return 4;
        }
    }
}
