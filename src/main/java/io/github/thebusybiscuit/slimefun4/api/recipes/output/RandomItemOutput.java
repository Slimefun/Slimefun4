package io.github.thebusybiscuit.slimefun4.api.recipes.output;

import org.bukkit.inventory.ItemStack;

import io.github.bakedlibs.dough.collections.RandomizedSet;

public class RandomItemOutput implements RecipeOutput {

    private final RandomizedSet<ItemStack> outputSet;

    public RandomItemOutput(RandomizedSet<ItemStack> outputSet) {
        this.outputSet = outputSet;
    }

    @Override
    public ItemStack generateOutput() {
        return outputSet.getRandom().clone();
    }

    @Override
    public boolean isDisabled() {
        // TODO check each element, same as ItemOutput
        return false;
    }
    
}
