package io.github.thebusybiscuit.slimefun4.implementation.items.armor;

import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

public class Parachute extends SlimefunItem {

    public Parachute(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, RecipeType.ENHANCED_CRAFTING_TABLE, recipe);
    }

}
