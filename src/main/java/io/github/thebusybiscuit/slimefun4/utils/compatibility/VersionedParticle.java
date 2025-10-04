package io.github.thebusybiscuit.slimefun4.utils.compatibility;

import java.lang.reflect.Field;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.Particle;

import io.github.thebusybiscuit.slimefun4.api.MinecraftVersion;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;

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
    public static final Particle BREEZE_WIND;
    public static final Particle TRIAL_SPAWNER_DETECTION;
    public static final Particle TRIAL_SPAWNER_DETECTION_OMINOUS;
    public static final Particle TRIAL_SPAWNER_EJECTION;
    public static final Particle TRIAL_SPAWNER_EJECTION_OMINOUS;
    public static final Particle TRIAL_SPAWNER_SMOKE;
    public static final Particle TRIAL_SPAWNER_SMOKE_OMINOUS;
    public static final Particle TRIAL_OMEN;
    public static final Particle OMINOUS_SPAWNING;
    public static final Particle GUST;
    public static final Particle SMALL_GUST;
    public static final Particle GUST_EMITTER_LARGE;
    public static final Particle GUST_EMITTER_SMALL;

    static {
        MinecraftVersion version = Slimefun.getMinecraftVersion();

        // REDSTONE is renamed to DUST in 1.20.5
        DUST = version.isAtLeast(MinecraftVersion.MINECRAFT_1_20_5)
            ? Particle.DUST
            : getKey("REDSTONE");

        // SMOKE_NORMAL is renamed to SMOKE in 1.20.5
        SMOKE = version.isAtLeast(MinecraftVersion.MINECRAFT_1_20_5)
            ? Particle.SMOKE
            : getKey("SMOKE_NORMAL");
    
        // VILLAGER_HAPPY is renamed to HAPPY_VILLAGER in 1.20.5
        HAPPY_VILLAGER = version.isAtLeast(MinecraftVersion.MINECRAFT_1_20_5)
            ? Particle.HAPPY_VILLAGER
            : getKey("VILLAGER_HAPPY");

        // CRIT_MAGIC is renamed to ENCHANTED_HIT in 1.20.5
        ENCHANTED_HIT = version.isAtLeast(MinecraftVersion.MINECRAFT_1_20_5)
            ? Particle.ENCHANTED_HIT
            : getKey("CRIT_MAGIC");
        
        // EXPLOSION_LARGE is renamed to EXPLOSION in 1.20.5
        EXPLOSION = version.isAtLeast(MinecraftVersion.MINECRAFT_1_20_5)
            ? Particle.EXPLOSION
            : getKey("EXPLOSION_LARGE");
        
        // SPELL_WITCH is renamed to WITCH in 1.20.5
        WITCH = version.isAtLeast(MinecraftVersion.MINECRAFT_1_20_5)
            ? Particle.WITCH
            : getKey("SPELL_WITCH");
        
        // FIREWORKS_SPARK is renamed to FIREWORK in 1.20.5
        FIREWORK = version.isAtLeast(MinecraftVersion.MINECRAFT_1_20_5)
            ? Particle.FIREWORK
            : getKey("FIREWORKS_SPARK");
        
        // ENCHANTMENT_TABLE is renamed to ENCHANT in 1.20.5
        ENCHANT = version.isAtLeast(MinecraftVersion.MINECRAFT_1_20_5)
            ? Particle.ENCHANT
            : getKey("ENCHANTMENT_TABLE");

        // Added in 1.21
        BREEZE_WIND = getKey("BREEZE_WIND");
        TRIAL_SPAWNER_DETECTION = getKey("TRIAL_SPAWNER_DETECTION");
        TRIAL_SPAWNER_DETECTION_OMINOUS = getKey("TRIAL_SPAWNER_DETECTION_OMINOUS");
        TRIAL_SPAWNER_EJECTION = getKey("TRIAL_SPAWNER_EJECTION");
        TRIAL_SPAWNER_EJECTION_OMINOUS = getKey("TRIAL_SPAWNER_EJECTION_OMINOUS");
        TRIAL_SPAWNER_SMOKE = getKey("TRIAL_SPAWNER_SMOKE");
        TRIAL_SPAWNER_SMOKE_OMINOUS = getKey("TRIAL_SPAWNER_SMOKE_OMINOUS");
        TRIAL_OMEN = getKey("TRIAL_OMEN");
        OMINOUS_SPAWNING = getKey("OMINOUS_SPAWNING");
        GUST = getKey("GUST");
        SMALL_GUST = getKey("SMALL_GUST");
        GUST_EMITTER_LARGE = getKey("GUST_EMITTER_LARGE");
        GUST_EMITTER_SMALL = getKey("GUST_EMITTER_SMALL");
    }

    @Nullable
    private static Particle getKey(@Nonnull String key) {
        try {
            Field field = Particle.class.getDeclaredField(key);
            return (Particle) field.get(null);
        } catch (Exception e) {
            // Return null so callers can decide how to handle missing particles
            return null;
        }
    }
}
