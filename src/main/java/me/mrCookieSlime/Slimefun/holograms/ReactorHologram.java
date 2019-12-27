package me.mrCookieSlime.Slimefun.holograms;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;

import me.mrCookieSlime.Slimefun.api.Slimefun;

public final class ReactorHologram {
	
	private ReactorHologram() {}

    public static ArmorStand getArmorStand(Location reactor, boolean createIfNoneExists) {
        Location l = new Location(reactor.getWorld(), reactor.getX() + 0.5, reactor.getY() + 0.7, reactor.getZ() + 0.5);

        for (Entity n : l.getChunk().getEntities()) {
            if (n instanceof ArmorStand && l.distanceSquared(n.getLocation()) < 0.4D) {
            	return (ArmorStand) n;
            }
        }
        
        if (!createIfNoneExists) return null;

        ArmorStand hologram = SimpleHologram.create(l);
        hologram.setCustomNameVisible(false);
        hologram.setCustomName(null);
        return hologram;
    }
    
    public static void update(Location l, String name) {
		Slimefun.runSync(() -> {
			ArmorStand hologram = getArmorStand(l, true);
			if (!hologram.isCustomNameVisible()) hologram.setCustomNameVisible(true);
			hologram.setCustomName(ChatColor.translateAlternateColorCodes('&', name));
		});
	}
}
