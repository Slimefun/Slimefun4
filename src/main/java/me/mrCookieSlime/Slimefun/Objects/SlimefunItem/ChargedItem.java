package me.mrCookieSlime.Slimefun.Objects.SlimefunItem;

import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;

public class ChargedItem extends SlimefunItem {
	
	private String chargeType;

	public ChargedItem(Category category, ItemStack item, String id, RecipeType recipeType, ItemStack[] recipe, String chargeType) {
		super(category, item, id, recipeType, recipe);
		this.chargeType = chargeType;
	}
	
	public ChargedItem(Category category, ItemStack item, String id, RecipeType recipeType, ItemStack[] recipe, String chargeType, String[] keys, Object[] values) {
		super(category, item, id, recipeType, recipe, keys, values);
		this.chargeType = chargeType;
	}
	
	public String getChargeType() {
		return chargeType;
	}

}
