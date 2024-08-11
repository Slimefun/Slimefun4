package io.github.thebusybiscuit.slimefun4.utils.compatibility;

import java.lang.reflect.Field;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.entity.EntityType;

import io.github.thebusybiscuit.slimefun4.api.MinecraftVersion;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;

// https://hub.spigotmc.org/stash/projects/SPIGOT/repos/craftbukkit/browse/src/main/java/org/bukkit/craftbukkit/legacy/FieldRename.java?until=2a6207fe150b6165722fce94c83cc1f206620ab5&untilPath=src%2Fmain%2Fjava%2Forg%2Fbukkit%2Fcraftbukkit%2Flegacy%2FFieldRename.java#158-193
public class VersionedEntityType {
    
    public static final EntityType MOOSHROOM;
    public static final EntityType SNOW_GOLEM;

    static {
        MinecraftVersion version = Slimefun.getMinecraftVersion();

        // MUSHROOM_COW is renamed to MOOSHROOM in 1.20.5
        MOOSHROOM = version.isAtLeast(MinecraftVersion.MINECRAFT_1_20_5)
            ? EntityType.MOOSHROOM
            : getKey("MUSHROOM_COW");

        // SNOWMAN is renamed to SNOW_GOLEM in 1.20.5
        SNOW_GOLEM = version.isAtLeast(MinecraftVersion.MINECRAFT_1_20_5)
            ? EntityType.SNOW_GOLEM
            : getKey("SNOWMAN");
    }

    @Nullable
    private static EntityType getKey(@Nonnull String key) {
        try {
            Field field = EntityType.class.getDeclaredField(key);
            return (EntityType) field.get(null);
        } catch(Exception e) {
            return null;
        }
    }
}
