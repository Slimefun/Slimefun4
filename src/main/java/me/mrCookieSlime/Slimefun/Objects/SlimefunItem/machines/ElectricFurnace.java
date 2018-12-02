package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines;

import java.util.Iterator;

import org.bukkit.Bukkit;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AContainer;

public abstract class ElectricFurnace extends AContainer {

	public ElectricFurnace(Category category, ItemStack item, String name, RecipeType recipeType, ItemStack[] recipe) {
		super(category, item, name, recipeType, recipe);
	}
	
	@Override
	public void registerDefaultRecipes() {
		Iterator<Recipe> iterator = Bukkit.recipeIterator();
		while (iterator.hasNext()) {
			Recipe r = iterator.next();
			if (r instanceof FurnaceRecipe) {
				registerRecipe(4, new ItemStack[] {((FurnaceRecipe) r).getInput()}, new ItemStack[] {r.getResult()});
			}
		}
	}
	
	@Override
	public String getMachineIdentifier() {
		return "ELECTRIC_FURNACE";
	}

}
