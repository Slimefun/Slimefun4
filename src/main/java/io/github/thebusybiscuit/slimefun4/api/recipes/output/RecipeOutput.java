package io.github.thebusybiscuit.slimefun4.api.recipes.output;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.recipes.Recipe;

public interface RecipeOutput {

    public static final RecipeOutput EMPTY = new RecipeOutput() {

        @Override
        public ItemStack generateOutput() {
            return new ItemStack(Material.AIR);
        }

        @Override
        public boolean isDisabled() {
            return false;
        }

        @Override
        public List<String> getSlimefunItemIDs() {
            return Collections.emptyList();
        }

        @Override
        public ItemStack asDisplayItem() {
            return new ItemStack(Material.AIR);
        }
        
    };

    @Nonnull
    @ParametersAreNonnullByDefault
    public static RecipeOutput of(ItemStack... outputs) {
        RecipeOutput[] recipeOutputs = Arrays.stream(outputs)
            .map(item -> item == null ? ItemOutput.EMPTY : new ItemOutput(item)).toArray(RecipeOutput[]::new);
        return recipeOutputs.length == 1
            ? recipeOutputs[0]
            : new MultiItemOutput(recipeOutputs);
    }

    /**
     * To be called when the Workstation/Machine needs to get the output of a
     * recipe
     * 
     * @return The output of a recipe
     */
    public @Nonnull ItemStack generateOutput();

    /**
     * Override this method if a recipe has multiple outputs at once
     * 
     * @return The outputs of the recipe
     */
    public default @Nonnull List<ItemStack> generateOutputs() {
        return List.of(generateOutput());
    }

    /**
     * Override this method if a recipe's output depends on its input.
     * 
     * This method should only be called if you are sure the given items
     * will craft this output
     * 
     * @param givenItems The input items
     * @return The output of the recipe
     */
    public default @Nonnull ItemStack generateOutput(ItemStack[] givenItems, Recipe recipe) {
        return generateOutput();
    }

    /**
     * Override this method if a recipe's output depends on its input.
     * 
     * This method should only be called if you are sure the given items
     * will craft this output
     * 
     * @param givenItems The input items
     * @return The outputs of the recipe
     */
    public default @Nonnull List<ItemStack> generateOutputs(ItemStack[] givenItems, Recipe recipe) {
        return generateOutputs();
    }

    /**
     * @return The item to be displayed in the guide
     */
    public @Nonnull ItemStack asDisplayItem();

    /**
     * @return The item to be displayed in the guide.
     * @param slimefunID The ID of the slimefun item currently being viewed in the guide.
     */
    public default @Nonnull ItemStack asDisplayItem(String slimefunID) {
        return asDisplayItem();
    }

    /**
     * If the output(s) are disabled and so cannot be crafted
     * @return
     */
    public boolean isDisabled();

    public List<String> getSlimefunItemIDs();

}
