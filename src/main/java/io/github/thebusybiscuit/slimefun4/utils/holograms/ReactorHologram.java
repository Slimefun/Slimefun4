package io.github.thebusybiscuit.slimefun4.utils.holograms;

import java.util.Collection;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;

import io.github.thebusybiscuit.cscorelib2.chat.ChatColors;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;

public final class ReactorHologram {

    private ReactorHologram() {}

    @Nullable
    public static ArmorStand getArmorStand(@Nonnull Location reactor, boolean createIfNoneExists) {
        Location l = new Location(reactor.getWorld(), reactor.getX() + 0.5, reactor.getY() + 0.7, reactor.getZ() + 0.5);
        Collection<Entity> holograms = l.getWorld().getNearbyEntities(l, 0.2, 0.2, 0.2, ReactorHologram::isPossibleHologram);

        for (Entity n : holograms) {
            if (n instanceof ArmorStand) {
                return (ArmorStand) n;
            }
        }

        if (!createIfNoneExists) {
            return null;
        }

        ArmorStand hologram = SimpleHologram.create(l);
        hologram.setCustomNameVisible(false);
        hologram.setCustomName(null);
        return hologram;
    }

    private static boolean isPossibleHologram(@Nonnull Entity n) {
        if (n instanceof ArmorStand) {
            ArmorStand armorstand = (ArmorStand) n;
            return armorstand.isValid() && armorstand.isSilent() && armorstand.isMarker() && !armorstand.hasGravity();
        } else {
            return false;
        }

    }

    public static void update(@Nonnull Location l, @Nonnull String name) {
        SlimefunPlugin.runSync(() -> {
            ArmorStand hologram = getArmorStand(l, true);

            hologram.setCustomNameVisible(true);
            hologram.setCustomName(ChatColors.color(name));
        });
    }
}
