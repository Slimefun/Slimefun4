package io.github.thebusybiscuit.slimefun4.utils.compatibility;

import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.enchantments.Enchantment;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

// https://hub.spigotmc.org/stash/projects/SPIGOT/repos/craftbukkit/browse/src/main/java/org/bukkit/craftbukkit/legacy/FieldRename.java?until=2a6207fe150b6165722fce94c83cc1f206620ab5&untilPath=src%2Fmain%2Fjava%2Forg%2Fbukkit%2Fcraftbukkit%2Flegacy%2FFieldRename.java#86-110
public class VersionedEnchantment {

    public static final Enchantment EFFICIENCY;
    public static final Enchantment UNBREAKING;
    public static final Enchantment PROTECTION;
    public static final Enchantment SHARPNESS;
    public static final Enchantment LUCK_OF_THE_SEA;
    public static final Enchantment AQUA_AFFINITY;
    public static final Enchantment FORTUNE;
    public static final Enchantment DENSITY;
    public static final Enchantment BREACH;
    public static final Enchantment WIND_BURST;

    static {
        // DIG_SPEED is renamed to EFFICIENCY in 1.20.5
        EFFICIENCY = getKey("efficiency");

        // DURABILITY is renamed to UNBREAKING in 1.20.5
        UNBREAKING = getKey("unbreaking");

        // PROTECTION_ENVIRONMENTAL is renamed to PROTECTION in 1.20.5
        PROTECTION = getKey("protection");

        // DAMAGE_ALL is renamed to SHARPNESS in 1.20.5
        SHARPNESS = getKey("sharpness");

        // LUCK is renamed to LUCK_OF_THE_SEA in 1.20.5
        LUCK_OF_THE_SEA = getKey("luck_of_the_sea");

        // WATER_WORKER is renamed to AQUA_AFFINITY in 1.20.5
        AQUA_AFFINITY = getKey("aqua_affinity");

        // LOOT_BONUS_BLOCKS is renamed to FORTUNE in 1.20.5
        FORTUNE = getKey("fortune");

        // Added in 1.21
        DENSITY = getKey("density");
        BREACH = getKey("breach");
        WIND_BURST = getKey("wind_burst");
    }

    @Nullable
    private static Enchantment getKey(@Nonnull String key) {
        return Registry.ENCHANTMENT.get(NamespacedKey.minecraft(key));
    }
}
