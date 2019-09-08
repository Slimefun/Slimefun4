package me.mrCookieSlime.Slimefun.Objects.tasks;

import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.CSCoreLibPlugin.compatibility.MaterialHelper;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockTicker;

import org.bukkit.block.Block;
import org.bukkit.block.data.Waterlogged;

public class RainbowTicker extends BlockTicker {
	
	private int meta;
	private int index;
	private int[] queue;

	public RainbowTicker() {
		this(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15);
	}

	public RainbowTicker(int... data) {
		this.queue = data;
		meta = data[0];
		index = 0;
	}

	@Override
	public void tick(Block b, SlimefunItem item, Config data) {
		if (MaterialHelper.isWool(b.getType())) {
			b.setType(MaterialHelper.WoolColours[meta], false);
		} 
		else if (MaterialHelper.isStainedGlass(b.getType())) {
			b.setType(MaterialHelper.StainedGlassColours[meta], false);
		} 
		else if (MaterialHelper.isStainedGlassPane(b.getType())){
			boolean waterlogged = ((Waterlogged) b.getBlockData()).isWaterlogged();
			b.setType(MaterialHelper.StainedGlassPaneColours[meta], true);
			
			if (waterlogged) {
				Waterlogged block = (Waterlogged) b.getBlockData();
				block.setWaterlogged(true);
				b.setBlockData(block);
			}
		} 
		else if (MaterialHelper.isTerracotta(b.getType())){
			b.setType(MaterialHelper.TerracottaColours[meta], false);
		}
	}

	@Override
	public void uniqueTick() {
		index = ((index >= queue.length - 1) ? 0: index + 1);
		meta = queue[index];
	}

	@Override
	public boolean isSynchronized() {
		return true;
	}

}
