package me.mrCookieSlime.Slimefun.Objects.SlimefunItem;

import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;

import org.bukkit.inventory.ItemStack;

public class SoulboundBackpack extends SlimefunBackpack {

	public SoulboundBackpack(int size, Category category, ItemStack item, String name, ItemStack[] recipe) {
		super(size, category, item, name, RecipeType.MAGIC_WORKBENCH, recipe);
	}
	public SoulboundBackpack(int size, Category category, ItemStack item, String name, RecipeType type, ItemStack[] recipe) {
		super(size, category, item, name, type, recipe);
	}

}
