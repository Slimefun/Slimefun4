package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines.electric;

import java.util.Iterator;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.CookingRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.RecipeChoice.MaterialChoice;

import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AContainer;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

public abstract class ElectricFurnace extends AContainer {

	public ElectricFurnace(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
		super(category, item, recipeType, recipe);
	}
	
	@Override
	public void registerDefaultRecipes() {
		Iterator<Recipe> iterator = Bukkit.recipeIterator();
		while (iterator.hasNext()) {
			Recipe r = iterator.next();
			if (r instanceof CookingRecipe) {
				RecipeChoice choice = ((CookingRecipe<?>) r).getInputChoice();
				if (choice instanceof MaterialChoice) {
					for (Material input : ((MaterialChoice) choice).getChoices()) {
						registerRecipe(4, new ItemStack[] {new ItemStack(input)}, new ItemStack[] {r.getResult()});
					}
				}
			}
		}
	}
	
	@Override
	public String getMachineIdentifier() {
		return "ELECTRIC_FURNACE";
	}
	
	@Override
	public ItemStack getProgressBar() {
		return new ItemStack(Material.FLINT_AND_STEEL);
	}

	@Override
	public String getInventoryTitle() {
		return "&bElectric Furnace";
	}

}
