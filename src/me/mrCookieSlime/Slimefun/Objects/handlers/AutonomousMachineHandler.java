package me.mrCookieSlime.Slimefun.Objects.handlers;

import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;

import org.bukkit.block.Block;
import org.bukkit.block.Dispenser;
import org.bukkit.event.block.BlockDispenseEvent;

@FunctionalInterface
public interface AutonomousMachineHandler extends ItemHandler {
	
	boolean onBlockDispense(BlockDispenseEvent e, Block dispenser, Dispenser d, Block block, Block chest, SlimefunItem machine);
	
	default String toCodename() {
		return "AutonomousMachineHandler";
	}
}
