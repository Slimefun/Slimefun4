package io.github.thebusybiscuit.slimefun4.utils;

import io.github.thebusybiscuit.slimefun4.api.player.StatusEffect;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import org.apache.commons.lang.Validate;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.Objects;

/**
 * This class is a basic wrapper around the
 * status effect.
 *
 * @author Semisol
 *
 * @see RadiationSymptom
 */
public class RadiationUtils {
    private static final StatusEffect RADIATION_EFFECT = new StatusEffect(new NamespacedKey(Objects.requireNonNull(SlimefunPlugin.instance()), "radiation"));
    public static void clearFromPlayer(@Nonnull Player p){
        Validate.notNull(p, "The player cannot be null");
        RADIATION_EFFECT.clear(p);
    }
    public static void addToPlayer(@Nonnull Player p, int exposure){
        Validate.notNull(p, "The player cannot be null");
        RADIATION_EFFECT.addPermanent(p, Math.max(RADIATION_EFFECT.getLevel(p).orElse(0) + exposure, 100));
    }
    public static void removeFromPlayer(@Nonnull Player p, int exposure){
        Validate.notNull(p, "The player cannot be null");
        RADIATION_EFFECT.addPermanent(p, Math.min(RADIATION_EFFECT.getLevel(p).orElse(0) - exposure, 0));
    }
    public static int getExposure(@Nonnull Player p){
        Validate.notNull(p, "The player cannot be null");
        return RADIATION_EFFECT.getLevel(p).orElse(0);
    }
}
