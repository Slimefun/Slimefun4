package me.mrCookieSlime.Slimefun.Objects.SlimefunItem;

import me.mrCookieSlime.Slimefun.Lists.Categories;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;

import org.bukkit.inventory.ItemStack;

public class Jetpack extends DamagableChargableItem {
	
	private double thrust;

	public Jetpack(ItemStack item, String id, ItemStack[] recipe, double thrust) {
		super(Categories.TECH, item, id, RecipeType.ENHANCED_CRAFTING_TABLE, recipe, "Jetpack");
		this.thrust = thrust;
	}
	
	public double getThrust() {
		return thrust;
	}
	

}
