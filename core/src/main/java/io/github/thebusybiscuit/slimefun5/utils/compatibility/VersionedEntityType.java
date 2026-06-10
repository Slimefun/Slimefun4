package io.github.thebusybiscuit.slimefun5.utils.compatibility;

import org.bukkit.entity.EntityType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

// Java-8 universal port: resolve entity types without referencing org.bukkit.Registry (1.14+, absent on
// the 1.8.8 floor). Modern servers resolve via Registry.ENTITY_TYPE (through RegistryCompat); legacy
// servers fall back to EntityType.valueOf using the pre-1.13 enum names.
// https://hub.spigotmc.org/stash/projects/SPIGOT/repos/craftbukkit/browse/src/main/java/org/bukkit/craftbukkit/legacy/FieldRename.java?until=2a6207fe150b6165722fce94c83cc1f206620ab5&untilPath=src%2Fmain%2Fjava%2Forg%2Fbukkit%2Fcraftbukkit%2Flegacy%2FFieldRename.java#158-193
public class VersionedEntityType {

    public static final EntityType MOOSHROOM;
    public static final EntityType SNOW_GOLEM;
    public static final EntityType FIREWORK;

    static {
        // MUSHROOM_COW is renamed to MOOSHROOM in 1.20.5
        MOOSHROOM = getKey("mooshroom", "MUSHROOM_COW");

        // SNOWMAN is renamed to SNOW_GOLEM in 1.20.5
        SNOW_GOLEM = getKey("snow_golem", "SNOWMAN");

        FIREWORK = getKey("firework_rocket", "FIREWORK");
    }

    @Nullable
    private static EntityType getKey(@Nonnull String key, @Nonnull String legacyName) {
        Object modern = RegistryCompat.get("ENTITY_TYPE", key);

        if (modern instanceof EntityType) {
            return (EntityType) modern;
        }

        try {
            return EntityType.valueOf(legacyName);
        } catch (Throwable ignored) {
            return null;
        }
    }
}
