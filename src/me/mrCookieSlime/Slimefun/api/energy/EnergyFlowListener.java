package me.mrCookieSlime.Slimefun.api.energy;

import org.bukkit.block.Block;

@FunctionalInterface
public interface EnergyFlowListener {

	void onPulse(Block b);
	
}
