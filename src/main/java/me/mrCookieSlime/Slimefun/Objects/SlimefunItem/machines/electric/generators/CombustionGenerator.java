package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines.electric.generators;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AGenerator;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineFuel;

public abstract class CombustionGenerator extends AGenerator {

	public CombustionGenerator(Category category, ItemStack item, String id, RecipeType recipeType, ItemStack[] recipe) {
		super(category, item, id, recipeType, recipe);
	}
	
	@Override
	public void registerDefaultRecipes() {
		registerFuel(new MachineFuel(30, SlimefunItems.BUCKET_OF_OIL));
		registerFuel(new MachineFuel(90, SlimefunItems.BUCKET_OF_FUEL));
	}

	@Override
	public ItemStack getProgressBar() {
		return new ItemStack(Material.FLINT_AND_STEEL);
	}

	@Override
	public String getInventoryTitle() {
		return "&cCombustion Reactor";
	}

}
