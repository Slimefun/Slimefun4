package io.github.thebusybiscuit.slimefun4.core.attributes;

import org.bukkit.entity.Bee;

/**
 * Represents the {@link ProtectionType} that a {@link ProtectiveArmor}
 * prevents the damage from.
 *
 * @author Linox
 *
 * @see ProtectiveArmor
 *
 */
public enum ProtectionType {

    /**
     * This damage type represents damage inflicted by {@link Radioactive} materials.
     */
    RADIATION,

    /**
     * This damage type represents damage caused by a {@link Bee}
     */
    BEES,

    /**
     * This damage type represents damage caused by flying into a wall with an elytra
     */
    FLYING_INTO_WALL;

}
