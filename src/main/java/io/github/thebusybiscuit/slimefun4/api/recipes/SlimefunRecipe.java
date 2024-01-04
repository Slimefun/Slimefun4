package io.github.thebusybiscuit.slimefun4.api.recipes;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import io.github.thebusybiscuit.slimefun4.api.recipes.input.RecipeInputs;
import io.github.thebusybiscuit.slimefun4.api.recipes.output.RecipeOutput;

public class SlimefunRecipe implements Recipe {

    final RecipeInputs inputs;
    final RecipeOutput outputs;

    @ParametersAreNonnullByDefault
    public SlimefunRecipe(RecipeInputs inputs, RecipeOutput outputs) {
        this.inputs = inputs;
        this.outputs = outputs;
    }

    @Nonnull
    public RecipeInputs getInputs() {
        return inputs;
    }

    @Nonnull
    public RecipeOutput getOutputs() {
        return outputs;
    }

}
