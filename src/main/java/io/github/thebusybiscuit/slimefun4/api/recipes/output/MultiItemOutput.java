package io.github.thebusybiscuit.slimefun4.api.recipes.output;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.inventory.ItemStack;

import com.google.common.base.Preconditions;

public class MultiItemOutput implements RecipeOutput {

    private final List<RecipeOutput> outputs = new ArrayList<>();
    private final boolean disabled;

    public MultiItemOutput(RecipeOutput... outputs) {
        Preconditions.checkArgument(outputs.length > 0, "The 'items' array must be non-empty");

        for (final RecipeOutput output : outputs) {
            if (output.isDisabled()) {
                continue;
            }

            this.outputs.add(output);
        }

        this.disabled = this.outputs.size() == 0;
    }

    @Override
    public ItemStack generateOutput() {
        return outputs.get(0).generateOutput();
    }

    @Override
    public ItemStack generateOutput(ItemStack[] givenItems) {
        return outputs.get(0).generateOutput(givenItems);
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
    public List<ItemStack> generateOutputs(ItemStack[] givenItems) {
        final List<ItemStack> outputs = new ArrayList<>();
        for (final RecipeOutput output : this.outputs) {
            outputs.addAll(output.generateOutputs(givenItems));
        }
        return outputs;
    }

    @Override
    public boolean isDisabled() {
        return disabled;
    }

}
