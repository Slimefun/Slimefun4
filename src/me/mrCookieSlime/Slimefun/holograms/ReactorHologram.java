package me.mrCookieSlime.Slimefun.holograms;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;

/**
 * Created by John on 20.11.2016.
 */
public class ReactorHologram {

    public static ArmorStand getArmorStand(Location reactor) {
        Location l = new Location(reactor.getWorld(), reactor.getX() + 0.5, reactor.getY(), reactor.getZ() + 0.5);

        for (Entity n : l.getChunk().getEntities()) {
            if (n instanceof ArmorStand) {
                if (n.getCustomName() == null && l.distanceSquared(n.getLocation()) < 0.4D) return (ArmorStand) n;
            }
        }

        ArmorStand hologram = ArmorStandFactory.createHidden(l);
        hologram.setCustomNameVisible(false);
        hologram.setCustomName(null);
        return hologram;
    }

    public static void remove(Location l) {
        ArmorStand hologram = getArmorStand(l);
        hologram.remove();
    }
}
