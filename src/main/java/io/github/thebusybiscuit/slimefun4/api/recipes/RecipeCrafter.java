package io.github.thebusybiscuit.slimefun4.api.recipes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;

import javax.annotation.Nonnull;

import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.recipes.components.RecipeComponent;
import io.github.thebusybiscuit.slimefun4.api.recipes.output.RecipeOutput;
import io.github.thebusybiscuit.slimefun4.core.services.SlimefunRecipeService.CachingStrategy;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;

/**
 * Workstations and Machines should implement this interface if
 * they craft items. Simply provide the category(ies) of items they
 * craft, and getting/searching recipes is provided for free.
 * 
 * @author SchnTgaiSpock
 */
@FunctionalInterface
public interface RecipeCrafter {

    /**
     * @return The {@link RecipeCategory}s that this crafter can craft
     */
    public Collection<RecipeCategory> getCraftedCategories();

    /**
     * @return All recipes in the categories that this crafter can craft
     */
    public default Collection<Recipe> getRecipes() {
        List<Recipe> recipes = new ArrayList<>();

        for (final RecipeCategory category : getCraftedCategories()) {
            recipes.addAll(Slimefun.getSlimefunRecipeService().getRecipes(category));
        }

        return recipes;
    }

    /**
     * Searches the recipes that this crafter can craft
     * 
     * @param givenItems The items to craft
     * @return (The recipe if found, The result of the match)
     */
    public default RecipeSearchResult searchRecipes(@Nonnull ItemStack[] givenItems) {
        for (RecipeCategory category : getCraftedCategories()) {
            RecipeSearchResult result = Slimefun.searchRecipes(
                    category, givenItems, CachingStrategy.IF_MULTIPLE_CRAFTABLE);

            if (result.isMatch()) {
                return result;
            }
        }

        return RecipeSearchResult.NO_MATCH;
    }

    /**
     * Searches the recipes that this crafter can craft
     * 
     * @param givenItems    The items to craft
     * @param onRecipeFound To be called when a matching recipe is found
     * @return (The recipe if found, The result of the match)
     */
    public default RecipeSearchResult searchRecipes(
            @Nonnull ItemStack[] givenItems,
            BiConsumer<Recipe, RecipeMatchResult> onRecipeFound) {
        for (RecipeCategory category : getCraftedCategories()) {
            RecipeSearchResult result = Slimefun.searchRecipes(
                    category, givenItems, CachingStrategy.IF_MULTIPLE_CRAFTABLE, onRecipeFound);

            if (result.isMatch()) {
                return result;
            }
        }

        return RecipeSearchResult.NO_MATCH;
    }

    /**
     * Searches the recipes that this crafter can craft
     * 
     * @param givenItems    The items to craft
     * @param onRecipeFound To be called when a matching recipe is found. If it
     *                      returns true, consumes the input items according to
     *                      the recipe
     * @return (The recipe if found, The result of the match)
     */
    public default RecipeSearchResult searchRecipes(
            @Nonnull ItemStack[] givenItems,
            BiPredicate<Recipe, RecipeMatchResult> onRecipeFound) {
        for (RecipeCategory category : getCraftedCategories()) {
            RecipeSearchResult result = Slimefun.searchRecipes(
                    category, givenItems, CachingStrategy.IF_MULTIPLE_CRAFTABLE, onRecipeFound);

            if (result.isMatch()) {
                return result;
            }
        }

        return RecipeSearchResult.NO_MATCH;
    }

    /**
     * @return All recipes that only have 1 input item
     */
    public default Map<RecipeComponent, RecipeOutput> getSingleInputRecipes() {
        Map<RecipeComponent, RecipeOutput> singleInputRecipes = new HashMap<>();
        
        recipeLoop: for (Recipe recipe : getRecipes()) {
            int nonEmptyComponents = 0;
            RecipeComponent nonEmptyComponent = RecipeComponent.AIR;

            for (RecipeComponent component : recipe.getInputs().getComponents()) {
                if (!component.isAir()) {
                    nonEmptyComponents++;
                    if (nonEmptyComponents > 1) {
                        continue recipeLoop;
                    }
                    nonEmptyComponent = component;
                }
            }

            if (nonEmptyComponents > 0) {
                singleInputRecipes.put(nonEmptyComponent, recipe.getOutput());
            }
        }

        return singleInputRecipes;
    }

}
