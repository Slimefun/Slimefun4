package io.github.thebusybiscuit.slimefun4.utils;

import javax.annotation.Nonnull;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;

import io.github.thebusybiscuit.slimefun4.core.services.holograms.HologramsService;
import io.github.thebusybiscuit.slimefun4.implementation.items.altar.AncientPedestal;
import io.github.thebusybiscuit.slimefun4.implementation.items.blocks.HologramProjector;

/**
 * This class holds utilities for {@link ArmorStand}, useful for classes
 * dealing with {@link ArmorStand}s that are not from {@link HologramsService}
 *
 * @see HologramProjector
 * @see AncientPedestal
 *
 * @author JustAHuman
 */
public class ArmorStandUtils {

    /**
     * Spawns an {@link ArmorStand} at the given {@link Location} with the given custom name
     * <br>
     * Set Properties: Invisible, Silent, Marker, No-Gravity, No Base Plate, Don't Remove When Far Away
     *
     * @param location The {@link Location} to spawn the {@link ArmorStand}
     * @param customName The {@link String} custom name the {@link ArmorStand} should display
     *
     * @return The spawned {@link ArmorStand}
     */
    public static @Nonnull ArmorStand spawnArmorStand(@Nonnull Location location, @Nonnull String customName) {
        ArmorStand armorStand = spawnArmorStand(location);
        armorStand.setCustomName(customName);
        armorStand.setCustomNameVisible(true);
        return armorStand;
    }
    
    /**
     * Spawns an {@link ArmorStand} at the given {@link Location}
     * <br>
     * Set Properties: Invisible, Silent, Marker, No-Gravity, No Base Plate, Don't Remove When Far Away
     *
     * @param location The {@link Location} to spawn the {@link ArmorStand}
     *
     * @return The spawned {@link ArmorStand}
     */
    public static @Nonnull ArmorStand spawnArmorStand(@Nonnull Location location) {
        return location.getWorld().spawn(location, ArmorStand.class, armorStand -> {
            armorStand.setVisible(false);
            armorStand.setSilent(true);
            armorStand.setMarker(true);
            armorStand.setGravity(false);
            armorStand.setBasePlate(false);
            armorStand.setRemoveWhenFarAway(false);
        });
    }
}
