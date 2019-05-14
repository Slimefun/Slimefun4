package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.handlers;

import java.util.List;

import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public abstract class BlockBreakHandler extends ItemHandler {
	
	public abstract boolean onBlockBreak(BlockBreakEvent e, ItemStack item, int fortune, List<ItemStack> drops);
	
	@Override
	public String toCodename() {
		return "BlockBreakHandler";
	}
}
