package io.github.thebusybiscuit.slimefun4.testing.mocks;

import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;

public class MockSlimefunItem extends SlimefunItem {

    public MockSlimefunItem(Category category, ItemStack item, String id) {
        super(category, item, id, RecipeType.NULL, new ItemStack[9]);
    }

}
