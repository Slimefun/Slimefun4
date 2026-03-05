package io.github.thebusybiscuit.slimefun4.utils.compatibility;

import java.lang.reflect.Field;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.potion.PotionEffectType;

import io.github.thebusybiscuit.slimefun4.api.MinecraftVersion;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;

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

    static {
        MinecraftVersion version = Slimefun.getMinecraftVersion();

        SLOWNESS = version.isAtLeast(MinecraftVersion.MINECRAFT_1_20_5)
            ? PotionEffectType.SLOWNESS
            : getKey("SLOW");

        HASTE = version.isAtLeast(MinecraftVersion.MINECRAFT_1_20_5)
            ? PotionEffectType.HASTE
            : getKey("FAST_DIGGING");

        MINING_FATIGUE = version.isAtLeast(MinecraftVersion.MINECRAFT_1_20_5)
            ? PotionEffectType.MINING_FATIGUE
            : getKey("SLOW_DIGGING");

        STRENGTH = version.isAtLeast(MinecraftVersion.MINECRAFT_1_20_5)
            ? PotionEffectType.STRENGTH
            : getKey("INCREASE_DAMAGE");

        INSTANT_HEALTH = version.isAtLeast(MinecraftVersion.MINECRAFT_1_20_5)
            ? PotionEffectType.INSTANT_HEALTH
            : getKey("HEAL");

        INSTANT_DAMAGE = version.isAtLeast(MinecraftVersion.MINECRAFT_1_20_5)
            ? PotionEffectType.INSTANT_DAMAGE
            : getKey("HARM");

        JUMP_BOOST = version.isAtLeast(MinecraftVersion.MINECRAFT_1_20_5)
            ? PotionEffectType.JUMP_BOOST
            : getKey("JUMP");

        NAUSEA = version.isAtLeast(MinecraftVersion.MINECRAFT_1_20_5)
            ? PotionEffectType.NAUSEA
            : getKey("CONFUSION");

        RESISTANCE = version.isAtLeast(MinecraftVersion.MINECRAFT_1_20_5)
            ? PotionEffectType.RESISTANCE
            : getKey("DAMAGE_RESISTANCE");
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
