package io.github.thebusybiscuit.slimefun4.utils.compatibility;

import java.lang.reflect.Field;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.enchantments.Enchantment;

import io.github.thebusybiscuit.slimefun4.api.MinecraftVersion;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;

// https://hub.spigotmc.org/stash/projects/SPIGOT/repos/craftbukkit/browse/src/main/java/org/bukkit/craftbukkit/legacy/FieldRename.java?until=2a6207fe150b6165722fce94c83cc1f206620ab5&untilPath=src%2Fmain%2Fjava%2Forg%2Fbukkit%2Fcraftbukkit%2Flegacy%2FFieldRename.java#86-110
public class VersionedEnchantment {
    
    public static final Enchantment EFFICIENCY;
    public static final Enchantment UNBREAKING;
    public static final Enchantment PROTECTION;
    public static final Enchantment SHARPNESS;
    public static final Enchantment LUCK_OF_THE_SEA;
    public static final Enchantment AQUA_AFFINITY;
    public static final Enchantment FORTUNE;

    static {
        MinecraftVersion version = Slimefun.getMinecraftVersion();

        // DIG_SPEED is renamed to EFFICIENCY in 1.20.5
        EFFICIENCY = version.isAtLeast(MinecraftVersion.MINECRAFT_1_20_5)
            ? Enchantment.EFFICIENCY
            : getKey("DIG_SPEED");

        // DURABILITY is renamed to UNBREAKING in 1.20.5
        UNBREAKING = version.isAtLeast(MinecraftVersion.MINECRAFT_1_20_5)
            ? Enchantment.UNBREAKING
            : getKey("DURABILITY");

        // PROTECTION_ENVIRONMENTAL is renamed to PROTECTION in 1.20.5
        PROTECTION = version.isAtLeast(MinecraftVersion.MINECRAFT_1_20_5)
            ? Enchantment.PROTECTION
            : getKey("PROTECTION_ENVIRONMENTAL");

        // DAMAGE_ALL is renamed to SHARPNESS in 1.20.5
        SHARPNESS = version.isAtLeast(MinecraftVersion.MINECRAFT_1_20_5)
            ? Enchantment.SHARPNESS
            : getKey("DAMAGE_ALL");

        // LUCK is renamed to LUCK_OF_THE_SEA in 1.20.5
        LUCK_OF_THE_SEA = version.isAtLeast(MinecraftVersion.MINECRAFT_1_20_5)
            ? Enchantment.LUCK_OF_THE_SEA
            : getKey("LUCK");

        // WATER_WORKER is renamed to AQUA_AFFINITY in 1.20.5
        AQUA_AFFINITY = version.isAtLeast(MinecraftVersion.MINECRAFT_1_20_5)
            ? Enchantment.AQUA_AFFINITY
            : getKey("WATER_WORKER");

        // LOOT_BONUS_BLOCKS is renamed to FORTUNE in 1.20.5
        FORTUNE = version.isAtLeast(MinecraftVersion.MINECRAFT_1_20_5)
            ? Enchantment.FORTUNE
            : getKey("LOOT_BONUS_BLOCKS");
    }

    @Nullable
    private static Enchantment getKey(@Nonnull String key) {
        try {
            Field field = Enchantment.class.getDeclaredField(key);
            return (Enchantment) field.get(null);
        } catch(Exception e) {
            return null;
        }
    }
}
