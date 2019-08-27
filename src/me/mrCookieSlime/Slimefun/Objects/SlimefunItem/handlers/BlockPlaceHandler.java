package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.handlers;

import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.Slimefun.Objects.handlers.ItemHandler;

@Deprecated
public abstract class BlockPlaceHandler implements ItemHandler {
	
	public abstract boolean onBlockPlace(BlockPlaceEvent e, ItemStack item);
	
	@Override
	public String toCodename() {
		return "BlockPlaceHandler";
	}
}