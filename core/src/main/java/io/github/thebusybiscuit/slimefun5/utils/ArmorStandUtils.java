package io.github.thebusybiscuit.slimefun5.utils;

import io.github.thebusybiscuit.slimefun5.utils.compatibility.ReflectionCompat;
import javax.annotation.Nonnull;

import io.papermc.lib.PaperLib;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;

import io.github.thebusybiscuit.slimefun5.core.services.holograms.HologramsService;
import io.github.thebusybiscuit.slimefun5.implementation.items.altar.AncientPedestal;
import io.github.thebusybiscuit.slimefun5.implementation.items.blocks.HologramProjector;

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

    private ArmorStandUtils() {}

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
        // The consumer method was moved from World to RegionAccessor in 1.20.2
        // Due to this, we need to use a rubbish workaround to support 1.20.1 and below
        // This causes flicker on these versions which sucks but not sure a better way around this right now.
        if (PaperLib.getMinecraftVersion() < 20 ||
                (PaperLib.getMinecraftVersion() == 20 && PaperLib.getMinecraftPatchVersion() < 2)) {
            ArmorStand armorStand = location.getWorld().spawn(location, ArmorStand.class);
            setupArmorStand(armorStand);
            return armorStand;
        }

        // The pre-spawn consumer overload of World#spawn is 1.11+; reached reflectively to keep its
        // flicker-free behaviour on modern servers (this branch only runs on 1.20.2+).
        java.util.function.Consumer<ArmorStand> setup = ArmorStandUtils::setupArmorStand;
        Object spawned = ReflectionCompat.invoke(location.getWorld(), "spawn", location, ArmorStand.class, setup);

        if (spawned instanceof ArmorStand) {
            return (ArmorStand) spawned;
        }

        ArmorStand fallback = location.getWorld().spawn(location, ArmorStand.class);
        setupArmorStand(fallback);
        return fallback;
    }

    private static void setupArmorStand(ArmorStand armorStand) {
        armorStand.setVisible(false);
        ReflectionCompat.invoke(armorStand, "setSilent", true);
        armorStand.setMarker(true);
        armorStand.setGravity(false);
        armorStand.setBasePlate(false);
        armorStand.setRemoveWhenFarAway(false);
    }
}

