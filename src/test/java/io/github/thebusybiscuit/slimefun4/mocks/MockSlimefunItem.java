package io.github.thebusybiscuit.slimefun4.mocks;

import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;

class MockSlimefunItem extends SlimefunItem {

    public MockSlimefunItem(Category category, ItemStack item, String id) {
        super(category, item, id, null, new ItemStack[9]);
    }

}
