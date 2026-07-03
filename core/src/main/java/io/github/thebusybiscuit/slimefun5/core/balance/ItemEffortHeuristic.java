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

    private static final int MAX_DEPTH = 8;

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
        // The DEPTH of the deepest chain of intermediate Slimefun ingredients is the dominant signal of
        // how deep in the tech tree an item sits, and it is naturally bounded (0..MAX_DEPTH). Keeping it
        // dominant (rather than SUMMING every ingredient's whole sub-tree, which compounded past 100 for
        // most items) spreads effort across the range so that only the genuinely deepest, gated items
        // approach 100. Machine tier, rare ingredients and a research gate are modest add-ons on top.
        int depth = deepestChain(item, new HashSet<String>(), 0);
        int effort = depth * 11;

        effort += machineCost(item.getRecipeType());
        effort += rareIngredientBonus(item);

        Research research = item.getResearch();

        if (research != null) {
            effort += 6 + Math.min(14, research.getCost() / 2);
        }

        return Math.max(0, Math.min(100, effort));
    }

    /**
     * The longest chain of intermediate Slimefun ingredients beneath this item (0 = crafted only from
     * vanilla materials, 1 = one Slimefun ingredient, ...). {@code path} is a per-branch visited set
     * (added on entry, removed on exit) so it detects recipe cycles without under-counting an item reused
     * across sibling branches; {@link #MAX_DEPTH} caps runaway recursion.
     */
    private int deepestChain(@Nonnull SlimefunItem item, @Nonnull Set<String> path, int depth) {
        if (depth >= MAX_DEPTH || !path.add(item.getId())) {
            return 0;
        }

        int max = 0;
        ItemStack[] recipe = item.getRecipe();

        if (recipe != null) {
            for (ItemStack ingredient : recipe) {
                if (ingredient == null) {
                    continue;
                }

                SlimefunItem sub = safeResolve(ingredient);

                if (sub != null && !sub.getId().equals(item.getId())) {
                    max = Math.max(max, 1 + deepestChain(sub, path, depth + 1));
                }
            }
        }

        path.remove(item.getId());
        return max;
    }

    /** Count of rare vanilla ingredients in the item's OWN recipe (bounded); each is a meaningful cost. */
    private int rareIngredientBonus(@Nonnull SlimefunItem item) {
        int rare = 0;
        ItemStack[] recipe = item.getRecipe();

        if (recipe != null) {
            for (ItemStack ingredient : recipe) {
                if (ingredient != null && RARE.contains(ingredient.getType())) {
                    rare++;
                }
            }
        }

        return Math.min(16, rare * 8);
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
