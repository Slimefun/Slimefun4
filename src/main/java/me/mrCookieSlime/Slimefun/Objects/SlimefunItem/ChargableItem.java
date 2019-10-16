package me.mrCookieSlime.Slimefun.Objects.SlimefunItem;

import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;

import org.bukkit.inventory.ItemStack;

public class ChargableItem extends SlimefunItem {
	
	private String chargeType;

	public ChargableItem(Category category, ItemStack item, String id, RecipeType recipeType, ItemStack[] recipe, String chargeType) {
		super(category, item, id, recipeType, recipe);
		this.chargeType = chargeType;
	}
	
	public ChargableItem(Category category, ItemStack item, String id, RecipeType recipeType, ItemStack[] recipe, String chargeType, String[] keys, Object[] values) {
		super(category, item, id, recipeType, recipe, keys, values);
		this.chargeType = chargeType;
	}
	
	public String getChargeType() {
		return chargeType;
	}

}
