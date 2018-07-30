package me.mrCookieSlime.Slimefun.Objects.tasks;

import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.handlers.BlockTicker;
import me.przemovi.util.WoolColor;

import org.bukkit.Material;
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
		// b.setData((byte) data[index], false);
		if (b.getType().equals(Material.WHITE_WOOL) || b.getType().equals(Material.ORANGE_WOOL) || b.getType().equals(Material.MAGENTA_WOOL) || b.getType().equals(Material.LIGHT_BLUE_WOOL) || b.getType().equals(Material.YELLOW_WOOL) || b.getType().equals(Material.LIME_WOOL) || b.getType().equals(Material.PINK_WOOL) || b.getType().equals(Material.GRAY_WOOL) || b.getType().equals(Material.LIGHT_GRAY_WOOL) || b.getType().equals(Material.CYAN_WOOL) || b.getType().equals(Material.PURPLE_WOOL)
				|| b.getType().equals(Material.BLUE_WOOL) || b.getType().equals(Material.BROWN_WOOL) || b.getType().equals(Material.GREEN_WOOL) || b.getType().equals(Material.RED_WOOL) || b.getType().equals(Material.BLACK_WOOL)) {
			b.setType(wc.getColoredWool(data[index]));
		} else if (b.getType().equals(Material.WHITE_STAINED_GLASS) || b.getType().equals(Material.ORANGE_STAINED_GLASS) || b.getType().equals(Material.MAGENTA_STAINED_GLASS) || b.getType().equals(Material.LIGHT_BLUE_STAINED_GLASS) || b.getType().equals(Material.YELLOW_STAINED_GLASS) || b.getType().equals(Material.LIME_STAINED_GLASS) || b.getType().equals(Material.PINK_STAINED_GLASS) || b.getType().equals(Material.GRAY_STAINED_GLASS) || b.getType().equals(Material.LIGHT_GRAY_STAINED_GLASS)
				|| b.getType().equals(Material.CYAN_STAINED_GLASS) || b.getType().equals(Material.PURPLE_STAINED_GLASS) || b.getType().equals(Material.BLUE_STAINED_GLASS) || b.getType().equals(Material.BROWN_STAINED_GLASS) || b.getType().equals(Material.GREEN_STAINED_GLASS) || b.getType().equals(Material.RED_STAINED_GLASS) || b.getType().equals(Material.BLACK_STAINED_GLASS)) {
			b.setType(wc.getColoredStainedGlass(data[index]));
		} else if (b.getType().equals(Material.WHITE_STAINED_GLASS_PANE) || b.getType().equals(Material.ORANGE_STAINED_GLASS_PANE) || b.getType().equals(Material.MAGENTA_STAINED_GLASS_PANE) || b.getType().equals(Material.LIGHT_BLUE_STAINED_GLASS_PANE) || b.getType().equals(Material.YELLOW_STAINED_GLASS_PANE) || b.getType().equals(Material.LIME_STAINED_GLASS_PANE) || b.getType().equals(Material.PINK_STAINED_GLASS_PANE) || b.getType().equals(Material.GRAY_STAINED_GLASS_PANE)
				|| b.getType().equals(Material.LIGHT_GRAY_STAINED_GLASS_PANE) || b.getType().equals(Material.CYAN_STAINED_GLASS_PANE) || b.getType().equals(Material.PURPLE_STAINED_GLASS_PANE) || b.getType().equals(Material.BLUE_STAINED_GLASS_PANE) || b.getType().equals(Material.BROWN_STAINED_GLASS_PANE) || b.getType().equals(Material.GREEN_STAINED_GLASS_PANE) || b.getType().equals(Material.RED_STAINED_GLASS_PANE) || b.getType().equals(Material.BLACK_STAINED_GLASS_PANE)) {
			b.setType(wc.getColoredStainedGlassPane(data[index]));
		} else if (b.getType().equals(Material.WHITE_TERRACOTTA) || b.getType().equals(Material.ORANGE_TERRACOTTA) || b.getType().equals(Material.MAGENTA_TERRACOTTA) || b.getType().equals(Material.LIGHT_BLUE_TERRACOTTA) || b.getType().equals(Material.YELLOW_TERRACOTTA) || b.getType().equals(Material.LIME_TERRACOTTA) || b.getType().equals(Material.PINK_TERRACOTTA) || b.getType().equals(Material.GRAY_TERRACOTTA)
				|| b.getType().equals(Material.LIGHT_GRAY_TERRACOTTA) || b.getType().equals(Material.CYAN_TERRACOTTA) || b.getType().equals(Material.PURPLE_TERRACOTTA) || b.getType().equals(Material.BLUE_TERRACOTTA) || b.getType().equals(Material.BROWN_TERRACOTTA) || b.getType().equals(Material.GREEN_TERRACOTTA) || b.getType().equals(Material.RED_TERRACOTTA) || b.getType().equals(Material.BLACK_TERRACOTTA)) {
			b.setType(wc.getColoredTerracotta(data[index]));
		}
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
