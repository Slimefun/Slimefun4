package me.mrCookieSlime.Slimefun.Objects.SlimefunItem;

import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;

import org.bukkit.inventory.ItemStack;

public class DamagableChargableItem extends SlimefunItem {
	
	private String chargeType;

	public DamagableChargableItem(Category category, ItemStack item, String id, RecipeType recipeType, ItemStack[] recipe, String chargeType) {
		super(category, item, id, recipeType, recipe);
		this.chargeType = chargeType;
	}
	
	public DamagableChargableItem(Category category, ItemStack item, String id, RecipeType recipeType, ItemStack[] recipe, String chargeType, String[] keys, Object[] values) {
		super(category, item, id, recipeType, recipe, keys, values);
		this.chargeType = chargeType;
	}
	
	public String getChargeType() {
		return this.chargeType;
	}

}
