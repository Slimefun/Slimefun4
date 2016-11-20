package me.mrCookieSlime.Slimefun.Objects.SlimefunItem;

import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;

import org.bukkit.inventory.ItemStack;

public class ChargedItem extends SlimefunItem {
	
	String chargeType;

	public ChargedItem(Category category, ItemStack item, String name, RecipeType recipeType, ItemStack[] recipe, String chargeType) {
		super(category, item, name, recipeType, recipe);
		this.chargeType = chargeType;
	}
	
	public ChargedItem(Category category, ItemStack item, String name, RecipeType recipeType, ItemStack[] recipe, String chargeType, String[] keys, Object[] values) {
		super(category, item, name, recipeType, recipe, keys, values);
		this.chargeType = chargeType;
	}
	
	public String getChargeType()		{		return this.chargeType;		}

}
