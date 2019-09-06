package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines.electric;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AContainer;

public abstract class FoodFabricator extends AContainer {

	public FoodFabricator(Category category, ItemStack item, String name, RecipeType recipeType, ItemStack[] recipe) {
		super(category, item, name, recipeType, recipe);
	}
	
	@Override
	public void registerDefaultRecipes() {
		registerRecipe(12, new ItemStack[] {SlimefunItems.CAN, new ItemStack(Material.WHEAT)}, new ItemStack[] {SlimefunItems.WHEAT_ORGANIC_FOOD});
		registerRecipe(12, new ItemStack[] {SlimefunItems.CAN, new ItemStack(Material.CARROT)}, new ItemStack[] {SlimefunItems.CARROT_ORGANIC_FOOD});
		registerRecipe(12, new ItemStack[] {SlimefunItems.CAN, new ItemStack(Material.POTATO)}, new ItemStack[] {SlimefunItems.POTATO_ORGANIC_FOOD});
		registerRecipe(12, new ItemStack[] {SlimefunItems.CAN, new ItemStack(Material.BEETROOT)}, new ItemStack[] {SlimefunItems.BEETROOT_ORGANIC_FOOD});
		registerRecipe(12, new ItemStack[] {SlimefunItems.CAN, new ItemStack(Material.MELON)}, new ItemStack[] {SlimefunItems.MELON_ORGANIC_FOOD});
		registerRecipe(12, new ItemStack[] {SlimefunItems.CAN, new ItemStack(Material.APPLE)}, new ItemStack[] {SlimefunItems.APPLE_ORGANIC_FOOD});
	}
	
	@Override
	public String getMachineIdentifier() {
		return "FOOD_FABRICATOR";
	}
	
	@Override
	public ItemStack getProgressBar() {
		return new ItemStack(Material.GOLDEN_HOE);
	}

	@Override
	public String getInventoryTitle() {
		return "&cFood Fabricator";
	}

}
