package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines;

import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItem;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AContainer;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

public abstract class CarbonPress extends AContainer {

	public CarbonPress(Category category, ItemStack item, String name, RecipeType recipeType, ItemStack[] recipe) {
		super(category, item, name, recipeType, recipe);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void registerDefaultRecipes() {
		registerRecipe(15, new ItemStack[] {new CustomItem(new MaterialData(Material.COAL,  (byte) 1).toItemStack(1), 4)}, new ItemStack[] {new ItemStack(Material.COAL)});
		registerRecipe(20, new ItemStack[] {new CustomItem(new ItemStack(Material.COAL), 8)}, new ItemStack[] {SlimefunItems.CARBON});
		registerRecipe(30, new ItemStack[] {new CustomItem(SlimefunItems.CARBON, 4)}, new ItemStack[] {SlimefunItems.COMPRESSED_CARBON});
		registerRecipe(60, new ItemStack[] {SlimefunItems.CARBON_CHUNK, SlimefunItems.SYNTHETIC_DIAMOND}, new ItemStack[] {SlimefunItems.RAW_CARBONADO});
		registerRecipe(60, new ItemStack[] {SlimefunItems.CARBON_CHUNK}, new ItemStack[] {SlimefunItems.SYNTHETIC_DIAMOND});
		registerRecipe(90, new ItemStack[] {SlimefunItems.RAW_CARBONADO}, new ItemStack[] {SlimefunItems.CARBONADO});
	}
	
	@Override
	public String getMachineIdentifier() {
		return "CARBON_PRESS";
	}
	
	

}
