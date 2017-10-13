package me.mrCookieSlime.Slimefun.Objects.SlimefunItem;

import me.mrCookieSlime.Slimefun.Lists.Categories;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;

import org.bukkit.inventory.ItemStack;

public class ReplacingAlloy extends ReplacingItem {

	public ReplacingAlloy(ItemStack item, String id, ItemStack[] recipe) {
		super(Categories.RESOURCES, item, id, RecipeType.SMELTERY, recipe);
	}
	
	public ReplacingAlloy(Category category, ItemStack item, String id, ItemStack[] recipe) {
		super(category, item, id, RecipeType.SMELTERY, recipe);
	}

}
