package io.github.thebusybiscuit.slimefun4.core.services;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.Plugin;

import io.github.thebusybiscuit.cscorelib2.recipes.MinecraftRecipe;
import io.github.thebusybiscuit.cscorelib2.recipes.RecipeSnapshot;
import io.github.thebusybiscuit.slimefun4.implementation.guide.ChestSlimefunGuide;

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

    /**
     * This constructs a new {@link MinecraftRecipeService} for the given {@link Plugin}.
     * Slimefun already has a {@link MinecraftRecipeService} so creating your own won't be
     * of much use unless you wanna expand upon it. It is advised to use Slimefun's built-in
     * {@link MinecraftRecipeService} though.
     * 
     * @param plugin
     *            The {@link Plugin} that requests this Service
     */
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
     * @param input
     *            The input {@link ItemStack}
     * 
     * @return An {@link Optional} describing the furnace output of the given {@link ItemStack}
     */
    public Optional<ItemStack> getFurnaceOutput(ItemStack input) {
        return snapshot.getRecipeOutput(MinecraftRecipe.FURNACE, input);
    }

    /**
     * This returns the shape of a given {@link Recipe}.
     * For any shapeless {@link Recipe} the result will be equivalent to
     * {@link RecipeSnapshot#getRecipeInput(Recipe)}.
     * For a {@link ShapedRecipe} this method will fix the order so it matches a
     * 3x3 crafting grid.
     * 
     * @param recipe
     *            The {@link Recipe} to get the shape from
     * @return An Array of {@link RecipeChoice} representing the shape of this {@link Recipe}
     */
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
        }
        else {
            return snapshot.getRecipeInput(recipe);
        }
    }

    /**
     * This returns an array containing all {@link Recipe Recipes} for crafting the given
     * {@link ItemStack}.
     * 
     * @param item
     *            The {@link ItemStack} for which to get the recipes
     * @return An array of {@link Recipe Recipes} to craft the given {@link ItemStack}
     */
    public Recipe[] getRecipesFor(ItemStack item) {
        if (item == null) {
            return new Recipe[0];
        }
        else {
            return snapshot.getRecipesFor(item).toArray(new Recipe[0]);
        }
    }

}
