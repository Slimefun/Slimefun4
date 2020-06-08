package io.github.thebusybiscuit.slimefun4.implementation.items.misc;

import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

public class OrganicFertilizer extends SlimefunItem {

    public static final int OUTPUT = 2;

    public OrganicFertilizer(Category category, SlimefunItemStack item, SlimefunItemStack ingredient) {
        super(category, item, RecipeType.FOOD_COMPOSTER, new ItemStack[] { ingredient, null, null, null, null, null, null, null, null }, new SlimefunItemStack(item, OUTPUT));
    }

}
