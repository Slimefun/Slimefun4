package io.github.thebusybiscuit.slimefun4.api.recipes;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.recipes.components.RecipeComponent;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;

public abstract class RecipeStructure implements Keyed {

    /**
     * <b>Note:</b> This structure assumes the recipe inputs and the given
     * inputs have the same dimension
     */
    public static final RecipeStructure IDENTICAL = new RecipeStructure("identical") {
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
    public static final RecipeStructure SHAPED = new RecipeStructure("shaped") {
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
                    break;
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
            for (int i = 0; i < numSlots - recipeFirstNonnull; i++) {
                final int recipeIndex = recipeFirstNonnull + i;
                final int givenIndex = givenFirstNonnull + i;
                final RecipeComponent component = components.get(recipeIndex);
                final ItemStack item = givenIndex < givenItems.length ? givenItems[givenIndex] : null;
                
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

    public static final RecipeStructure SHAPELESS = new RecipeStructure("shapeless") {
        @Override
        public RecipeMatchResult match(ItemStack[] givenItems, List<RecipeComponent> components) {
            if (countNonEmpty(givenItems) != countNonEmpty(components)) {
                return RecipeMatchResult.NO_MATCH;
            }

            return checkSubset(givenItems, components);
        }
    };

    public static final RecipeStructure SUBSET = new RecipeStructure("subset") {
        @Override
        public RecipeMatchResult match(ItemStack[] givenItems, List<RecipeComponent> components) {
            System.out.println("########## givenItems: " + Arrays.toString(givenItems));
            System.out.println("========== recipe: " + components);

            if (countNonEmpty(givenItems) < countNonEmpty(components)) {
                System.out.println("------------- too little");
                return RecipeMatchResult.NO_MATCH;
            }

            return checkSubset(givenItems, components);
        }
    };

    public static final RecipeStructure NULL = new RecipeStructure("null") {
        @Override
        public RecipeMatchResult match(ItemStack[] givenItems, List<RecipeComponent> components) {
            return RecipeMatchResult.NO_MATCH;
        }
    };

    /**
     * This may give a false negative on certain recpies, such as 
     * [b, a, c] against [(a|b|c), b, c] which could be solved by
     * using a bipartite matching algorithm, however that would
     * most likely be too slow, so it will be up to the recipe
     * creator to not make bad recipes such as the example above.
     * @param givenItems The items to match
     * @param components The recipe components
     * @return If the two match
     */
    protected static final RecipeMatchResult checkSubset(ItemStack[] givenItems, List<RecipeComponent> components) {
        final Map<Integer, Integer> consumption = new HashMap<>();
        System.out.println("Checking " + givenItems + " against " + components);
        componentLoop: for (final RecipeComponent component : components) {
            if (component.isAir()) {
                System.out.println(component + " is air, skipping");
                continue;
            }

            for (int i = 0; i < givenItems.length; i++) {
                if (!consumption.containsKey(i) && component.matches(givenItems[i])) {
                    System.out.println(component + " matches " + givenItems + ", continuing");
                    consumption.put(i, component.getAmount());
                    continue componentLoop;
                }
            }

            System.out.println("uh oh, no match");
            return RecipeMatchResult.NO_MATCH;
        }

        return RecipeMatchResult.match(consumption);
    }

    private static int countNonEmpty(ItemStack[] givenItems) {
        int nonEmpty = 0;
        for (final ItemStack itemStack : givenItems) {
            if (itemStack != null && !itemStack.getType().isAir()) {
                nonEmpty++;
            }
        }
        return nonEmpty;
    }

    private static int countNonEmpty(List<RecipeComponent> recipe) {
        int nonEmpty = 0;
        for (final RecipeComponent comp : recipe) {
            if (!comp.isAir()) {
                nonEmpty++;
            }
        }
        return nonEmpty;
    }

    private final NamespacedKey key;

    public RecipeStructure(NamespacedKey key) {
        this.key = key;
    }

    RecipeStructure(String name) {
        this(new NamespacedKey(Slimefun.instance(), name));
    }

    @Override
    public NamespacedKey getKey() {
        return key;
    }

    public String getTranslationKey() {
        return key.getNamespace() + "." + key.getKey();
    }

    @Nonnull
    @ParametersAreNonnullByDefault
    public abstract RecipeMatchResult match(ItemStack[] givenItems, List<RecipeComponent> components);

    @Override
    public String toString() {
        return key.toString();
    }

}
