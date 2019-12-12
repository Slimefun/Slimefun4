package me.mrCookieSlime.Slimefun.Objects.SlimefunItem;

import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.interfaces.Soulbound;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

public class SoulboundBackpack extends SlimefunBackpack implements Soulbound {

	public SoulboundBackpack(int size, Category category, SlimefunItemStack item, ItemStack[] recipe) {
		super(size, category, item, RecipeType.MAGIC_WORKBENCH, recipe);
	}

}
