package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines.electric;

import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AContainer;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineRecipe;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.interfaces.RecipeDisplayItem;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public abstract class Freezer extends AContainer implements RecipeDisplayItem {

	public Freezer(Category category, ItemStack item, String name, RecipeType recipeType, ItemStack[] recipe) {
		super(category, item, name, recipeType, recipe);
	}
	
	@Override
	public void registerDefaultRecipes() {
		registerRecipe(2, new ItemStack[] {new ItemStack(Material.WATER_BUCKET)}, new ItemStack[] {new ItemStack(Material.BUCKET), new ItemStack(Material.ICE)});
		registerRecipe(8, new ItemStack[] {new ItemStack(Material.LAVA_BUCKET)}, new ItemStack[] {new ItemStack(Material.BUCKET), new ItemStack(Material.OBSIDIAN)});
		registerRecipe(4, new ItemStack[] {new ItemStack(Material.ICE)}, new ItemStack[] {new ItemStack(Material.PACKED_ICE)});
		registerRecipe(6, new ItemStack[] {new ItemStack(Material.PACKED_ICE)}, new ItemStack[] {new ItemStack(Material.BLUE_ICE)});
		registerRecipe(8, new ItemStack[] {new ItemStack(Material.BLUE_ICE)}, new ItemStack[] {SlimefunItems.REACTOR_COOLANT_CELL});
	}
	
	@Override
	public List<ItemStack> getDisplayRecipes() {
		List<ItemStack> displayRecipes = new ArrayList<>(recipes.size() * 2);
		
		for (MachineRecipe recipe: recipes) {
			displayRecipes.add(recipe.getInput()[0]);
			displayRecipes.add(recipe.getOutput()[recipe.getOutput().length - 1]);
		}
		
		return displayRecipes;
	}
	
	@Override
	public ItemStack getProgressBar() {
		return new ItemStack(Material.GOLDEN_PICKAXE);
	}

	@Override
	public String getInventoryTitle() {
		return "&bFreezer";
	}
	
	@Override
	public String getMachineIdentifier() {
		return "FREEZER";
	}

}
