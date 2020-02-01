package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.items;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.Slimefun.GEO.GEOScanner;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.ItemUseHandler;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

public class PortableGEOScanner extends SimpleSlimefunItem<ItemUseHandler> {

	public PortableGEOScanner(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
		super(category, item, recipeType, recipe);
	}
	
	@Override
	public ItemUseHandler getItemHandler() {
		return e -> {
			e.cancel();
			Player p = e.getPlayer();
			GEOScanner.scanChunk(p, p.getLocation().getChunk());
		};
	}

}
