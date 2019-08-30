package me.mrCookieSlime.Slimefun.Objects.handlers;

import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;

import org.bukkit.block.Block;

public abstract class BlockTicker implements ItemHandler {
	
	protected boolean unique = true;

	public void update() {
		if (unique) {
			uniqueTick();
			unique = false;
		}
	}
	
	/**
	 * This method must be overridden to define whether a Block
	 * needs to be run on the main server thread (World Manipulation requires that)
	 * 
	 * @return Whether this task should run on the main server thread
	 */
	public abstract boolean isSynchronized();
	
	/**
	 * This method is called every tick for every block
	 */
	public abstract void tick(Block b, SlimefunItem item, Config data);

	/**
	 * This method is called every tick but not per-block and only once.
	 */
	public void uniqueTick() {
		// Override this method and fill it with content
	}

	@Override
	public String toCodename() {
		return "BlockTicker";
	}
	
	/**
	 * This method resets the 'unique' flag for {@link BlockTicker#uniqueTick()}
	 */
	public void startNewTick() {
		unique = true;
	}

}
