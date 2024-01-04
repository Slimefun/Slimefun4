package io.github.thebusybiscuit.slimefun4.api.recipes;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.recipes.input.RecipeInputs;
import io.github.thebusybiscuit.slimefun4.api.recipes.output.ItemOutput;
import io.github.thebusybiscuit.slimefun4.api.recipes.output.RecipeOutput;

/**
 * A simple class that associates a set of input items to an
 * output item(s). Contains methods to match its input items
 * to another set of items
 * 
 * @author SchnTgaiSpock
 */
public interface Recipe {

    public static Recipe EMPTY = new SlimefunRecipe(RecipeInputs.EMPTY, RecipeOutput.EMPTY);

    /**
     * Construct a simple recipe
     * 
     * @param structure The {@link RecipeStructure} of the recipe
     * @param inputs The inputs of the recipe
     * @param output The output of the recipe
     * @return The constructed Recipe
     */
    public static Recipe of(RecipeStructure structure, ItemStack[] inputs, ItemStack output) {
        return new SlimefunRecipe(RecipeInputs.of(structure, inputs), new ItemOutput(output));
    }

    public @Nonnull RecipeInputs getInputs();
    public @Nonnull RecipeOutput getOutputs();

    public default @Nonnull RecipeStructure getStructure() {
        return getInputs().getStructure();
    }

    public default boolean isDisabled() {
        return getInputs().isDisabled() || getOutputs().isDisabled();
    }

    /**
     * Matches the givenItems against this recipe's inputs
     * 
     * @param givenItems The items to match
     * @return The result of the match. See {@link RecipeMatchResult}
     */
    public default @Nonnull RecipeMatchResult match(@Nonnull ItemStack[] givenItems) {
        return getInputs().match(getStructure(), givenItems);
    }
    
    /**
     * Matches the givenItems against this recipe's inputs
     * using some other structure.
     * 
     * @param otherStructure The alternate structure
     * @param givenItems The items to match
     * @return The result of the match. See {@link RecipeMatchResult}
     */
    @ParametersAreNonnullByDefault
    public default @Nonnull RecipeMatchResult match(RecipeStructure otherStructure, ItemStack[] givenItems) {
        return getInputs().match(otherStructure, givenItems);
    }

}
