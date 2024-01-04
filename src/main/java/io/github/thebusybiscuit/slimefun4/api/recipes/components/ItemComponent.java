package io.github.thebusybiscuit.slimefun4.api.recipes.components;

import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;

public class ItemComponent implements RecipeComponent {

    private final ItemStack component;
    private final boolean disabled;

    public ItemComponent(ItemStack component) {
        this.component = component;

        if (component instanceof final SlimefunItemStack sfStack) {
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
    public boolean isDisabled() {
        return disabled;
    }

    @Override
    public boolean isAir() {
        return component.getType().isAir();
    }

    @Override
    public int getAmount() {
        return component.getAmount();
    }

    @Override
    public boolean matches(ItemStack givenItem) {
        return SlimefunUtils.isItemSimilar(givenItem, component, true);
    }

    @Override
    public ItemStack getDisplayItem() {
        return component;
    }

}
