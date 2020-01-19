package me.mrCookieSlime.Slimefun.Objects.SlimefunItem;

import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.Slimefun.Lists.Categories;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

public class JetBoots extends DamagableChargableItem {
	
	private final double speed;

	public JetBoots(SlimefunItemStack item, ItemStack[] recipe, double speed) {
		super(Categories.TECH, item, RecipeType.ENHANCED_CRAFTING_TABLE, recipe, "Jet Boots");
		this.speed = speed;
	}
	
	public double getSpeed() {
		return speed;
	}
	

}
