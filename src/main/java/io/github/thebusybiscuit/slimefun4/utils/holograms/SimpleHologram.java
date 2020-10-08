package io.github.thebusybiscuit.slimefun4.utils.holograms;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import io.github.thebusybiscuit.cscorelib2.chat.ChatColors;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;

/**
 * This utility class provides a few static methods for modifying a simple Text-based Hologram.
 * 
 * @author TheBusyBiscuit
 *
 */
public final class SimpleHologram {

    private SimpleHologram() {}

    public static void update(@Nonnull Block b, @Nonnull String name) {
        SlimefunPlugin.runSync(() -> {
            ArmorStand hologram = getArmorStand(b, true);
            hologram.setCustomName(ChatColors.color(name));
        });
    }

    public static void remove(@Nonnull Block b) {
        SlimefunPlugin.runSync(() -> {
            ArmorStand hologram = getArmorStand(b, false);

            if (hologram != null) {
                hologram.remove();
            }
        });
    }

    @Nullable
    private static ArmorStand getArmorStand(@Nonnull Block b, boolean createIfNoneExists) {
        Location l = new Location(b.getWorld(), b.getX() + 0.5, b.getY() + 0.7F, b.getZ() + 0.5);

        for (Entity n : l.getChunk().getEntities()) {
            if (n instanceof ArmorStand && l.distanceSquared(n.getLocation()) < 0.4D && isPossibleHologram((ArmorStand) n)) {
                return (ArmorStand) n;
            }
        }

        if (!createIfNoneExists) {
            return null;
        } else {
            return create(l);
        }
    }

    private static boolean isPossibleHologram(@Nonnull ArmorStand armorstand) {
        return armorstand.isValid() && armorstand.isSilent() && armorstand.isMarker() && !armorstand.hasGravity() && armorstand.isCustomNameVisible();
    }

    @Nonnull
    public static ArmorStand create(@Nonnull Location l) {
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
