package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines.electric;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AContainer;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.interfaces.RecipeDisplayItem;

public abstract class ElectricOreGrinder extends AContainer implements RecipeDisplayItem {

	public ElectricOreGrinder(Category category, ItemStack item, String name, RecipeType recipeType, ItemStack[] recipe) {
		super(category, item, name, recipeType, recipe);
	}
	
	@Override
	public String getMachineIdentifier() {
		return "ELECTRIC_ORE_GRINDER";
	}
	
	@Override
	public ItemStack getProgressBar() {
		return new ItemStack(Material.IRON_PICKAXE);
	}

	@Override
	public String getInventoryTitle() {
		return "&bElectric Ore Grinder";
	}

}
