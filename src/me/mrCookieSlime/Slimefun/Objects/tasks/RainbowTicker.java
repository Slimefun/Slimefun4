package me.mrCookieSlime.Slimefun.Objects.tasks;

import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.CSCoreLibPlugin.compatibility.MaterialHook;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.handlers.BlockTicker;

import org.bukkit.block.Block;

public class RainbowTicker extends BlockTicker {
	
	public int meta;
	public int index;
	public int[] queue;

	public RainbowTicker() {
		this(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15);
	}

	public RainbowTicker(int... data) {
		this.queue = data;
		meta = data[0];
		index = 0;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void tick(Block b, SlimefunItem item, Config data) {
		if (MaterialHook.isWool(b.getType())){
			b.setType(MaterialHook.WoolColours[meta], false);
		}else if (MaterialHook.isStainedGlass(b.getType())) {
			b.setType(MaterialHook.StainedGlassColours[meta], false);
		}else if (MaterialHook.isStainedGlassPane(b.getType())){
			b.setType(MaterialHook.StainedGlassPaneColours[meta], true);
		}else if (MaterialHook.isTerracotta(b.getType())){
			b.setType(MaterialHook.TerracottaColours[meta], false);
		}
	}

	@Override
	public void uniqueTick() {
		index = ((index == queue.length - 1) ? 0: index + 1);
		meta = queue[index];
	}

	@Override
	public boolean isSynchronized() {
		return true;
	}

}
