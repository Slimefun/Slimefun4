package me.mrCookieSlime.Slimefun.Objects.SlimefunItem;

import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;

import org.bukkit.inventory.ItemStack;

public class ExcludedSoulboundTool extends SoulboundItem{

	public ExcludedSoulboundTool(Category category, ItemStack item, String name, RecipeType type, ItemStack[] recipe) {
		super(category, item, name, type, recipe);
	}
	
	public ExcludedSoulboundTool(Category category, ItemStack item, String name, ItemStack[] recipe) {
		super(category, item, name, recipe);
	}

}
