package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.interfaces;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@FunctionalInterface
public interface RecipeDisplayItem {
	
	List<ItemStack> getDisplayRecipes();
	
	@Deprecated
	default String getRecipeSectionLabel() {
		return "&7\u21E9 Recipes made in this Machine \u21E9";
	}
	
	default String getRecipeSectionLabel(Player p) {
		return getRecipeSectionLabel();
	}
}
