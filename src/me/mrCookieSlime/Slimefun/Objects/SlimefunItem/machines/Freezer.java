package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines;

import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AContainer;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public abstract class Freezer extends AContainer {

	public Freezer(Category category, ItemStack item, String name, RecipeType recipeType, ItemStack[] recipe) {
		super(category, item, name, recipeType, recipe);
	}
	
	@Override
	public void registerDefaultRecipes() {
		registerRecipe(2, new ItemStack[] {new ItemStack(Material.WATER_BUCKET)}, new ItemStack[] {new ItemStack(Material.BUCKET), new ItemStack(Material.ICE)});
		registerRecipe(8, new ItemStack[] {new ItemStack(Material.LAVA_BUCKET)}, new ItemStack[] {new ItemStack(Material.BUCKET), new ItemStack(Material.OBSIDIAN)});
		registerRecipe(4, new ItemStack[] {new ItemStack(Material.ICE)}, new ItemStack[] {new ItemStack(Material.PACKED_ICE)});
		registerRecipe(6, new ItemStack[] {new ItemStack(Material.PACKED_ICE)}, new ItemStack[] {SlimefunItems.REACTOR_COOLANT_CELL});
	}
	
	@Override
	public String getMachineIdentifier() {
		return "FREEZER";
	}

}
