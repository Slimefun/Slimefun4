package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines.electric;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AContainer;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineRecipe;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.interfaces.RecipeDisplayItem;

public abstract class ElectricIngotFactory extends AContainer implements RecipeDisplayItem {

	public ElectricIngotFactory(Category category, ItemStack item, String name, RecipeType recipeType, ItemStack[] recipe) {
		super(category, item, name, recipeType, recipe);
	}
	
	@Override
	public void registerDefaultRecipes() {
		registerRecipe(new MachineRecipe(8, new ItemStack[] {SlimefunItems.ALUMINUM_DUST}, new ItemStack[] {SlimefunItems.ALUMINUM_INGOT}));
		registerRecipe(new MachineRecipe(8, new ItemStack[] {SlimefunItems.COPPER_DUST}, new ItemStack[] {SlimefunItems.COPPER_INGOT}));
		registerRecipe(new MachineRecipe(8, new ItemStack[] {SlimefunItems.GOLD_DUST}, new ItemStack[] {SlimefunItems.GOLD_4K}));
		registerRecipe(new MachineRecipe(8, new ItemStack[] {SlimefunItems.IRON_DUST}, new ItemStack[] {new ItemStack(Material.IRON_INGOT)}));
		registerRecipe(new MachineRecipe(8, new ItemStack[] {SlimefunItems.LEAD_DUST}, new ItemStack[] {SlimefunItems.LEAD_INGOT}));
		registerRecipe(new MachineRecipe(8, new ItemStack[] {SlimefunItems.MAGNESIUM_DUST}, new ItemStack[] {SlimefunItems.MAGNESIUM_INGOT}));
		registerRecipe(new MachineRecipe(8, new ItemStack[] {SlimefunItems.SILVER_DUST}, new ItemStack[] {SlimefunItems.SILVER_INGOT}));
		registerRecipe(new MachineRecipe(8, new ItemStack[] {SlimefunItems.TIN_DUST}, new ItemStack[] {SlimefunItems.TIN_INGOT}));
		registerRecipe(new MachineRecipe(8, new ItemStack[] {SlimefunItems.ZINC_DUST}, new ItemStack[] {SlimefunItems.ZINC_INGOT}));
	}
	
	@Override
	public String getMachineIdentifier() {
		return "ELECTRIC_INGOT_FACTORY";
	}
	
	@Override
	public ItemStack getProgressBar() {
		return new ItemStack(Material.FLINT_AND_STEEL);
	}

	@Override
	public String getInventoryTitle() {
		return "&cElectric Ingot Factory";
	}

}
