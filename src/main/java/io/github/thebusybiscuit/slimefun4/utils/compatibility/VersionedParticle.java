package io.github.thebusybiscuit.slimefun4.utils.compatibility;

import io.github.thebusybiscuit.slimefun4.api.MinecraftVersion;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Registry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

// https://hub.spigotmc.org/stash/projects/SPIGOT/repos/craftbukkit/browse/src/main/java/org/bukkit/craftbukkit/legacy/FieldRename.java?until=2a6207fe150b6165722fce94c83cc1f206620ab5&untilPath=src%2Fmain%2Fjava%2Forg%2Fbukkit%2Fcraftbukkit%2Flegacy%2FFieldRename.java#281-318
public class VersionedParticle {

    public static final Particle DUST;
    public static final Particle SMOKE;
    public static final Particle HAPPY_VILLAGER;
    public static final Particle ENCHANTED_HIT;
    public static final Particle EXPLOSION;
    public static final Particle WITCH;
    public static final Particle FIREWORK;
    public static final Particle ENCHANT;

    static {
        MinecraftVersion version = Slimefun.getMinecraftVersion();

        // REDSTONE is renamed to DUST in 1.20.5
        DUST = getKey("dust");

        // SMOKE_NORMAL is renamed to SMOKE in 1.20.5
        SMOKE = getKey("smoke");

        // VILLAGER_HAPPY is renamed to HAPPY_VILLAGER in 1.20.5
        HAPPY_VILLAGER = getKey("happy_villager");

        // CRIT_MAGIC is renamed to ENCHANTED_HIT in 1.20.5
        ENCHANTED_HIT = getKey("enchanted_hit");

        // EXPLOSION_LARGE is renamed to EXPLOSION in 1.20.5
        EXPLOSION = getKey("explosion");

        // SPELL_WITCH is renamed to WITCH in 1.20.5
        WITCH = getKey("witch");

        // FIREWORKS_SPARK is renamed to FIREWORK in 1.20.5
        FIREWORK = getKey("firework");

        // ENCHANTMENT_TABLE is renamed to ENCHANT in 1.20.5
        ENCHANT = getKey("enchant");
    }

    @Nullable
    private static Particle getKey(@Nonnull String key) {
        return Registry.PARTICLE_TYPE.get(NamespacedKey.minecraft(key));
    }
}
