package me.mrCookieSlime.Slimefun.Objects.SlimefunItem;

import me.mrCookieSlime.Slimefun.Lists.Categories;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;

import org.bukkit.inventory.ItemStack;

public class JetBoots extends DamagableChargableItem {
	
	double speed;

	public JetBoots(ItemStack item, String name, ItemStack[] recipe, double speed) {
		super(Categories.TECH, item, name, RecipeType.ENHANCED_CRAFTING_TABLE, recipe, "Jet Boots");
		this.speed = speed;
	}
	
	public double getSpeed() {
		return speed;
	}
	

}
