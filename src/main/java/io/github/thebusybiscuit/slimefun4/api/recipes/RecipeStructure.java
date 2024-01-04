package io.github.thebusybiscuit.slimefun4.api.recipes;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.recipes.components.RecipeComponent;

public abstract class RecipeStructure {

    /**
     * <b>Note:</b> This structure assumes the recipe inputs and the given
     * inputs have the same dimension
     */
    public static final RecipeStructure IDENTICAL = new RecipeStructure() {
        @Override
        public RecipeMatchResult match(ItemStack[] givenItems, List<RecipeComponent> components) {
            if (givenItems.length != components.size()) {
                return RecipeMatchResult.NO_MATCH;
            }

            final Map<Integer, Integer> consumption = new HashMap<>();
            for (int i = 0; i < givenItems.length; i++) {
                final RecipeComponent component = components.get(i);

                if (!component.matches(givenItems[i])) {
                    return RecipeMatchResult.NO_MATCH;
                }

                if (!component.isAir()) {
                    consumption.put(i, component.getAmount());
                }
            }

            return RecipeMatchResult.match(consumption);
        }
    };

    /**
     * <b>Note:</b> This structure assumes the recipe is 3x3
     */
    public static final RecipeStructure SHAPED = new RecipeStructure() {
        @Override
        public RecipeMatchResult match(ItemStack[] givenItems, List<RecipeComponent> components) {
            if (givenItems.length != components.size() || givenItems.length != 9) {
                return RecipeMatchResult.NO_MATCH;
            }

            final int height = 3;
            final int numSlots = 9;

            // Get the first non-empty slot of the recipe
            int recipeFirstNonnull = 0;
            for (int i = 0; i < numSlots; i++) {
                if (!components.get(i).isAir()) {
                    recipeFirstNonnull = i;
                }
            }

            // Get the first non-empty slot of the given items
            int givenFirstNonnull = 0;
            for (int i = 0; i < numSlots; i++) {
                if (givenItems[i] != null && !givenItems[i].getType().isAir()) {
                    givenFirstNonnull = i;
                    break;
                }
            }

            // Same procedure as IDENTICAL, with the additional caveat that two
            // matching (non-empty) items cannot be on different rows (ignoring
            // the initial row difference)
            final int rowOffset = (givenFirstNonnull - recipeFirstNonnull) / height;

            final Map<Integer, Integer> consumption = new HashMap<>();

            // Check the remaining slots
            for (int i = 0; i < numSlots - Math.max(recipeFirstNonnull, givenFirstNonnull); i++) {
                final int recipeIndex = recipeFirstNonnull + i;
                final int givenIndex = givenFirstNonnull + i;
                final RecipeComponent component = components.get(recipeIndex);
                final ItemStack item = givenItems[givenIndex];
                if (!component.matches(item)) {
                    return RecipeMatchResult.NO_MATCH;
                } else if (!component.isAir()) {
                    // Different relative rows
                    if (recipeIndex / height + rowOffset != givenIndex / height) {
                        return RecipeMatchResult.NO_MATCH;
                    }
                    consumption.put(givenIndex, component.getAmount());
                }
            }

            return RecipeMatchResult.match(consumption);
        }
    };

    public static final RecipeStructure SHAPELESS = new RecipeStructure() {
        @Override
        public RecipeMatchResult match(ItemStack[] givenItems, List<RecipeComponent> components) {
            if (Arrays.stream(givenItems)
                    .filter(item -> item != null && !item.getType().isAir())
                    .count() != components.size()) {
                return RecipeMatchResult.NO_MATCH;
            }

            return SUBSET.match(givenItems, components);
        }
    };

    public static final RecipeStructure SUBSET = new RecipeStructure() {
        @Override
        public RecipeMatchResult match(ItemStack[] givenItems, List<RecipeComponent> components) {
            if (givenItems.length < components.size()) {
                return RecipeMatchResult.NO_MATCH;
            }

            // This may give a false negative on certain recpies, such as 
            // [b, a, c] against [(a|b|c), b, c] which could be solved by
            // using a bipartite matching algorithm, however that would
            // most likely be too slow, so it will be up to the recipe
            // creator to not make bad recipes such as the example above.
            final Map<Integer, Integer> consumption = new HashMap<>();
            for (final RecipeComponent component : components) {
                boolean matched = false;
                for (int i = 0; i < givenItems.length; i++) {
                    if (!consumption.containsKey(i) && component.matches(givenItems[i])) {
                        consumption.put(i, component.getAmount());
                        matched = true;
                    }
                }
                if (!matched) {
                    return RecipeMatchResult.NO_MATCH;
                }
            }

            return RecipeMatchResult.match(consumption);
        }
    };

    @Nonnull
    @ParametersAreNonnullByDefault
    public abstract RecipeMatchResult match(ItemStack[] givenItems, List<RecipeComponent> components);
}
