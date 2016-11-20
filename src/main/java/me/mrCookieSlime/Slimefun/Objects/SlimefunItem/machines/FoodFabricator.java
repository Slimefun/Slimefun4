package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines;

import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AContainer;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public abstract class FoodFabricator extends AContainer {

	public FoodFabricator(Category category, ItemStack item, String name, RecipeType recipeType, ItemStack[] recipe) {
		super(category, item, name, recipeType, recipe);
	}
	
	@Override
	public void registerDefaultRecipes() {
		registerRecipe(12, new ItemStack[] {SlimefunItems.CAN, new ItemStack(Material.WHEAT)}, new ItemStack[] {SlimefunItems.ORGANIC_FOOD2});
		registerRecipe(12, new ItemStack[] {SlimefunItems.CAN, new ItemStack(Material.CARROT_ITEM)}, new ItemStack[] {SlimefunItems.ORGANIC_FOOD3});
		registerRecipe(12, new ItemStack[] {SlimefunItems.CAN, new ItemStack(Material.POTATO_ITEM)}, new ItemStack[] {SlimefunItems.ORGANIC_FOOD4});
		registerRecipe(12, new ItemStack[] {SlimefunItems.CAN, new ItemStack(Material.SEEDS)}, new ItemStack[] {SlimefunItems.ORGANIC_FOOD5});
		registerRecipe(12, new ItemStack[] {SlimefunItems.CAN, new ItemStack(Material.BEETROOT)}, new ItemStack[] {SlimefunItems.ORGANIC_FOOD6});
		registerRecipe(12, new ItemStack[] {SlimefunItems.CAN, new ItemStack(Material.MELON)}, new ItemStack[] {SlimefunItems.ORGANIC_FOOD7});
		registerRecipe(12, new ItemStack[] {SlimefunItems.CAN, new ItemStack(Material.APPLE)}, new ItemStack[] {SlimefunItems.ORGANIC_FOOD8});
	}
	
	@Override
	public String getMachineIdentifier() {
		return "FOOD_FABRICATOR";
	}

}
