package me.mrCookieSlime.Slimefun.Objects.tasks;

import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.handlers.BlockTicker;
import me.przemovi.util.WoolColor;

import org.bukkit.block.Block;

public class AdvancedRainbowTicker extends BlockTicker {
	
	public int index;
	public int[] data;
	WoolColor wc = new WoolColor();

	public AdvancedRainbowTicker(int... data) {
		this.data = data;
		index = 0;
	}

	@Override
	public void tick(Block b, SlimefunItem item, Config cfg) {
		//b.setData((byte) data[index], false);
		b.setType(wc.getColoredWool(data[index]));
	}

	@Override
	public void uniqueTick() {
		index = index == data.length - 1 ? 0: index + 1;
	}

	@Override
	public boolean isSynchronized() {
		return true;
	}

}
