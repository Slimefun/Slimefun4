package io.github.thebusybiscuit.slimefun4.implementation.items.misc;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

public class OrganicFood extends SlimefunItem {

    public static final int OUTPUT = 2;

    public OrganicFood(Category category, SlimefunItemStack item, Material ingredient) {
        super(category, item, RecipeType.FOOD_FABRICATOR, new ItemStack[] { SlimefunItems.TIN_CAN, new ItemStack(ingredient), null, null, null, null, null, null, null }, new SlimefunItemStack(item, OUTPUT));
    }

}
