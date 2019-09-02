package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines.electric;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItem;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AContainer;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineRecipe;
import me.mrCookieSlime.Slimefun.utils.RecipeDisplayItem;

public abstract class CarbonPress extends AContainer implements RecipeDisplayItem {

	public CarbonPress(Category category, ItemStack item, String name, RecipeType recipeType, ItemStack[] recipe) {
		super(category, item, name, recipeType, recipe);
	}
	
	@Override
	public void registerDefaultRecipes() {
		registerRecipe(15, new ItemStack[] {new CustomItem(new ItemStack(Material.CHARCOAL), 4)}, new ItemStack[] {new ItemStack(Material.COAL)});
		registerRecipe(20, new ItemStack[] {new CustomItem(new ItemStack(Material.COAL), 8)}, new ItemStack[] {SlimefunItems.CARBON});
		registerRecipe(30, new ItemStack[] {new CustomItem(SlimefunItems.CARBON, 4)}, new ItemStack[] {SlimefunItems.COMPRESSED_CARBON});
		registerRecipe(60, new ItemStack[] {SlimefunItems.CARBON_CHUNK, SlimefunItems.SYNTHETIC_DIAMOND}, new ItemStack[] {SlimefunItems.RAW_CARBONADO});
		registerRecipe(60, new ItemStack[] {SlimefunItems.CARBON_CHUNK}, new ItemStack[] {SlimefunItems.SYNTHETIC_DIAMOND});
		registerRecipe(90, new ItemStack[] {SlimefunItems.RAW_CARBONADO}, new ItemStack[] {SlimefunItems.CARBONADO});
	}

	@Override
	public List<ItemStack> getDisplayRecipes() {
		List<ItemStack> displayRecipes = new ArrayList<>(recipes.size() * 2);
		
		for (MachineRecipe recipe: recipes) {
			displayRecipes.add(recipe.getInput()[0]);
			displayRecipes.add(recipe.getOutput()[0]);
		}
		
		return displayRecipes;
	}
	
	@Override
	public String getMachineIdentifier() {
		return "CARBON_PRESS";
	}
	
	

}
