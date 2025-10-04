package io.github.thebusybiscuit.slimefun4.utils.compatibility;

import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.entity.EntityType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

// https://hub.spigotmc.org/stash/projects/SPIGOT/repos/craftbukkit/browse/src/main/java/org/bukkit/craftbukkit/legacy/FieldRename.java?until=2a6207fe150b6165722fce94c83cc1f206620ab5&untilPath=src%2Fmain%2Fjava%2Forg%2Fbukkit%2Fcraftbukkit%2Flegacy%2FFieldRename.java#158-193
public class VersionedEntityType {

    public static final EntityType MOOSHROOM;
    public static final EntityType SNOW_GOLEM;
    public static final EntityType FIREWORK;
    public static final EntityType ARMADILLO;
    public static final EntityType BOGGED;
    public static final EntityType BREEZE;
    public static final EntityType BREEZE_WIND_CHARGE;
    public static final EntityType CREAKING;
    public static final EntityType WIND_CHARGE;

    static {
        // MUSHROOM_COW is renamed to MOOSHROOM in 1.20.5
        MOOSHROOM = getKey("mooshroom");

        // SNOWMAN is renamed to SNOW_GOLEM in 1.20.5
        SNOW_GOLEM = getKey("snow_golem");

        FIREWORK = getKey("firework_rocket");

        // Added in 1.21
        ARMADILLO = getKey("armadillo");
        BOGGED = getKey("bogged");
        BREEZE = getKey("breeze");
        BREEZE_WIND_CHARGE = getKey("breeze_wind_charge");

        // Added in 1.21.2
        CREAKING = getKey("creaking");
        WIND_CHARGE = getKey("wind_charge");
    }

    @Nullable
    private static EntityType getKey(@Nonnull String key) {
        return Registry.ENTITY_TYPE.get(NamespacedKey.minecraft(key));
    }
}
