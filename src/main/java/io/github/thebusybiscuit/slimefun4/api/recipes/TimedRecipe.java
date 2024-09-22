package io.github.thebusybiscuit.slimefun4.api.recipes;

import io.github.thebusybiscuit.slimefun4.api.recipes.input.RecipeInputs;
import io.github.thebusybiscuit.slimefun4.api.recipes.output.RecipeOutput;

public class TimedRecipe extends SlimefunRecipe {

    private final int ticks;

    public TimedRecipe(int ticks, RecipeInputs inputs, RecipeOutput outputs) {
        super(inputs, outputs);
        
        this.ticks = ticks;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(input(s) = " + getInputs() + ", output(s) = " + getOutput() + ", ticks = " + ticks + ")";
    }

    public int getTicks() {
        return ticks;
    }

}
