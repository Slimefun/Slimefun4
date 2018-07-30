package me.mrCookieSlime.Slimefun.Objects.tasks;

import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.handlers.BlockTicker;
import me.przemovi.util.ColoredBlock;
import org.bukkit.block.Block;

public class AdvancedRainbowTicker extends BlockTicker {

	public int index;
	public int[] data;

	public AdvancedRainbowTicker(int... data) {
		this.data = data;
		index = 0;
	}

	@Override
	public void tick(Block b, SlimefunItem item, Config cfg) {
		b.setType(ColoredBlock.fromBlockType(b.getType()).getColoredBlock(data[index]));
	}

	@Override
	public void uniqueTick() {
		index = index == data.length - 1 ? 0 : index + 1;
	}

	@Override
	public boolean isSynchronized() {
		return true;
	}

}
