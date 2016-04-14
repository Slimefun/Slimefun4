package me.mrCookieSlime.Slimefun.holograms;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;

public class AndroidStatusHologram {
	
	private static final double offset = 1.2;
	
	public static void update(final Block b, final String name) {
		ArmorStand hologram = getArmorStand(b);
		hologram.setCustomName(name);
	}
	
	public static void remove(final Block b) {
		ArmorStand hologram = getArmorStand(b);
		hologram.remove();
	}
	
	public static List<Entity> getNearbyEntities(final Block b, double radius) {
		ArmorStand hologram = getArmorStand(b);
		return hologram.getNearbyEntities(radius, 1D, radius);
	}
	
	public static List<Entity> getNearbyEntities(final Block b, double radius, double y) {
		ArmorStand hologram = getArmorStand(b);
		return hologram.getNearbyEntities(radius, y, radius);
	}
	
	private static ArmorStand getArmorStand(Block b) {
		Location l = new Location(b.getWorld(), b.getX() + 0.5, b.getY() + offset, b.getZ() + 0.5);
		
		for (Entity n: l.getChunk().getEntities()) {
			if (n instanceof ArmorStand) {
				if (n.getCustomName() == null && l.distanceSquared(n.getLocation()) < 0.4D) return (ArmorStand) n;
			}
		}
		
		ArmorStand hologram = ArmorStandFactory.createHidden(l);
		hologram.setCustomNameVisible(false);
		hologram.setCustomName(null);
		return hologram;
	}

}
