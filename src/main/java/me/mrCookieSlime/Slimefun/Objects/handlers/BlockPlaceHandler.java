package me.mrCookieSlime.Slimefun.Objects.handlers;

import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

@FunctionalInterface
public interface BlockPlaceHandler extends ItemHandler {
	
	boolean onBlockPlace(BlockPlaceEvent e, ItemStack item);
	
	default String toCodename() {
		return "BlockPlaceHandler";
	}
}
