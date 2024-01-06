package io.github.thebusybiscuit.slimefun4.api.recipes.components;

import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;

/**
 * A {@code DistinctiveComponent} only checks the id of the slimefun item
 */
public class DistinctiveComponent extends ItemComponent {

    public DistinctiveComponent(ItemStack slimefunItem) {
        super(slimefunItem);
    }

    @Override
    public boolean matches(ItemStack givenItem) {
        return SlimefunUtils.isItemSimilar(givenItem, getComponent(), false, true, false);
    }
    
}
