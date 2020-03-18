package io.github.thebusybiscuit.slimefun4.core.services;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

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

    public MinecraftRecipeService(Plugin plugin) {
        this.plugin = plugin;
    }

    public void load() {
        snapshot = new RecipeSnapshot(plugin);
    }

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
        }
        else {
            return snapshot.getRecipeInput(recipe);
        }
    }

    public Recipe[] getRecipesFor(ItemStack item) {
        return snapshot.getRecipesFor(item).toArray(new Recipe[0]);
    }

}
