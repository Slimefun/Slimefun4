package io.github.thebusybiscuit.slimefun4.utils.holograms;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;

import me.mrCookieSlime.Slimefun.api.BlockStorage;

public final class HologramProjectorHologram {
	
	private HologramProjectorHologram() {}
	
	public static ArmorStand getArmorStand(Block projector, boolean createIfNoneExists) {
		String nametag = BlockStorage.getLocationInfo(projector.getLocation(), "text");
		double offset = Double.parseDouble(BlockStorage.getLocationInfo(projector.getLocation(), "offset"));
		Location l = new Location(projector.getWorld(), projector.getX() + 0.5, projector.getY() + offset, projector.getZ() + 0.5);
		
		for (Entity n : l.getChunk().getEntities()) {
			if (n instanceof ArmorStand && n.getCustomName() != null && n.getCustomName().equals(nametag) && l.distanceSquared(n.getLocation()) < 0.4D) {
				return (ArmorStand) n;
			}
		}
		
		if (!createIfNoneExists) {
			return null;
		}
		
		ArmorStand hologram = SimpleHologram.create(l);
		hologram.setCustomName(nametag);
		return hologram;
	}
	
	public static void remove(Block b) {
		ArmorStand hologram = getArmorStand(b, false);
		if (hologram != null) hologram.remove();
	}

}
