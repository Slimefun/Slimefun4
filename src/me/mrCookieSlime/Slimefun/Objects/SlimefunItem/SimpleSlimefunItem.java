package me.mrCookieSlime.Slimefun.Objects.SlimefunItem;

import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.handlers.ItemInteractionHandler;

public abstract class SimpleSlimefunItem extends SlimefunItem {

	public SimpleSlimefunItem(Category category, ItemStack item, String id, RecipeType recipeType, ItemStack[] recipe) {
		super(category, item, id, recipeType, recipe);
	}
	
	@Override
	public void register(boolean slimefun) {
		addItemHandler(onRightClick());
		super.register(slimefun);
	}

	public abstract ItemInteractionHandler onRightClick();

}
