package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines.electric.geo;

import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.Slimefun.GEO.GEOScanner;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.ItemInteractionHandler;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

public class GEOScannerBlock extends SimpleSlimefunItem<ItemInteractionHandler> {

	public GEOScannerBlock(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
		super(category, item, recipeType, recipe);
	}
	
	@Override
	protected boolean areItemHandlersPrivate() {
		return false;
	}

	@Override
	public ItemInteractionHandler getItemHandler() {
		return (e, p, stack) -> {
			if (e.getClickedBlock() == null) return false;
			String item = BlockStorage.checkID(e.getClickedBlock());
			if (item == null || !item.equals(getID())) return false;
			e.setCancelled(true);
			
			GEOScanner.scanChunk(p, e.getClickedBlock().getChunk());
			return true;
		};
	}
}
