package io.github.thebusybiscuit.slimefun4.implementation.items.tools;

import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import org.bukkit.inventory.ItemStack;

public class RestoredBackpack extends SlimefunBackpack {

    public RestoredBackpack(Category category, SlimefunItemStack item) {
        super(54, category, item, RecipeType.NULL, new ItemStack[9]);
        this.hidden = true;
    }
}
