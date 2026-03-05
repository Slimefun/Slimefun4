package io.github.thebusybiscuit.slimefun4.utils.compatibility;

import java.lang.reflect.Field;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.potion.PotionType;

import io.github.thebusybiscuit.slimefun4.api.MinecraftVersion;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;

// https://hub.spigotmc.org/stash/projects/SPIGOT/repos/craftbukkit/browse/src/main/java/org/bukkit/craftbukkit/legacy/FieldRename.java?until=2a6207fe150b6165722fce94c83cc1f206620ab5&untilPath=src%2Fmain%2Fjava%2Forg%2Fbukkit%2Fcraftbukkit%2Flegacy%2FFieldRename.java#242-250
public class VersionedPotionType {
    
    public static final PotionType LEAPING;
    public static final PotionType SWIFTNESS;
    public static final PotionType HEALING;
    public static final PotionType HARMING;
    public static final PotionType REGENERATION;

    static {
        MinecraftVersion version = Slimefun.getMinecraftVersion();

        LEAPING = version.isAtLeast(MinecraftVersion.MINECRAFT_1_20_5)
            ? PotionType.LEAPING
            : getKey("JUMP");

        SWIFTNESS = version.isAtLeast(MinecraftVersion.MINECRAFT_1_20_5)
            ? PotionType.SWIFTNESS
            : getKey("SPEED");

        HEALING = version.isAtLeast(MinecraftVersion.MINECRAFT_1_20_5)
            ? PotionType.HEALING
            : getKey("INSTANT_HEAL");
        
        HARMING = version.isAtLeast(MinecraftVersion.MINECRAFT_1_20_5)
            ? PotionType.HARMING
            : getKey("INSTANT_DAMAGE");

        REGENERATION = version.isAtLeast(MinecraftVersion.MINECRAFT_1_20_5)
            ? PotionType.REGENERATION
            : getKey("REGEN");
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
