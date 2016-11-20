package me.mrCookieSlime.Slimefun.Objects.SlimefunItem;

import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;

import org.bukkit.inventory.ItemStack;

public class ReplacingItem extends SlimefunItem {

	public ReplacingItem(Category category, ItemStack item, String name, RecipeType recipeType, ItemStack[] recipe) {
		super(category, item, name, recipeType, recipe);
		
		this.replacing = true;
	}

}
