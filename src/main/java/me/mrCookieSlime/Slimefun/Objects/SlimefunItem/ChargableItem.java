package me.mrCookieSlime.Slimefun.Objects.SlimefunItem;

import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

public class ChargableItem extends SlimefunItem {
	
	private String chargeType;

	public ChargableItem(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, String chargeType) {
		super(category, item, recipeType, recipe);
		this.chargeType = chargeType;
	}
	
	public ChargableItem(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, String chargeType, String[] keys, Object[] values) {
		super(category, item, recipeType, recipe, keys, values);
		this.chargeType = chargeType;
	}
	
	public String getChargeType() {
		return chargeType;
	}

}
