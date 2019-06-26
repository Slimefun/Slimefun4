package me.mrCookieSlime.Slimefun.holograms;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;

import me.mrCookieSlime.CSCoreLibPlugin.general.World.ArmorStandFactory;
import me.mrCookieSlime.Slimefun.SlimefunStartup;


public class ReactorHologram {

    public static ArmorStand getArmorStand(Location reactor) {
        Location l = new Location(reactor.getWorld(), reactor.getX() + 0.5, reactor.getY() + 0.7, reactor.getZ() + 0.5);

        for (Entity n : l.getChunk().getEntities()) {
            if (n instanceof ArmorStand) {
                if (l.distanceSquared(n.getLocation()) < 0.4D) return (ArmorStand) n;
            }
        }

        ArmorStand hologram = ArmorStandFactory.createHidden(l);
        hologram.setCustomNameVisible(false);
        hologram.setCustomName(null);
        return hologram;
    }
    
    public static void update(final Location l, final String name) {
		Bukkit.getScheduler().scheduleSyncDelayedTask(SlimefunStartup.instance, () -> {
			ArmorStand hologram = getArmorStand(l);
			if (!hologram.isCustomNameVisible()) hologram.setCustomNameVisible(true);
			hologram.setCustomName(ChatColor.translateAlternateColorCodes('&', name));
		});
	}

    public static void remove(Location l) {
        ArmorStand hologram = getArmorStand(l);
        hologram.remove();
    }
}
