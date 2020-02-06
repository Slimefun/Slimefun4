package io.github.thebusybiscuit.slimefun4.implementation.tasks;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Waterlogged;

import io.github.thebusybiscuit.cscorelib2.collections.LoopIterator;
import io.github.thebusybiscuit.cscorelib2.materials.MaterialCollection;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockTicker;

public class RainbowTicker extends BlockTicker {

	private final LoopIterator<Material> iterator;
	private Material material;

	public RainbowTicker(Material... materials) {
		iterator = new LoopIterator<>(Arrays.asList(materials));
		material = iterator.next();
	}

	public RainbowTicker(MaterialCollection collection) {
		this(collection.getAsArray());
	}

	@Override
	public void tick(Block b, SlimefunItem item, Config data) {
		BlockData blockData = b.getBlockData();
		boolean waterlogged = blockData instanceof Waterlogged && ((Waterlogged) blockData).isWaterlogged();
		
		b.setType(material, true);
		
		if (waterlogged) {
			Waterlogged block = (Waterlogged) b.getBlockData();
			block.setWaterlogged(true);
			b.setBlockData(block);
		}
	}

	@Override
	public void uniqueTick() {
		material = iterator.next();
	}

	@Override
	public boolean isSynchronized() {
		return true;
	}

}
