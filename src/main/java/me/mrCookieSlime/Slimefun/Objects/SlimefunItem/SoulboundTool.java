package me.mrCookieSlime.Slimefun.Objects.SlimefunItem;

import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.interfaces.NotPlaceable;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

public class SoulboundTool extends SoulboundItem implements NotPlaceable {

	public SoulboundTool(Category category, ItemStack item, String id, RecipeType type, ItemStack[] recipe) {
		super(category, item, id, type, recipe);
	}
	
	public SoulboundTool(Category category, SlimefunItemStack item, ItemStack[] recipe) {
		super(category, item, recipe);
	}

}
