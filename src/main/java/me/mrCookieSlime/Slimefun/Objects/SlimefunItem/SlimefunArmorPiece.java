package me.mrCookieSlime.Slimefun.Objects.SlimefunItem;

import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;

import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

public class SlimefunArmorPiece extends SlimefunItem {
	
	PotionEffect[] effects;

	public SlimefunArmorPiece(Category category, ItemStack item, String name, RecipeType recipeType, ItemStack[] recipe, PotionEffect[] effects) {
		super(category, item, name, recipeType, recipe);
		this.effects = effects;
	}
	
	public SlimefunArmorPiece(Category category, ItemStack item, String name, RecipeType recipeType, ItemStack[] recipe, PotionEffect[] effects, String[] keys, Object[] values) {
		super(category, item, name, recipeType, recipe, keys, values);
		this.effects = effects;
	}
	
	public PotionEffect[] getEffects()		{		return this.effects;		}

}
