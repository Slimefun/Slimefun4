package me.mrCookieSlime.Slimefun.utils;

import java.util.List;

import org.bukkit.inventory.ItemStack;

@FunctionalInterface
public interface RecipeDisplayItem {
	
	List<ItemStack> getDisplayRecipes();

}
