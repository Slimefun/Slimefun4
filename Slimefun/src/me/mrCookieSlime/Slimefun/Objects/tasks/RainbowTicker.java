package me.mrCookieSlime.Slimefun.Objects.tasks;

import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.handlers.BlockTicker;
import me.przemovi.util.WoolColor;

import org.bukkit.Material;
import org.bukkit.block.Block;

public class RainbowTicker extends BlockTicker {
	
	public int meta;
	public int index;
	public int[] queue;
	WoolColor wc = new WoolColor();

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
		//b.setData((byte) meta, false);
		b.setType(wc.getColoredWool(meta));
		
		if (b.getType().equals(Material.WHITE_WOOL) || b.getType().equals(Material.ORANGE_WOOL) || b.getType().equals(Material.MAGENTA_WOOL) || b.getType().equals(Material.LIGHT_BLUE_WOOL) || b.getType().equals(Material.YELLOW_WOOL) || b.getType().equals(Material.LIME_WOOL) || b.getType().equals(Material.PINK_WOOL) || b.getType().equals(Material.GRAY_WOOL) || b.getType().equals(Material.LIGHT_GRAY_WOOL) || b.getType().equals(Material.CYAN_WOOL) || b.getType().equals(Material.PURPLE_WOOL)
				|| b.getType().equals(Material.BLUE_WOOL) || b.getType().equals(Material.BROWN_WOOL) || b.getType().equals(Material.GREEN_WOOL) || b.getType().equals(Material.RED_WOOL) || b.getType().equals(Material.BLACK_WOOL)) {
			b.setType(wc.getColoredWool(meta));
		} else if (b.getType().equals(Material.WHITE_STAINED_GLASS) || b.getType().equals(Material.ORANGE_STAINED_GLASS) || b.getType().equals(Material.MAGENTA_STAINED_GLASS) || b.getType().equals(Material.LIGHT_BLUE_STAINED_GLASS) || b.getType().equals(Material.YELLOW_STAINED_GLASS) || b.getType().equals(Material.LIME_STAINED_GLASS) || b.getType().equals(Material.PINK_STAINED_GLASS) || b.getType().equals(Material.GRAY_STAINED_GLASS) || b.getType().equals(Material.LIGHT_GRAY_STAINED_GLASS)
				|| b.getType().equals(Material.CYAN_STAINED_GLASS) || b.getType().equals(Material.PURPLE_STAINED_GLASS) || b.getType().equals(Material.BLUE_STAINED_GLASS) || b.getType().equals(Material.BROWN_STAINED_GLASS) || b.getType().equals(Material.GREEN_STAINED_GLASS) || b.getType().equals(Material.RED_STAINED_GLASS) || b.getType().equals(Material.BLACK_STAINED_GLASS)) {
			b.setType(wc.getColoredStainedGlass(meta));
		} else if (b.getType().equals(Material.WHITE_STAINED_GLASS_PANE) || b.getType().equals(Material.ORANGE_STAINED_GLASS_PANE) || b.getType().equals(Material.MAGENTA_STAINED_GLASS_PANE) || b.getType().equals(Material.LIGHT_BLUE_STAINED_GLASS_PANE) || b.getType().equals(Material.YELLOW_STAINED_GLASS_PANE) || b.getType().equals(Material.LIME_STAINED_GLASS_PANE) || b.getType().equals(Material.PINK_STAINED_GLASS_PANE) || b.getType().equals(Material.GRAY_STAINED_GLASS_PANE)
				|| b.getType().equals(Material.LIGHT_GRAY_STAINED_GLASS_PANE) || b.getType().equals(Material.CYAN_STAINED_GLASS_PANE) || b.getType().equals(Material.PURPLE_STAINED_GLASS_PANE) || b.getType().equals(Material.BLUE_STAINED_GLASS_PANE) || b.getType().equals(Material.BROWN_STAINED_GLASS_PANE) || b.getType().equals(Material.GREEN_STAINED_GLASS_PANE) || b.getType().equals(Material.RED_STAINED_GLASS_PANE) || b.getType().equals(Material.BLACK_STAINED_GLASS_PANE)) {
			b.setType(wc.getColoredStainedGlassPane(meta));
		} else if (b.getType().equals(Material.WHITE_TERRACOTTA) || b.getType().equals(Material.ORANGE_TERRACOTTA) || b.getType().equals(Material.MAGENTA_TERRACOTTA) || b.getType().equals(Material.LIGHT_BLUE_TERRACOTTA) || b.getType().equals(Material.YELLOW_TERRACOTTA) || b.getType().equals(Material.LIME_TERRACOTTA) || b.getType().equals(Material.PINK_TERRACOTTA) || b.getType().equals(Material.GRAY_TERRACOTTA)
				|| b.getType().equals(Material.LIGHT_GRAY_TERRACOTTA) || b.getType().equals(Material.CYAN_TERRACOTTA) || b.getType().equals(Material.PURPLE_TERRACOTTA) || b.getType().equals(Material.BLUE_TERRACOTTA) || b.getType().equals(Material.BROWN_TERRACOTTA) || b.getType().equals(Material.GREEN_TERRACOTTA) || b.getType().equals(Material.RED_TERRACOTTA) || b.getType().equals(Material.BLACK_TERRACOTTA)) {
			b.setType(wc.getColoredStainedGlassPane(meta));
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
