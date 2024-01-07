package io.github.thebusybiscuit.slimefun4.api.recipes;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.recipes.input.RecipeInputs;
import io.github.thebusybiscuit.slimefun4.api.recipes.output.ItemOutput;
import io.github.thebusybiscuit.slimefun4.api.recipes.output.RecipeOutput;

/**
 * A simple interface that associates a set of input items to an
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
    @Nonnull
    @ParametersAreNonnullByDefault
    public static Recipe of(RecipeStructure structure, ItemStack[] inputs, ItemStack output) {
        return new SlimefunRecipe(RecipeInputs.of(structure, inputs), new ItemOutput(output));
    }

    /**
     * Sets the inputs of this recipe
     * @param inputs The new inputs
     */
    public void setInputs(RecipeInputs inputs);

    /**
     * Sets the output of this recipe
     * @param output The new output
     */
    public void setOutputs(RecipeOutput output);

    /**
     * Sets the structure of this recipe
     * @param structure The new structure
     */
    public default void setStructure(RecipeStructure structure) {
        getInputs().setStructure(structure);
    }

    /**
     * @return The inputs of this recipe
     */
    public @Nonnull RecipeInputs getInputs();
    
    /**
     * @return The outputs of this recipe
     */
    public @Nonnull RecipeOutput getOutput();

    /**
     * @return The structure of this recipe
     */
    public default @Nonnull RecipeStructure getStructure() {
        return getInputs().getStructure();
    }

    /**
     * If this recipe is disabled. Disabled recipes cannot be registered
     * @return
     */
    public default boolean isDisabled() {
        return getInputs().isDisabled() || getOutput().isDisabled();
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
