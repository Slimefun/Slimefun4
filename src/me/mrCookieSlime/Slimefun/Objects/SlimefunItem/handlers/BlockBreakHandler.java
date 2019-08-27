package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.handlers;

import java.util.List;

import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.Slimefun.Objects.handlers.ItemHandler;

@Deprecated
public abstract class BlockBreakHandler implements ItemHandler {
	
	public abstract boolean onBlockBreak(BlockBreakEvent e, ItemStack item, int fortune, List<ItemStack> drops);
	
	@Override
	public String toCodename() {
		return "BlockBreakHandler";
	}
}