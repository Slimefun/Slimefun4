package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines.electric;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AContainer;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineRecipe;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.interfaces.RecipeDisplayItem;

public class ElectricIngotPulverizer extends AContainer implements RecipeDisplayItem {

	public ElectricIngotPulverizer(Category category, ItemStack item, String id, RecipeType recipeType, ItemStack[] recipe) {
		super(category, item, id, recipeType, recipe);
	}
	
	@Override
	public String getInventoryTitle() {
		return "&bElectric Ingot Pulverizer";
	}

	@Override
	public ItemStack getProgressBar() {
		return new ItemStack(Material.IRON_PICKAXE);
	}

	@Override
	public void registerDefaultRecipes() {
		registerRecipe(3, new ItemStack[]{SlimefunItems.ALUMINUM_INGOT}, new ItemStack[]{SlimefunItems.ALUMINUM_DUST});
		registerRecipe(3, new ItemStack[]{SlimefunItems.COPPER_INGOT}, new ItemStack[]{SlimefunItems.COPPER_DUST});
		registerRecipe(3, new ItemStack[]{SlimefunItems.GOLD_4K}, new ItemStack[]{SlimefunItems.GOLD_DUST});
		registerRecipe(3, new ItemStack[]{new ItemStack(Material.IRON_INGOT)}, new ItemStack[]{SlimefunItems.IRON_DUST});
		registerRecipe(3, new ItemStack[]{SlimefunItems.LEAD_INGOT}, new ItemStack[]{SlimefunItems.LEAD_DUST});
		registerRecipe(3, new ItemStack[]{SlimefunItems.MAGNESIUM_INGOT}, new ItemStack[]{SlimefunItems.MAGNESIUM_DUST});
		registerRecipe(3, new ItemStack[]{SlimefunItems.SILVER_INGOT}, new ItemStack[]{SlimefunItems.SILVER_DUST});
		registerRecipe(3, new ItemStack[]{SlimefunItems.TIN_INGOT}, new ItemStack[]{SlimefunItems.TIN_DUST});
		registerRecipe(3, new ItemStack[]{SlimefunItems.ZINC_INGOT}, new ItemStack[]{SlimefunItems.ZINC_DUST});
	}

	@Override
	public List<ItemStack> getDisplayRecipes() {
		List<ItemStack> displayRecipes = new ArrayList<>(recipes.size() * 2);
		
		for (MachineRecipe recipe: recipes) {
			displayRecipes.add(recipe.getInput()[0]);
			displayRecipes.add(recipe.getOutput()[0]);
		}
		
		return displayRecipes;
	}

	@Override
	public int getEnergyConsumption() {
		return 7;
	}

	@Override
	public int getSpeed() {
		return 1;
	}

	@Override
	public String getMachineIdentifier() {
		return "ELECTRIC_INGOT_PULVERIZER";
	}

}
