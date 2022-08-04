package io.github.thebusybiscuit.slimefun4.utils;

import org.apache.commons.lang.Validate;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;

import javax.annotation.Nonnull;

public class ArmorStandUtils {

    /**
     * This method returns a template {@link ArmorStand} that has all of its
     * values set.
     *
     * @param l
     *            The {@link Location} to spawn the Armor Stand at
     * @return the {@link ArmorStand}
     */
    public static @Nonnull ArmorStand spawnArmorStand(@Nonnull Location location) {
        Validate.notNull(location, "Location cannot be null!");

        ArmorStand armorStand = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
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
