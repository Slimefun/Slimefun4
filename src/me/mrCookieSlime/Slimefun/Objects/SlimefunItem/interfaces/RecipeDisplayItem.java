package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.interfaces;

import java.util.List;

import org.bukkit.inventory.ItemStack;

@FunctionalInterface
public interface RecipeDisplayItem {
	
	List<ItemStack> getDisplayRecipes();
	
	default String getRecipeSectionLabel() {
		return "&7\u21E9 Recipes made in this Machine \u21E9";
	}

}
