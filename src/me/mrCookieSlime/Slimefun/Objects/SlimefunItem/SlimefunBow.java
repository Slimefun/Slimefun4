package me.mrCookieSlime.Slimefun.Objects.SlimefunItem;

import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.Slimefun.Lists.Categories;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.handlers.BowShootHandler;

public abstract class SlimefunBow extends SimpleSlimefunItem<BowShootHandler> {

	public SlimefunBow(ItemStack item, String id, ItemStack[] recipe) {
		super(Categories.WEAPONS, item, id, RecipeType.MAGIC_WORKBENCH, recipe);
	}

}
