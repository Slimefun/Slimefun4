package io.github.thebusybiscuit.slimefun5.core.guide.wiki;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun5.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun5.core.multiblocks.MultiBlockMachine;
import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;

/**
 * Maps an item id to the list of {@link SlimefunItem}s whose recipe consumes that item
 * (the inverse of a recipe lookup, powering the wiki's "used in" section).
 *
 * Core exposes no such reverse mapping, so this builds one by scanning the registry once
 * and caching the result. The very first call is heavy (it resolves every ingredient stack
 * of every enabled item via {@link SlimefunItem#getByItem(ItemStack)}); it should ideally run
 * off the main thread, or be accepted as a one-time cost on the first wiki open.
 */
public final class ReverseRecipeIndex {

    private volatile Map<String, List<SlimefunItem>> cache;
    private final Object lock = new Object();

    /**
     * Returns the enabled items that consume the given item in a recipe, or an empty list.
     * The returned list is unmodifiable.
     */
    @Nonnull
    public List<SlimefunItem> getConsumers(@Nonnull SlimefunItem target) {
        Map<String, List<SlimefunItem>> index = cache;

        if (index == null) {
            synchronized (lock) {
                if (cache == null) {
                    cache = buildIndex();
                }

                index = cache;
            }
        }

        List<SlimefunItem> consumers = index.get(target.getId());

        if (consumers == null) {
            return Collections.emptyList();
        }

        return Collections.unmodifiableList(consumers);
    }

    /** Scans every enabled item once and groups consumers by the ingredient ids they use. */
    @Nonnull
    private Map<String, List<SlimefunItem>> buildIndex() {
        Map<String, List<SlimefunItem>> consumersByIngredient = new HashMap<>();
        // Tracks which consumer ids are already recorded per ingredient to avoid duplicates.
        Map<String, Set<String>> seenByIngredient = new HashMap<>();

        for (SlimefunItem consumer : Slimefun.getRegistry().getEnabledSlimefunItems()) {
            indexConsumer(consumer, consumersByIngredient, seenByIngredient);
        }

        return consumersByIngredient;
    }

    private void indexConsumer(@Nonnull SlimefunItem consumer, @Nonnull Map<String, List<SlimefunItem>> consumersByIngredient, @Nonnull Map<String, Set<String>> seenByIngredient) {
        // getByItem can be expensive and may throw on unusual stacks, so isolate each consumer.
        try {
            for (ItemStack ingredient : collectIngredients(consumer)) {
                addIngredient(consumer, ingredient, consumersByIngredient, seenByIngredient);
            }
        } catch (Exception | LinkageError e) {
            Slimefun.logger().log(Level.WARNING, "Failed to index recipe of {0} for the wiki: {1}", new Object[] { consumer.getId(), e });
        }
    }

    /** Collects every ingredient stack of a consumer, including multiblock recipe arrays. */
    @Nonnull
    private List<ItemStack> collectIngredients(@Nonnull SlimefunItem consumer) {
        List<ItemStack> ingredients = new ArrayList<>();

        addStacks(ingredients, consumer.getRecipe());

        if (consumer instanceof MultiBlockMachine) {
            // Multiblock recipes alternate input/output arrays by convention; scanning every
            // array (output stacks resolve to their own item, not the consumer's) is harmless.
            for (ItemStack[] recipe : ((MultiBlockMachine) consumer).getRecipes()) {
                addStacks(ingredients, recipe);
            }
        }

        return ingredients;
    }

    private void addStacks(@Nonnull List<ItemStack> target, @Nullable ItemStack[] stacks) {
        if (stacks == null) {
            return;
        }

        for (ItemStack stack : stacks) {
            if (stack != null) {
                target.add(stack);
            }
        }
    }

    private void addIngredient(@Nonnull SlimefunItem consumer, @Nonnull ItemStack ingredient, @Nonnull Map<String, List<SlimefunItem>> consumersByIngredient, @Nonnull Map<String, Set<String>> seenByIngredient) {
        SlimefunItem resolved = SlimefunItem.getByItem(ingredient);

        if (resolved == null) {
            return;
        }

        String ingredientId = resolved.getId();

        // Skip self-references (an item listing itself as its own ingredient).
        if (ingredientId.equals(consumer.getId())) {
            return;
        }

        Set<String> seen = seenByIngredient.computeIfAbsent(ingredientId, key -> new HashSet<>());

        if (seen.add(consumer.getId())) {
            consumersByIngredient.computeIfAbsent(ingredientId, key -> new ArrayList<>()).add(consumer);
        }
    }
}
