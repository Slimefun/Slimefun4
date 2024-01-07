package io.github.thebusybiscuit.slimefun4.api.recipes.output;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.inventory.ItemStack;

import com.google.common.base.Preconditions;

import io.github.thebusybiscuit.slimefun4.api.recipes.Recipe;

public class MultiItemOutput implements RecipeOutput {

    private final List<RecipeOutput> outputs = new ArrayList<>();
    private final boolean disabled;
    private final List<String> slimefunIDs = new ArrayList<>();

    public MultiItemOutput(RecipeOutput... outputs) {
        Preconditions.checkArgument(outputs.length > 0, "The 'items' array must be non-empty");

        for (final RecipeOutput output : outputs) {
            if (output.isDisabled()) {
                continue;
            }

            this.outputs.add(output);
        }

        this.disabled = this.outputs.size() == 0;
        
        for (final RecipeOutput output : outputs) {
            slimefunIDs.addAll(output.getSlimefunItemIDs());
        }
    }

    public List<RecipeOutput> getOutputs() {
        return outputs;
    }

    @Override
    public ItemStack generateOutput() {
        return outputs.get(0).generateOutput();
    }

    @Override
    public ItemStack generateOutput(ItemStack[] givenItems, Recipe recipe) {
        return outputs.get(0).generateOutput(givenItems, recipe);
    }

    @Override
    public List<ItemStack> generateOutputs() {
        final List<ItemStack> outputs = new ArrayList<>();
        for (final RecipeOutput output : this.outputs) {
            outputs.addAll(output.generateOutputs());
        }
        return outputs;
    }

    @Override
    public List<ItemStack> generateOutputs(ItemStack[] givenItems, Recipe recipe) {
        final List<ItemStack> outputs = new ArrayList<>();
        for (final RecipeOutput output : this.outputs) {
            outputs.addAll(output.generateOutputs(givenItems, recipe));
        }
        return outputs;
    }

    @Override
    public boolean isDisabled() {
        return disabled;
    }

    @Override
    public String toString() {
        return outputs.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        return ((MultiItemOutput) obj).getOutputs().equals(outputs);
    }

    @Override
    public int hashCode() {
        return outputs.hashCode();
    }
    
    @Override
    public List<String> getSlimefunItemIDs() {
        return slimefunIDs;
    }

}
