package me.mrCookieSlime.Slimefun.Objects.SlimefunItem;

import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;

public class SlimefunBackpack extends SlimefunItem {
	
	private int size;

	public SlimefunBackpack(int size, Category category, ItemStack item, String id, RecipeType recipeType, ItemStack[] recipe) {
		super(category, item, id, recipeType, recipe);
		
		this.size = size;
	}
	
	public int getSize() {
		return size;
	}

}
