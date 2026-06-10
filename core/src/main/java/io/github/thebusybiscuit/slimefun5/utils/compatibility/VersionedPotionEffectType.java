package io.github.thebusybiscuit.slimefun5.utils.compatibility;

import java.lang.reflect.Field;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.potion.PotionEffectType;

// Java-8 port: the post-1.20.5 names (SLOWNESS, HASTE, ...) do not exist at the 1.8.8 floor, so we
// resolve every field reflectively by name (modern first, legacy fallback) instead of referencing the
// modern constants directly. See VersionedParticle for the same pattern.
// https://hub.spigotmc.org/stash/projects/SPIGOT/repos/craftbukkit/browse/src/main/java/org/bukkit/craftbukkit/legacy/FieldRename.java?until=2a6207fe150b6165722fce94c83cc1f206620ab5&untilPath=src%2Fmain%2Fjava%2Forg%2Fbukkit%2Fcraftbukkit%2Flegacy%2FFieldRename.java#216-228
public class VersionedPotionEffectType {

    public static final PotionEffectType SLOWNESS;
    public static final PotionEffectType HASTE;
    public static final PotionEffectType MINING_FATIGUE;
    public static final PotionEffectType STRENGTH;
    public static final PotionEffectType INSTANT_HEALTH;
    public static final PotionEffectType INSTANT_DAMAGE;
    public static final PotionEffectType JUMP_BOOST;
    public static final PotionEffectType NAUSEA;
    public static final PotionEffectType RESISTANCE;
    // Added in 1.13 with no legacy alias; null on versions lacking it.
    public static final PotionEffectType SLOW_FALLING;
    // Added in 1.9 with no legacy alias; null on versions lacking it.
    public static final PotionEffectType LEVITATION;
    // Added in 1.14 with no legacy alias; null on versions lacking it.
    public static final PotionEffectType BAD_OMEN;

    static {
        SLOWNESS = resolve("SLOWNESS", "SLOW");
        HASTE = resolve("HASTE", "FAST_DIGGING");
        MINING_FATIGUE = resolve("MINING_FATIGUE", "SLOW_DIGGING");
        STRENGTH = resolve("STRENGTH", "INCREASE_DAMAGE");
        INSTANT_HEALTH = resolve("INSTANT_HEALTH", "HEAL");
        INSTANT_DAMAGE = resolve("INSTANT_DAMAGE", "HARM");
        JUMP_BOOST = resolve("JUMP_BOOST", "JUMP");
        NAUSEA = resolve("NAUSEA", "CONFUSION");
        RESISTANCE = resolve("RESISTANCE", "DAMAGE_RESISTANCE");
        SLOW_FALLING = resolve("SLOW_FALLING", "SLOW_FALLING");
        LEVITATION = resolve("LEVITATION", "LEVITATION");
        BAD_OMEN = resolve("BAD_OMEN", "BAD_OMEN");
    }

    @Nullable
    private static PotionEffectType resolve(@Nonnull String modern, @Nonnull String legacy) {
        PotionEffectType type = getKey(modern);
        return type != null ? type : getKey(legacy);
    }

    @Nullable
    private static PotionEffectType getKey(@Nonnull String key) {
        try {
            Field field = PotionEffectType.class.getDeclaredField(key);
            return (PotionEffectType) field.get(null);
        } catch(Exception e) {
            return null;
        }
    }
}

