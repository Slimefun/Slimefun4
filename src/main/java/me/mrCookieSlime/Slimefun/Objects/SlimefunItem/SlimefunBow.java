package me.mrCookieSlime.Slimefun.Objects.SlimefunItem;

import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.Slimefun.Lists.Categories;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

public class SlimefunBow extends SlimefunItem {

	public SlimefunBow(SlimefunItemStack item, ItemStack[] recipe) {
		super(Categories.WEAPONS, item, RecipeType.MAGIC_WORKBENCH, recipe);
	}

}
