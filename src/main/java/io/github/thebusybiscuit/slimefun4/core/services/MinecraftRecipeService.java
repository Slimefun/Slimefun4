package io.github.thebusybiscuit.slimefun4.core.services;

import io.github.thebusybiscuit.cscorelib2.recipes.MinecraftRecipe;
import io.github.thebusybiscuit.cscorelib2.recipes.RecipeSnapshot;
import io.github.thebusybiscuit.slimefun4.implementation.guide.ChestSlimefunGuide;
import org.bukkit.inventory.*;
import org.bukkit.plugin.Plugin;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * This Service is responsible for accessing a {@link RecipeSnapshot}.
 * This snapshot contains a compiled list of all recipes that could be found on the
 * Server at the time the Service was loaded.
 *
 * This Service is primarily used by the {@link ChestSlimefunGuide}.
 *
 * @author TheBusyBiscuit
 *
 */
public class MinecraftRecipeService {

    private final Plugin plugin;
    private RecipeSnapshot snapshot;

    public MinecraftRecipeService(Plugin plugin) {
        this.plugin = plugin;
    }

    /**
     * This method refreshes the {@link RecipeSnapshot} that is used by the {@link MinecraftRecipeService}.
     */
    public void refresh() {
        snapshot = new RecipeSnapshot(plugin);
    }

    /**
     * This method returns an {@link Optional} describing the output of a {@link FurnaceRecipe}
     * with the given {@link ItemStack} as an input.
     *
     * @param input The input {@link ItemStack}
     * @return An {@link Optional} describing the furnace output of the given {@link ItemStack}
     */
    public Optional<ItemStack> getFurnaceOutput(ItemStack input) {
        return snapshot.getRecipeOutput(MinecraftRecipe.FURNACE, input);
    }

    public RecipeChoice[] getRecipeShape(Recipe recipe) {
        if (recipe instanceof ShapedRecipe) {
            List<RecipeChoice> choices = new LinkedList<>();

            for (String row : ((ShapedRecipe) recipe).getShape()) {
                int columns = row.toCharArray().length;

                for (char key : row.toCharArray()) {
                    choices.add(((ShapedRecipe) recipe).getChoiceMap().get(key));
                }

                while (columns < 3) {
                    choices.add(null);
                    columns++;
                }
            }

            return choices.toArray(new RecipeChoice[0]);
        } else {
            return snapshot.getRecipeInput(recipe);
        }
    }

    /**
     * This returns an array containing all {@link Recipe Recipes} for crafting the given
     * {@link ItemStack}.
     *
     * @param item The {@link ItemStack} for which to get the recipes
     * @return An array of {@link Recipe Recipes} to craft the given {@link ItemStack}
     */
    public Recipe[] getRecipesFor(ItemStack item) {
        if (item == null) {
            return new Recipe[0];
        } else {
            return snapshot.getRecipesFor(item).toArray(new Recipe[0]);
        }
    }

}