package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines.electric.geo;

import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.Slimefun.GEO.GEOScanner;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.ItemInteractionHandler;
import me.mrCookieSlime.Slimefun.api.BlockStorage;

public class GEOScannerBlock extends SimpleSlimefunItem<ItemInteractionHandler> {

	public GEOScannerBlock(Category category, ItemStack item, String id, RecipeType recipeType, ItemStack[] recipe) {
		super(category, item, id, recipeType, recipe);
	}

	@Override
	public ItemInteractionHandler getItemHandler() {
		return (e, p, stack) -> {
			if (e.getClickedBlock() == null) return false;
			String item = BlockStorage.checkID(e.getClickedBlock());
			if (item == null || !item.equals("GPS_GEO_SCANNER")) return false;
			e.setCancelled(true);
			
			GEOScanner.scanChunk(p, e.getClickedBlock().getChunk());
			return true;
		};
	}
}
