package io.github.thebusybiscuit.slimefun4.api.recipes;

import java.util.Collection;
import java.util.List;

import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.recipes.matching.RecipeSearchResult;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;

public interface RecipeCrafter {

    public Collection<RecipeType> getCraftableRecipeTypes();
    public default RecipeSearchResult searchRecipes(List<ItemStack> givenItems) {
        return Slimefun.getRecipeService().searchRecipes(getCraftableRecipeTypes(), givenItems);
    }
    
}
