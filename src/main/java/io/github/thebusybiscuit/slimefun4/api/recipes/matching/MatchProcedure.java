package io.github.thebusybiscuit.slimefun4.api.recipes.matching;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.recipes.AbstractRecipeInput;
import io.github.thebusybiscuit.slimefun4.api.recipes.items.AbstractRecipeInputItem;
import io.github.thebusybiscuit.slimefun4.utils.RecipeUtils;
import io.github.thebusybiscuit.slimefun4.utils.RecipeUtils.BoundingBox;

public abstract class MatchProcedure {

    /**
     * For Recipes used as display/dummy purposes. Will never match anything
     */
    public static final MatchProcedure DUMMY = new MatchProcedure("slimefun:dummy") {
        @Override
        public InputMatchResult match(AbstractRecipeInput recipeInput, List<ItemStack> givenItems) {
            return InputMatchResult.noMatch(recipeInput);
        }
    };

    /**
     * For Recipes with empty inputs and whose crafters don't take input items
     */
    public static final MatchProcedure EMPTY = new MatchProcedure("slimefun:empty") {
        @Override
        public InputMatchResult match(AbstractRecipeInput recipeInput, List<ItemStack> givenItems) {
            return new InputMatchResult(recipeInput, Collections.emptyList(), true);
        }
    };

    public static final MatchProcedure SHAPED = new MatchProcedure("slimefun:shaped") {
        @Override
        public InputMatchResult match(AbstractRecipeInput recipeInput, List<ItemStack> givenItems) {
            final int width = recipeInput.getWidth();
            final int height = recipeInput.getHeight();

            Optional<BoundingBox> oRecipeBox = recipeInput.getBoundingBox();
            if (oRecipeBox.isEmpty()) {
                return InputMatchResult.noMatch(recipeInput);
            }
            BoundingBox recipeBox = oRecipeBox.get();
            BoundingBox givenBox = RecipeUtils.calculateBoundingBox(givenItems, width, height, i -> i == null || i.getType().isAir());

            if (!recipeBox.isSameShape(givenBox)) {
                return InputMatchResult.noMatch(recipeInput);
            }

            List<ItemMatchResult> results = new ArrayList<>();
            for (int i = 0; i < recipeBox.getWidth() * recipeBox.getHeight(); i++) {
                int recipeX = recipeBox.left + i % recipeBox.getWidth();
                int recipeY = recipeBox.top + i / recipeBox.getWidth();
                int givenX = givenBox.left + i % givenBox.getWidth();
                int givenY = givenBox.top + i / givenBox.getWidth();
                AbstractRecipeInputItem recipeItem = recipeInput.getItem(recipeY * width + recipeX);
                ItemStack givenItem = givenItems.get(givenY * width + givenX);
                ItemMatchResult matchResult = recipeItem.matchItem(givenItem);
                results.add(matchResult);
                if (!matchResult.itemsMatch()) {
                    return new InputMatchResult(recipeInput, results, false);
                }
            }
            return new InputMatchResult(recipeInput, results, true);
        }
    };

    public static final MatchProcedure SHAPED_FLIPPABLE = new MatchProcedure("slimefun:shaped_flippable") {
        @Override
        public InputMatchResult match(AbstractRecipeInput recipeInput, List<ItemStack> givenItems) {
            InputMatchResult result = SHAPED.match(recipeInput, givenItems);
            if (result.itemsMatch()) {
                return result;
            }
            // Flip given items
            List<ItemStack> flipped = RecipeUtils.flipY(givenItems, recipeInput.getWidth(), recipeInput.getHeight());
            return SHAPED.match(recipeInput, flipped);
        }
    };

    public static final MatchProcedure SHAPED_ROTATABLE_45_3X3 = new MatchProcedure("slimefun:shaped_rotatable_3x3") {
        @Override
        public InputMatchResult match(AbstractRecipeInput recipeInput, List<ItemStack> givenItems) {
            InputMatchResult result = null;
            for (int i = 0; i < 8; i++) {
                result = SHAPED.match(recipeInput, givenItems);
                if (result.itemsMatch() || i == 7) {
                    return result;
                }
                givenItems = RecipeUtils.rotate45deg3x3(givenItems);
            }
            return result;
        }
    };

    public static final MatchProcedure SUBSET = new MatchProcedure("slimefun:subset") {
        @Override
        public InputMatchResult match(AbstractRecipeInput recipeInput, List<ItemStack> givenItems) {
            if (
                recipeInput.getItems().stream().filter(i -> !i.isEmpty()).count() >
                givenItems.stream().filter(i -> i != null && !i.getType().isAir()).count()
            ) {
                return InputMatchResult.noMatch(recipeInput);
            }
            Map<Integer, ItemMatchResult> matchedItems = new HashMap<>();
            outer: for (AbstractRecipeInputItem recipeItem : recipeInput.getItems()) {
                if (recipeItem.isEmpty()) {
                    continue;
                }
                for (int i = 0; i < givenItems.size(); i++) {
                    if (matchedItems.containsKey(i)) {
                        continue;
                    }
                    ItemMatchResult result = recipeItem.matchItem(givenItems.get(i));
                    if (result.itemsMatch()) {
                        matchedItems.put(i, result);
                        continue outer;
                    }
                }

                return new InputMatchResult(recipeInput, List.copyOf(matchedItems.values()), false);
            }

            return new InputMatchResult(recipeInput, List.copyOf(matchedItems.values()), true);
        }
    };

    public static final MatchProcedure SHAPELESS = new MatchProcedure("slimefun:shapeless") {
        @Override
        public InputMatchResult match(AbstractRecipeInput recipeInput, List<ItemStack> givenItems) {
            if (
                recipeInput.getItems().stream().filter(i -> !i.isEmpty()).count() !=
                givenItems.stream().filter(i -> i != null && !i.getType().isAir()).count()
                ) {
                return InputMatchResult.noMatch(recipeInput);
            }
            return SUBSET.match(recipeInput, givenItems);
        }
    };

    private final NamespacedKey key;

    public MatchProcedure(String key) {
        this.key = NamespacedKey.fromString(key);
    }

    public NamespacedKey getKey() {
        return key;
    }

    public abstract InputMatchResult match(AbstractRecipeInput recipeInput, List<ItemStack> givenItems);
    public InputMatchResult match(AbstractRecipeInput recipeInput, ItemStack[] givenItems) {
        return match(recipeInput, List.of(givenItems));
    }

    public boolean recipeShouldSaveBoundingBox() {
        return true;
    }

    @Override
    public String toString() {
        return key.toString();
    }
    
}
