package me.mrCookieSlime.Slimefun.holograms;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;

import me.mrCookieSlime.CSCoreLibPlugin.general.World.ArmorStandFactory;

public class AutoBreeder {
	
	public static ArmorStand getArmorStand(Block hopper) {
		Location l = new Location(hopper.getWorld(), hopper.getX() + 0.5, hopper.getY(), hopper.getZ() + 0.5);
		
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
