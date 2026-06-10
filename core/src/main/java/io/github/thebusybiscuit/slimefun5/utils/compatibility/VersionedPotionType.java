package io.github.thebusybiscuit.slimefun5.utils.compatibility;

import java.lang.reflect.Field;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.potion.PotionType;

// Java-8 port: the post-1.20.5 names do not exist at the 1.8.8 floor, so we resolve every field
// reflectively by name (modern first, legacy fallback). See VersionedParticle for the same pattern.
// https://hub.spigotmc.org/stash/projects/SPIGOT/repos/craftbukkit/browse/src/main/java/org/bukkit/craftbukkit/legacy/FieldRename.java?until=2a6207fe150b6165722fce94c83cc1f206620ab5&untilPath=src%2Fmain%2Fjava%2Forg%2Fbukkit%2Fcraftbukkit%2Flegacy%2FFieldRename.java#242-250
public class VersionedPotionType {
    
    public static final PotionType LEAPING;
    public static final PotionType SWIFTNESS;
    public static final PotionType HEALING;
    public static final PotionType HARMING;
    public static final PotionType REGENERATION;
    // Constants absent on the 1.8.8 enum (no legacy alias); null on versions lacking them.
    public static final PotionType AWKWARD;
    public static final PotionType TURTLE_MASTER;
    public static final PotionType SLOW_FALLING;

    static {
        LEAPING = resolve("LEAPING", "JUMP");
        SWIFTNESS = resolve("SWIFTNESS", "SPEED");
        HEALING = resolve("HEALING", "INSTANT_HEAL");
        HARMING = resolve("HARMING", "INSTANT_DAMAGE");
        REGENERATION = resolve("REGENERATION", "REGEN");
        AWKWARD = resolve("AWKWARD", "AWKWARD");
        TURTLE_MASTER = resolve("TURTLE_MASTER", "TURTLE_MASTER");
        SLOW_FALLING = resolve("SLOW_FALLING", "SLOW_FALLING");
    }

    @Nullable
    private static PotionType resolve(@Nonnull String modern, @Nonnull String legacy) {
        PotionType type = getKey(modern);
        return type != null ? type : getKey(legacy);
    }

    @Nullable
    private static PotionType getKey(@Nonnull String key) {
        try {
            Field field = PotionType.class.getDeclaredField(key);
            return (PotionType) field.get(null);
        } catch(Exception e) {
            return null;
        }
    }
}

