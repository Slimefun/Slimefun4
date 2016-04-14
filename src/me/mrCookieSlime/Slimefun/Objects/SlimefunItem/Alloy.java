package me.mrCookieSlime.Slimefun.Objects.SlimefunItem;

import me.mrCookieSlime.Slimefun.Lists.Categories;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;

import org.bukkit.inventory.ItemStack;

public class Alloy extends SlimefunItem {

	public Alloy(ItemStack item, String name, ItemStack[] recipe) {
		super(Categories.RESOURCES, item, name, RecipeType.SMELTERY, recipe);
	}
	
	public Alloy(Category category, ItemStack item, String name, ItemStack[] recipe) {
		super(category, item, name, RecipeType.SMELTERY, recipe);
	}

}
