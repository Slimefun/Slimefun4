package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.handlers;

import org.bukkit.block.Block;
import org.bukkit.block.Dispenser;
import org.bukkit.event.block.BlockDispenseEvent;

public abstract class AutonomousToolHandler extends ItemHandler {

	@Override
	public String toCodename() {
		return "AutonomousToolHandler";
	}
	
	public abstract boolean onBlockDispense(BlockDispenseEvent e, Block dispenser, Dispenser d, Block block, Block chest, int i);

}
