package me.mrCookieSlime.Slimefun.holograms;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;

import me.mrCookieSlime.CSCoreLibPlugin.general.World.ArmorStandFactory;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;

public final class ReactorHologram {
	
	private ReactorHologram() {}

    public static ArmorStand getArmorStand(Location reactor, boolean createIfNoneExists) {
        Location l = new Location(reactor.getWorld(), reactor.getX() + 0.5, reactor.getY() + 0.7, reactor.getZ() + 0.5);

        for (Entity n : l.getChunk().getEntities()) {
            if (n instanceof ArmorStand && l.distanceSquared(n.getLocation()) < 0.4D) return (ArmorStand) n;
        }
        
        if (!createIfNoneExists) return null;

        ArmorStand hologram = ArmorStandFactory.createHidden(l);
        hologram.setCustomNameVisible(false);
        hologram.setCustomName(null);
        return hologram;
    }
    
    public static void update(final Location l, final String name) {
		Bukkit.getScheduler().scheduleSyncDelayedTask(SlimefunPlugin.instance, () -> {
			ArmorStand hologram = getArmorStand(l, true);
			if (!hologram.isCustomNameVisible()) hologram.setCustomNameVisible(true);
			hologram.setCustomName(ChatColor.translateAlternateColorCodes('&', name));
		});
	}

    public static void remove(Location l) {
    	Bukkit.getScheduler().scheduleSyncDelayedTask(SlimefunPlugin.instance, () -> {
            ArmorStand hologram = getArmorStand(l, false);
            if (hologram != null) hologram.remove();
		});
    }
}
