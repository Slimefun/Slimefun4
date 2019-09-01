package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines.electric;

import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AContainer;

import org.bukkit.inventory.ItemStack;

public abstract class FoodComposter extends AContainer {

	public FoodComposter(Category category, ItemStack item, String name, RecipeType recipeType, ItemStack[] recipe) {
		super(category, item, name, recipeType, recipe);
	}
	
	@Override
	public void registerDefaultRecipes() {
		registerRecipe(30, new ItemStack[] {SlimefunItems.WHEAT_ORGANIC_FOOD}, new ItemStack[] {SlimefunItems.WHEAT_FERTILIZER});
		registerRecipe(30, new ItemStack[] {SlimefunItems.CARROT_ORGANIC_FOOD}, new ItemStack[] {SlimefunItems.CARROT_FERTILIZER});
		registerRecipe(30, new ItemStack[] {SlimefunItems.POTATO_ORGANIC_FOOD}, new ItemStack[] {SlimefunItems.POTATO_FERTILIZER});
		registerRecipe(30, new ItemStack[] {SlimefunItems.SEEDS_ORGANIC_FOOD}, new ItemStack[] {SlimefunItems.SEEDS_FERTILIZER});
		registerRecipe(30, new ItemStack[] {SlimefunItems.BEETROOT_ORGANIC_FOOD}, new ItemStack[] {SlimefunItems.BEETROOT_FERTILIZER});
		registerRecipe(30, new ItemStack[] {SlimefunItems.MELON_ORGANIC_FOOD}, new ItemStack[] {SlimefunItems.MELON_FERTILIZER});
		registerRecipe(30, new ItemStack[] {SlimefunItems.APPLE_ORGANIC_FOOD}, new ItemStack[] {SlimefunItems.APPLE_FERTILIZER});
	}
	
	@Override
	public String getMachineIdentifier() {
		return "FOOD_COMPOSTER";
	}

}
