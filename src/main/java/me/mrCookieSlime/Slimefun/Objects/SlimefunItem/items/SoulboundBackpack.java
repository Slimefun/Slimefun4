package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.items;

import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.core.attributes.Soulbound;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunBackpack;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

public class SoulboundBackpack extends SlimefunBackpack implements Soulbound {

	public SoulboundBackpack(int size, Category category, SlimefunItemStack item, ItemStack[] recipe) {
		super(size, category, item, RecipeType.MAGIC_WORKBENCH, recipe);
	}

}
