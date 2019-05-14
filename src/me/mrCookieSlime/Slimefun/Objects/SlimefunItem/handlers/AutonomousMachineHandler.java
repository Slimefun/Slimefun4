package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.handlers;

import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;

import org.bukkit.block.Block;
import org.bukkit.block.Dispenser;
import org.bukkit.event.block.BlockDispenseEvent;

public abstract class AutonomousMachineHandler extends ItemHandler {

	@Override
	public String toCodename() {
		return "AutonomousMachineHandler";
	}
	
	public abstract boolean onBlockDispense(BlockDispenseEvent e, Block dispenser, Dispenser d, Block block, Block chest, SlimefunItem machine);

}
