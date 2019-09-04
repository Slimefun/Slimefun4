package me.mrCookieSlime.Slimefun.Objects.SlimefunItem;

import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;

import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

public class SlimefunArmorPiece extends SlimefunItem {
	
	private PotionEffect[] effects;

	public SlimefunArmorPiece(Category category, ItemStack item, String id, RecipeType recipeType, ItemStack[] recipe, PotionEffect[] effects) {
		super(category, item, id, recipeType, recipe);
		this.effects = effects;
	}
	
	public SlimefunArmorPiece(Category category, ItemStack item, String id, RecipeType recipeType, ItemStack[] recipe, PotionEffect[] effects, String[] keys, Object[] values) {
		super(category, item, id, recipeType, recipe, keys, values);
		this.effects = effects;
	}
	
	public PotionEffect[] getEffects() {
		return this.effects;
	}

}
