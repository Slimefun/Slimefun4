package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.interfaces;

import java.util.List;

import org.bukkit.inventory.ItemStack;

@FunctionalInterface
public interface RecipeDisplayItem {
	
	List<ItemStack> getDisplayRecipes();

}
