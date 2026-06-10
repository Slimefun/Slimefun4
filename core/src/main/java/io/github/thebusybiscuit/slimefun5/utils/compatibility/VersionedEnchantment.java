package io.github.thebusybiscuit.slimefun5.utils.compatibility;

import org.bukkit.enchantments.Enchantment;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

// Java-8 universal port: resolve enchantments without referencing org.bukkit.Registry (1.14+, absent on
// the 1.8.8 floor). Modern servers resolve via Registry.ENCHANTMENT (through RegistryCompat); legacy
// servers fall back to the deprecated Enchantment#getByName using the pre-1.13 field names.
// https://hub.spigotmc.org/stash/projects/SPIGOT/repos/craftbukkit/browse/src/main/java/org/bukkit/craftbukkit/legacy/FieldRename.java?until=2a6207fe150b6165722fce94c83cc1f206620ab5&untilPath=src%2Fmain%2Fjava%2Forg%2Fbukkit%2Fcraftbukkit%2Flegacy%2FFieldRename.java#86-110
public class VersionedEnchantment {

    public static final Enchantment EFFICIENCY;
    public static final Enchantment UNBREAKING;
    public static final Enchantment PROTECTION;
    public static final Enchantment SHARPNESS;
    public static final Enchantment LUCK_OF_THE_SEA;
    public static final Enchantment AQUA_AFFINITY;
    public static final Enchantment FORTUNE;
    public static final Enchantment MENDING;
    public static final Enchantment BINDING_CURSE;
    public static final Enchantment VANISHING_CURSE;

    static {
        // modern registry key, then pre-1.13 legacy field name
        EFFICIENCY = getKey("efficiency", "DIG_SPEED");
        UNBREAKING = getKey("unbreaking", "DURABILITY");
        PROTECTION = getKey("protection", "PROTECTION_ENVIRONMENTAL");
        SHARPNESS = getKey("sharpness", "DAMAGE_ALL");
        LUCK_OF_THE_SEA = getKey("luck_of_the_sea", "LUCK");
        AQUA_AFFINITY = getKey("aqua_affinity", "WATER_WORKER");
        FORTUNE = getKey("fortune", "LOOT_BONUS_BLOCKS");
        // Added after 1.8 — no pre-1.13 alias, so null on the very oldest servers.
        MENDING = getKey("mending", "MENDING");
        BINDING_CURSE = getKey("binding_curse", "BINDING_CURSE");
        VANISHING_CURSE = getKey("vanishing_curse", "VANISHING_CURSE");
    }

    @Nullable
    @SuppressWarnings("deprecation")
    private static Enchantment getKey(@Nonnull String key, @Nonnull String legacyName) {
        Object modern = RegistryCompat.get("ENCHANTMENT", key);

        if (modern instanceof Enchantment) {
            return (Enchantment) modern;
        }

        try {
            return Enchantment.getByName(legacyName);
        } catch (Throwable ignored) {
            return null;
        }
    }
}
