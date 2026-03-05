package io.github.thebusybiscuit.slimefun4.utils.compatibility;

import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.attribute.Attribute;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

// https://hub.spigotmc.org/stash/projects/SPIGOT/repos/craftbukkit/browse/src/main/java/org/bukkit/craftbukkit/legacy/FieldRename.java?until=2a6207fe150b6165722fce94c83cc1f206620ab5&untilPath=src%2Fmain%2Fjava%2Forg%2Fbukkit%2Fcraftbukkit%2Flegacy%2FFieldRename.java#112-157
public class VersionedAttribute {

    public static final Attribute MAX_HEALTH;

    static {
        // Attribute.GENERIC_MAX_HEALTH was renamed to Attribute.MAX_HEALTH in Minecraft 1.21.2+.
        // Registry key is "generic.max_health" across all versions
        MAX_HEALTH = getKey("generic.max_health");
    }

    @Nullable
    private static Attribute getKey(@Nonnull String key) {
        return Registry.ATTRIBUTE.get(NamespacedKey.minecraft(key));
    }
}
