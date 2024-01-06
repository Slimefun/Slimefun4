package io.github.thebusybiscuit.slimefun4.api.recipes.output;

import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

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
        
    };

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
    public default @Nonnull ItemStack generateOutput(ItemStack[] givenItems) {
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
    public default @Nonnull List<ItemStack> generateOutputs(ItemStack[] givenItems) {
        return generateOutputs();
    }

    /**
     * @return The output template for this RecipeOutput, if it exists 
     */
    public default @Nullable ItemStack getOutputTemplate() {
        return null;
    }

    /**
     * If the output(s) are disabled and so cannot be crafted
     * @return
     */
    public boolean isDisabled();

    public List<String> getSlimefunItemIDs();

}
