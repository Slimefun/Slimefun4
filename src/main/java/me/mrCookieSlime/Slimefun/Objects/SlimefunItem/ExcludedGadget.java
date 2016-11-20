package me.mrCookieSlime.Slimefun.Objects.SlimefunItem;

import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;

import org.bukkit.inventory.ItemStack;

public class ExcludedGadget extends SlimefunGadget {

	public ExcludedGadget(Category category, ItemStack item, String name, RecipeType recipeType, ItemStack[] recipe, ItemStack[] machineRecipes) {
		super(category, item, name, recipeType, recipe, machineRecipes);
	}
	
	public ExcludedGadget(Category category, ItemStack item, String name, RecipeType recipeType, ItemStack[] recipe, ItemStack[] machineRecipes, String[] keys, Object[] values) {
		super(category, item, name, recipeType, recipe, machineRecipes, keys, values);
	}
}
