package me.mrCookieSlime.Slimefun.Objects.tasks;

import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.handlers.BlockTicker;
import me.przemovi.util.ColoredBlock;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;

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

	@Override
	public void tick(Block b, SlimefunItem item, Config data) {
		b.setType(ColoredBlock.fromBlockType(b.getType()).getColoredBlock(meta));
		if (b.getType().name().contains("PANE")) {
			if (b.getLocation().getWorld().getBlockAt(b.getLocation().getBlockX() + 1, b.getLocation().getBlockY(), b.getLocation().getBlockZ()) != null) {
				Block b1 = b.getLocation().getWorld().getBlockAt(b.getLocation().getBlockX() + 1, b.getLocation().getBlockY(), b.getLocation().getBlockZ());
				if (b1.getType() != null && b1.getType() != Material.AIR && b1.getType() != Material.PLAYER_HEAD && !b1.isLiquid() && !b1.getType().name().contains("PANE")) {
					BlockData blockdata = b1.getBlockData();
					b1.setType(Material.AIR);
					b1.setBlockData(blockdata);
				}
			}
			if (b.getLocation().getWorld().getBlockAt(b.getLocation().getBlockX() - 1, b.getLocation().getBlockY(), b.getLocation().getBlockZ()) != null) {
				Block b1 = b.getLocation().getWorld().getBlockAt(b.getLocation().getBlockX() - 1, b.getLocation().getBlockY(), b.getLocation().getBlockZ());
				if (b1.getType() != null && b1.getType() != Material.AIR && b1.getType() != Material.PLAYER_HEAD && !b1.isLiquid() && !b1.getType().name().contains("PANE")) {
					BlockData blockdata = b1.getBlockData();
					b1.setType(Material.AIR);
					b1.setBlockData(blockdata);
				}
			}
			if (b.getLocation().getWorld().getBlockAt(b.getLocation().getBlockX(), b.getLocation().getBlockY(), b.getLocation().getBlockZ() + 1) != null) {
				Block b1 = b.getLocation().getWorld().getBlockAt(b.getLocation().getBlockX(), b.getLocation().getBlockY(), b.getLocation().getBlockZ() + 1);
				if (b1.getType() != null && b1.getType() != Material.AIR && b1.getType() != Material.PLAYER_HEAD && !b1.isLiquid() && !b1.getType().name().contains("PANE")) {
					BlockData blockdata = b1.getBlockData();
					b1.setType(Material.AIR);
					b1.setBlockData(blockdata);
				}
			}
			if (b.getLocation().getWorld().getBlockAt(b.getLocation().getBlockX(), b.getLocation().getBlockY(), b.getLocation().getBlockZ() - 1) != null) {
				Block b1 = b.getLocation().getWorld().getBlockAt(b.getLocation().getBlockX(), b.getLocation().getBlockY(), b.getLocation().getBlockZ() - 1);
				if (b1.getType() != null && b1.getType() != Material.AIR && b1.getType() != Material.PLAYER_HEAD && !b1.isLiquid() && !b1.getType().name().contains("PANE")) {
					BlockData blockdata = b1.getBlockData();
					b1.setType(Material.AIR);
					b1.setBlockData(blockdata);
				}
			}
		}
	}

	@Override
	public void uniqueTick() {
		index = ((index == queue.length - 1) ? 0 : index + 1);
		meta = queue[index];
	}

	@Override
	public boolean isSynchronized() {
		return true;
	}

}
