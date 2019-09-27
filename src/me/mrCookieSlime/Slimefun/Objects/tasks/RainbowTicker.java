package me.mrCookieSlime.Slimefun.Objects.tasks;

import org.bukkit.block.Block;
import org.bukkit.block.data.Waterlogged;

import io.github.thebusybiscuit.cscorelib2.materials.MaterialCollections;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockTicker;

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
		if (MaterialCollections.contains(b.getType(), MaterialCollections.getAllWools())) {
			b.setType(MaterialCollections.getAllWools()[meta], false);
		} 
		else if (MaterialCollections.contains(b.getType(), MaterialCollections.getAllStainedGlassColors())) {
			b.setType(MaterialCollections.getAllStainedGlassColors()[meta], false);
		} 
		else if (MaterialCollections.contains(b.getType(), MaterialCollections.getAllStainedGlassPaneColors())){
			boolean waterlogged = ((Waterlogged) b.getBlockData()).isWaterlogged();
			b.setType(MaterialCollections.getAllStainedGlassPaneColors()[meta], true);
			
			if (waterlogged) {
				Waterlogged block = (Waterlogged) b.getBlockData();
				block.setWaterlogged(true);
				b.setBlockData(block);
			}
		} 
		else if (MaterialCollections.contains(b.getType(), MaterialCollections.getAllTerracottaColors())){
			b.setType(MaterialCollections.getAllTerracottaColors()[meta], false);
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
