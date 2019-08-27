package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.handlers;

import org.bukkit.block.Block;

import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;

@Deprecated
public abstract class BlockTicker extends ItemHandler {
	
	public boolean unique = true;

	public void update() {
		if (unique) {
			uniqueTick();
			unique = false;
		}
	}
	

	public abstract boolean isSynchronized();
	public abstract void uniqueTick();
	public abstract void tick(Block b, SlimefunItem item, Config data);

	@Override
	public String toCodename() {
		return "BlockTicker";
	}

}
