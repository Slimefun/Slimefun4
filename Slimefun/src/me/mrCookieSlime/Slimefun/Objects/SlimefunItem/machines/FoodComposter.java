package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines;

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
		registerRecipe(30, new ItemStack[] {SlimefunItems.ORGANIC_FOOD2}, new ItemStack[] {SlimefunItems.FERTILIZER2});
		registerRecipe(30, new ItemStack[] {SlimefunItems.ORGANIC_FOOD3}, new ItemStack[] {SlimefunItems.FERTILIZER3});
		registerRecipe(30, new ItemStack[] {SlimefunItems.ORGANIC_FOOD4}, new ItemStack[] {SlimefunItems.FERTILIZER4});
		registerRecipe(30, new ItemStack[] {SlimefunItems.ORGANIC_FOOD5}, new ItemStack[] {SlimefunItems.FERTILIZER5});
		registerRecipe(30, new ItemStack[] {SlimefunItems.ORGANIC_FOOD6}, new ItemStack[] {SlimefunItems.FERTILIZER6});
		registerRecipe(30, new ItemStack[] {SlimefunItems.ORGANIC_FOOD7}, new ItemStack[] {SlimefunItems.FERTILIZER7});
		registerRecipe(30, new ItemStack[] {SlimefunItems.ORGANIC_FOOD8}, new ItemStack[] {SlimefunItems.FERTILIZER8});
	}
	
	@Override
	public String getMachineIdentifier() {
		return "FOOD_COMPOSTER";
	}

}
