package io.github.thebusybiscuit.slimefun4.api.recipes.output;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import io.github.bakedlibs.dough.collections.RandomizedSet;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;

public class RandomItemOutput implements RecipeOutput {

    private final RandomizedSet<ItemStack> outputSet;
    private final List<String> slimefunIDs = new ArrayList<>();

    public RandomItemOutput(RandomizedSet<ItemStack> outputSet) {
        this.outputSet = outputSet;
        for (final ItemStack item : outputSet) {
            if (item instanceof final SlimefunItemStack sfStack) {
                slimefunIDs.add(sfStack.getItemId());
            }
        }
    }

    public RandomizedSet<ItemStack> getOutputSet() {
        return outputSet;
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

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" + outputSet.toString() + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        return ((RandomItemOutput) obj).getOutputSet().equals(outputSet);
    }

    @Override
    public int hashCode() {
        return outputSet.hashCode();
    }

    @Override
    public List<String> getSlimefunItemIDs() {
        return slimefunIDs;
    }

    @Override
    public List<ItemStack> getDisplayItems() {
        return outputSet.stream().toList();
    }

    @Override
    public ItemStack getDisplayItem(String slimefunID) {
        for (ItemStack item : outputSet) {
            SlimefunItem sfItem = SlimefunItem.getByItem(item);
            if (sfItem != null && sfItem.getId().equals(slimefunID)) {
                return item.clone();
            }
        }

        return new ItemStack(Material.AIR);
    }
    
}
