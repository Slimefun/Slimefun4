package io.github.thebusybiscuit.slimefun4.core.attributes;

import org.bukkit.entity.Bee;

/**
 * Represents the {@link ProtectionType} that a {@link ProtectiveArmor}
 * prevents the damage from.
 *
 * @author Linox
 * @author Seggan
 *
 * @see ProtectiveArmor
 *
 */
@SuppressWarnings("InstantiationOfUtilityClass")
public final class ProtectionType {

    /**
     * This damage type represents damage inflicted by {@link Radioactive} materials.
     */
    public static final ProtectionType RADIATION = new ProtectionType();

    /**
     * This damage type represents damage caused by a {@link Bee}
     */
    public static final ProtectionType BEES = new ProtectionType();

    /**
     * This damage type represents damage caused by flying into a wall with an elytra
     */
    public static final ProtectionType FLYING_INTO_WALL = new ProtectionType();

}
