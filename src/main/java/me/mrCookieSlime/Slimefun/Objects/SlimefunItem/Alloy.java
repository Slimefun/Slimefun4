package me.mrCookieSlime.Slimefun.Objects.SlimefunItem;

import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

/**
 * 
 * @deprecated Moved to {@code io.github.thebusybiscuit.slimefun4.implementation.items.Alloy}
 *
 */
@Deprecated
public class Alloy extends io.github.thebusybiscuit.slimefun4.implementation.items.Alloy {

    public Alloy(SlimefunItemStack item, ItemStack[] recipe) {
        super(item, recipe);
    }

    public Alloy(Category category, SlimefunItemStack item, ItemStack[] recipe) {
        super(category, item, recipe);
    }

}
