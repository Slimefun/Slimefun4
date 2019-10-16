package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.items;

import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.Slimefun.GEO.GEOScanner;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.ItemInteractionHandler;
import me.mrCookieSlime.Slimefun.Setup.SlimefunManager;

public class PortableGEOScanner extends SimpleSlimefunItem<ItemInteractionHandler> {

	public PortableGEOScanner(Category category, ItemStack item, String id, RecipeType recipeType, ItemStack[] recipe) {
		super(category, item, id, recipeType, recipe);
	}
	
	@Override
	public ItemInteractionHandler getItemHandler() {
		return (e, p, item) -> {
			if (SlimefunManager.isItemSimiliar(item, getItem(), true)) {
				e.setCancelled(true);
				GEOScanner.scanChunk(p, p.getLocation().getChunk());
				return true;
			}
			return false;
		};
	}

}
