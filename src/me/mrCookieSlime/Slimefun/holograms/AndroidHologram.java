package me.mrCookieSlime.Slimefun.holograms;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;

import me.mrCookieSlime.CSCoreLibPlugin.general.World.ArmorStandFactory;

public final class AndroidHologram {
	
	private AndroidHologram() {}
	
	private static final double offset = 1.2;
	
	public static void update(final Block b, final String name) {
		ArmorStand hologram = getArmorStand(b, true);
		hologram.setCustomName(name);
	}
	
	public static void remove(final Block b) {
		ArmorStand hologram = getArmorStand(b, false);
		if (hologram != null) hologram.remove();
	}
	
	public static List<Entity> getNearbyEntities(final Block b, double radius) {
		ArmorStand hologram = getArmorStand(b, true);
		return hologram.getNearbyEntities(radius, 1D, radius);
	}
	
	public static List<Entity> getNearbyEntities(final Block b, double radius, double y) {
		ArmorStand hologram = getArmorStand(b, true);
		return hologram.getNearbyEntities(radius, y, radius);
	}
	
	private static ArmorStand getArmorStand(Block b, boolean createIfNoneExists) {
		Location l = new Location(b.getWorld(), b.getX() + 0.5, b.getY() + offset, b.getZ() + 0.5);
		
		for (Entity n: l.getChunk().getEntities()) {
			if (n instanceof ArmorStand && n.getCustomName() == null && l.distanceSquared(n.getLocation()) < 0.4D) return (ArmorStand) n;
		}
		
		if (!createIfNoneExists) return null;
		
		ArmorStand hologram = ArmorStandFactory.createHidden(l);
		hologram.setCustomNameVisible(false);
		hologram.setCustomName(null);
		return hologram;
	}

}
