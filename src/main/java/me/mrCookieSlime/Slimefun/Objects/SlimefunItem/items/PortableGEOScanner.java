package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.items;

import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.Slimefun.GEO.GEOScanner;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.ItemInteractionHandler;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

public class PortableGEOScanner extends SimpleSlimefunItem<ItemInteractionHandler> {

	public PortableGEOScanner(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
		super(category, item, recipeType, recipe);
	}
	
	@Override
	public ItemInteractionHandler getItemHandler() {
		return (e, p, item) -> {
			if (isItem(item)) {
				e.setCancelled(true);
				GEOScanner.scanChunk(p, p.getLocation().getChunk());
				return true;
			}
			return false;
		};
	}

}
