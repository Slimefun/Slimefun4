package io.github.thebusybiscuit.slimefun4.test.mocks;

import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

public class MockSlimefunItem extends SlimefunItem {

    public MockSlimefunItem(Category category, ItemStack item, String id) {
        super(category, new SlimefunItemStack(id, item), RecipeType.NULL, new ItemStack[9]);
    }

}
