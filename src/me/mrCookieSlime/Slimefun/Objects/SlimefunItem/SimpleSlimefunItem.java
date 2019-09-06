package me.mrCookieSlime.Slimefun.Objects.SlimefunItem;

import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.handlers.ItemHandler;

public abstract class SimpleSlimefunItem<T extends ItemHandler> extends SlimefunItem {

	public SimpleSlimefunItem(Category category, ItemStack item, String id, RecipeType recipeType, ItemStack[] recipe) {
		super(category, item, id, recipeType, recipe);
	}
	
	public SimpleSlimefunItem(Category category, ItemStack item, String id, RecipeType recipeType, ItemStack[] recipe, ItemStack recipeOutput) {
		super(category, item, id, recipeType, recipe, recipeOutput);
	}
	
	public SimpleSlimefunItem(Category category, ItemStack item, String id, RecipeType recipeType, ItemStack[] recipe, String[] keys, Object[] values) {
		super(category, item, id, recipeType, recipe, keys, values);
	}
	
	@Override
	public void preRegister() {
		addItemHandler(getItemHandler());
	}

	public abstract T getItemHandler();

}
