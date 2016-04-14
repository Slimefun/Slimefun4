package me.mrCookieSlime.Slimefun.Objects.SlimefunItem;

import me.mrCookieSlime.Slimefun.Lists.Categories;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;

import org.bukkit.inventory.ItemStack;

public class Jetpack extends DamagableChargableItem {
	
	double thrust;

	public Jetpack(ItemStack item, String name, ItemStack[] recipe, double thrust) {
		super(Categories.TECH, item, name, RecipeType.ENHANCED_CRAFTING_TABLE, recipe, "Jetpack");
		this.thrust = thrust;
	}
	
	public double getThrust() {
		return thrust;
	}
	

}
