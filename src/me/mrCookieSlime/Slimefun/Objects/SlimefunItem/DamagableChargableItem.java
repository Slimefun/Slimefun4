package me.mrCookieSlime.Slimefun.Objects.SlimefunItem;

import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

public class DamagableChargableItem extends SlimefunItem {
	
	private final String chargeType;

	public DamagableChargableItem(Category category, ItemStack item, String id, RecipeType recipeType, ItemStack[] recipe, String chargeType) {
		super(category, item, id, recipeType, recipe);
		this.chargeType = chargeType;
	}

	public DamagableChargableItem(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, String chargeType) {
		super(category, item, recipeType, recipe);
		this.chargeType = chargeType;
	}
	
	public DamagableChargableItem(Category category, ItemStack item, String id, RecipeType recipeType, ItemStack[] recipe, String chargeType, String[] keys, Object[] values) {
		super(category, item, id, recipeType, recipe, keys, values);
		this.chargeType = chargeType;
	}

	public DamagableChargableItem(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, String chargeType, String[] keys, Object[] values) {
		super(category, item, recipeType, recipe, keys, values);
		this.chargeType = chargeType;
	}
	
	public String getChargeType() {
		return this.chargeType;
	}

}
