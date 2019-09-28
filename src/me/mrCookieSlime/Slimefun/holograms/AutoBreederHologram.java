package me.mrCookieSlime.Slimefun.holograms;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;

import me.mrCookieSlime.CSCoreLibPlugin.general.World.ArmorStandFactory;

public final class AutoBreederHologram {
	
	private AutoBreederHologram() {}
	
	public static ArmorStand getArmorStand(Block hopper, boolean createIfNonExists) {
		Location l = new Location(hopper.getWorld(), hopper.getX() + 0.5, hopper.getY(), hopper.getZ() + 0.5);
		
		for (Entity n: l.getChunk().getEntities()) {
			if (n instanceof ArmorStand && n.getCustomName() == null && l.distanceSquared(n.getLocation()) < 0.4D) return (ArmorStand) n;
		}
		
		if (!createIfNonExists) return null;
		
		ArmorStand hologram = ArmorStandFactory.createHidden(l);
		hologram.setCustomNameVisible(false);
		hologram.setCustomName(null);
		return hologram;
	}

	public static void remove(Block b) {
		ArmorStand hologram = getArmorStand(b, false);
		if (hologram != null) hologram.remove();
	}

}
