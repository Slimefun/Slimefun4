package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines.electric.generators;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AGenerator;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineFuel;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

public abstract class MagnesiumGenerator extends AGenerator {

	public MagnesiumGenerator(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
		super(category, item, recipeType, recipe);
	}
	
	@Override
	public void registerDefaultRecipes() {
		registerFuel(new MachineFuel(12, SlimefunItems.MAGNESIUM_SALT));
	}

	@Override
	public ItemStack getProgressBar() {
		return new ItemStack(Material.FLINT_AND_STEEL);
	}

	@Override
	public String getInventoryTitle() {
		return "&cMagnesium-powered Generator";
	}

}
