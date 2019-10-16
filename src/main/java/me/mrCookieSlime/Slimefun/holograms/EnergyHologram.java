package me.mrCookieSlime.Slimefun.holograms;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;

import io.github.thebusybiscuit.cscorelib2.math.DoubleHandler;
import me.mrCookieSlime.CSCoreLibPlugin.general.World.ArmorStandFactory;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;

public final class EnergyHologram {
	
	private EnergyHologram() {}
	
	public static void update(Block b, double supply, double demand) {
		update(b, demand > supply ? ("&4&l- &c" + DoubleHandler.getFancyDouble(Math.abs(supply - demand)) + " &7J &e\u26A1"): ("&2&l+ &a" + DoubleHandler.getFancyDouble(supply - demand) + " &7J &e\u26A1"));
	}
	
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
