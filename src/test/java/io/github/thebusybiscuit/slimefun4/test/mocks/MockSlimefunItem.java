package io.github.thebusybiscuit.slimefun4.test.mocks;

import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;

public class MockSlimefunItem extends SlimefunItem {

    public MockSlimefunItem(ItemGroup itemGroup, ItemStack item, String id) {
        super(itemGroup, new SlimefunItemStack(id, item), RecipeType.NULL, new ItemStack[9]);
    }

}
