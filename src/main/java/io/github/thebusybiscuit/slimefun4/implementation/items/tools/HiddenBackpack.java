package io.github.thebusybiscuit.slimefun4.implementation.items.tools;

import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import org.bukkit.inventory.ItemStack;

public class HiddenBackpack extends SlimefunBackpack {

    public HiddenBackpack(int size, Category category, SlimefunItemStack item) {
        super(size, category, item, RecipeType.NULL, new ItemStack[9]);
        this.hidden = true;
    }
}
