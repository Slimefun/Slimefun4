package me.mrCookieSlime.Slimefun.Objects.SlimefunItem;

import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.implementation.listeners.BackpackListener;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.handlers.ItemUseHandler;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

public class SlimefunBackpack extends SimpleSlimefunItem<ItemUseHandler> {
	
	private final int size;

	public SlimefunBackpack(int size, Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
		super(category, item, recipeType, recipe);
		
		this.size = size;
	}
	
	public int getSize() {
		return size;
	}

	@Override
	public ItemUseHandler getItemHandler() {
		return e -> BackpackListener.openBackpack(e.getPlayer(), e.getItem(), this);
	}

}
