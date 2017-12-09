package me.mrCookieSlime.Slimefun.Objects.SlimefunItem;

import me.mrCookieSlime.Slimefun.Lists.Categories;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;

import org.bukkit.inventory.ItemStack;

public class SlimefunBow extends SlimefunItem {

	public SlimefunBow(ItemStack item, String id, ItemStack[] recipe) {
		super(Categories.WEAPONS, item, id, RecipeType.MAGIC_WORKBENCH, recipe);
	}

}
