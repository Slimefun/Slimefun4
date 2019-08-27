package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.handlers;

import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

@Deprecated
public abstract class BlockPlaceHandler extends ItemHandler {
	
	public abstract boolean onBlockPlace(BlockPlaceEvent e, ItemStack item);
	
	@Override
	public String toCodename() {
		return "BlockPlaceHandler";
	}
}