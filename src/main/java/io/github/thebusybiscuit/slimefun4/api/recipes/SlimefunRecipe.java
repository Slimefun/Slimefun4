package io.github.thebusybiscuit.slimefun4.api.recipes;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import io.github.thebusybiscuit.slimefun4.api.recipes.input.RecipeInputs;
import io.github.thebusybiscuit.slimefun4.api.recipes.output.RecipeOutput;

public class SlimefunRecipe implements Recipe {

    private RecipeInputs inputs;
    private RecipeOutput outputs;

    @ParametersAreNonnullByDefault
    public SlimefunRecipe(RecipeInputs inputs, RecipeOutput outputs) {
        this.inputs = inputs;
        this.outputs = outputs;
    }

    @Override
    public void setInputs(RecipeInputs inputs) {
        this.inputs = inputs;
    }

    @Override
    public void setOutputs(RecipeOutput outputs) {
        this.outputs = outputs;
    }

    @Override
    @Nonnull
    public RecipeInputs getInputs() {
        return inputs;
    }

    @Override
    @Nonnull
    public RecipeOutput getOutput() {
        return outputs;
    }

    @Override
    public String toString() {
        return "{Input=" + inputs + ", Output=" + outputs + "}";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        Recipe other = (SlimefunRecipe) obj;
        return other.getInputs().equals(getInputs()) && other.getOutput().equals(getOutput());
    }

    @Override
    public int hashCode() {
        return getInputs().hashCode() * 31 + getOutput().hashCode();
    }

    public TimedRecipe asTimedRecipe(int ticks) {
        return new TimedRecipe(ticks, inputs, outputs);
    }

}
