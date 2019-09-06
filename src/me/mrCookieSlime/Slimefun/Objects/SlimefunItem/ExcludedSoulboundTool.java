package me.mrCookieSlime.Slimefun.Objects.SlimefunItem;

import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.interfaces.NotPlaceable;

public class ExcludedSoulboundTool extends SoulboundItem implements NotPlaceable {

	public ExcludedSoulboundTool(Category category, ItemStack item, String id, RecipeType type, ItemStack[] recipe) {
		super(category, item, id, type, recipe);
	}
	
	public ExcludedSoulboundTool(Category category, ItemStack item, String id, ItemStack[] recipe) {
		super(category, item, id, recipe);
	}

}
