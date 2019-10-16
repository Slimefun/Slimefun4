package me.mrCookieSlime.Slimefun.Objects.SlimefunItem;

import me.mrCookieSlime.Slimefun.Lists.Categories;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

import org.bukkit.inventory.ItemStack;

public class Jetpack extends DamagableChargableItem {
	
	private final double thrust;

	public Jetpack(SlimefunItemStack item, ItemStack[] recipe, double thrust) {
		super(Categories.TECH, item, RecipeType.ENHANCED_CRAFTING_TABLE, recipe, "Jetpack");
		this.thrust = thrust;
	}
	
	public double getThrust() {
		return thrust;
	}
	

}
