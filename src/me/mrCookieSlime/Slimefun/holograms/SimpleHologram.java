package me.mrCookieSlime.Slimefun.holograms;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;

import me.mrCookieSlime.CSCoreLibPlugin.general.World.ArmorStandFactory;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;

public final class SimpleHologram {
	
	private SimpleHologram() {}
	
	public static void update(final Block b, final String name) {
		Bukkit.getScheduler().scheduleSyncDelayedTask(SlimefunPlugin.instance, () -> {
			ArmorStand hologram = getArmorStand(b, true);
			hologram.setCustomName(ChatColor.translateAlternateColorCodes('&', name));
		});
	}	
	
	public static void remove(final Block b) {
		Bukkit.getScheduler().scheduleSyncDelayedTask(SlimefunPlugin.instance, () -> {
			ArmorStand hologram = getArmorStand(b, false);
			if (hologram != null) hologram.remove();
		});
	}
	
	private static ArmorStand getArmorStand(Block b, boolean createIfNoneExists) {
		Location l = new Location(b.getWorld(), b.getX() + 0.5, b.getY() + 0.7F, b.getZ() + 0.5);
		
		for (Entity n : l.getChunk().getEntities()) {
			if (n instanceof ArmorStand && n.getCustomName() != null && l.distanceSquared(n.getLocation()) < 0.4D) return (ArmorStand) n;
		}
		
		if (!createIfNoneExists) return null;
		else return ArmorStandFactory.createHidden(l);
	}

}
