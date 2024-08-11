package io.github.thebusybiscuit.slimefun4.utils.compatibility;

import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.potion.PotionEffectType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

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
        SLOWNESS = getKey("slowness");

        HASTE = getKey("haste");

        MINING_FATIGUE = getKey("mining_fatigue");

        STRENGTH = getKey("strength");

        INSTANT_HEALTH = getKey("instant_health");

        INSTANT_DAMAGE = getKey("instant_damage");

        JUMP_BOOST = getKey("jump_boost");

        NAUSEA = getKey("nausea");

        RESISTANCE = getKey("resistance");
    }

    @Nullable
    private static PotionEffectType getKey(@Nonnull String key) {
        return Registry.POTION_EFFECT_TYPE.get(NamespacedKey.minecraft(key));
    }
}
