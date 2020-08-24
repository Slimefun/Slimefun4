package io.github.thebusybiscuit.slimefun4.utils.holograms;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import io.github.thebusybiscuit.cscorelib2.chat.ChatColors;
import me.mrCookieSlime.Slimefun.api.Slimefun;

/**
 * This utility class provides a few static methods for modifying a simple Text-based Hologram.
 * 
 * @author TheBusyBiscuit
 *
 */
public final class SimpleHologram {

    private SimpleHologram() {}

    public static void update(Block b, String name) {
        Slimefun.runSync(() -> {
            ArmorStand hologram = getArmorStand(b, true);
            hologram.setCustomName(ChatColors.color(name));
        });
    }

    public static void remove(Block b) {
        Slimefun.runSync(() -> {
            ArmorStand hologram = getArmorStand(b, false);

            if (hologram != null) {
                hologram.remove();
            }
        });
    }

    private static ArmorStand getArmorStand(Block b, boolean createIfNoneExists) {
        Location l = new Location(b.getWorld(), b.getX() + 0.5, b.getY() + 0.7F, b.getZ() + 0.5);

        for (Entity n : l.getChunk().getEntities()) {
            if (n instanceof ArmorStand && l.distanceSquared(n.getLocation()) < 0.4D && isPossibleHologram((ArmorStand) n)) {
                return (ArmorStand) n;
            }
        }

        if (!createIfNoneExists) {
            return null;
        }
        else {
            return create(l);
        }
    }

    private static boolean isPossibleHologram(ArmorStand armorstand) {
        return armorstand.isValid() && armorstand.isSilent() && armorstand.isMarker() && !armorstand.hasGravity() && armorstand.isCustomNameVisible();
    }

    public static ArmorStand create(Location l) {
        ArmorStand armorStand = (ArmorStand) l.getWorld().spawnEntity(l, EntityType.ARMOR_STAND);
        armorStand.setVisible(false);
        armorStand.setSilent(true);
        armorStand.setMarker(true);
        armorStand.setGravity(false);
        armorStand.setBasePlate(false);
        armorStand.setCustomNameVisible(true);
        armorStand.setRemoveWhenFarAway(false);
        return armorStand;
    }

}
