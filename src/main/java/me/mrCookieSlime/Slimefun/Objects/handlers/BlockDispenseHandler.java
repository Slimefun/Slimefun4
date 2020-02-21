package me.mrCookieSlime.Slimefun.Objects.handlers;

import org.bukkit.block.Block;
import org.bukkit.block.Dispenser;
import org.bukkit.event.block.BlockDispenseEvent;

import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;

@FunctionalInterface
public interface BlockDispenseHandler extends ItemHandler {
	
	void onBlockDispense(BlockDispenseEvent e, Dispenser dispenser, Block facedBlock, SlimefunItem machine);
	
	@Override
	default Class<? extends ItemHandler> getIdentifier() {
		return BlockDispenseHandler.class;
	}
}
