package io.github.thebusybiscuit.slimefun4.api.recipes.output;

import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;

public class ItemOutput implements RecipeOutput {

    private final ItemStack output;
    private final boolean disabled;

    public ItemOutput(ItemStack output) {
        this.output = output;
        if (output == null || output.getType().isAir()) {
            disabled = true;
        } else if (output instanceof final SlimefunItemStack sfStack) {
            final SlimefunItem sfItem = SlimefunItem.getById(sfStack.getItemId());
            if (sfItem != null && sfItem.isDisabled()) {
                disabled = true;
            } else {
                disabled = false;
            }
        } else {
            disabled = false;
        }
    }

    @Override
    public ItemStack generateOutput() {
        return output.clone();
    }

    @Override
    public boolean isDisabled() {
        return disabled;
    }

}
