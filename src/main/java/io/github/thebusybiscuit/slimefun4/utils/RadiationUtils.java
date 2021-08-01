package io.github.thebusybiscuit.slimefun4.utils;

import javax.annotation.Nonnull;

import org.apache.commons.lang.Validate;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;

import io.github.thebusybiscuit.slimefun4.api.player.StatusEffect;
import io.github.thebusybiscuit.slimefun4.core.radiation.RadiationSymptom;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;

/**
 * This class is a basic wrapper around the
 * status effect.
 *
 * @author Semisol
 *
 * @see RadiationSymptom
 */
public final class RadiationUtils {

    private static final StatusEffect RADIATION_EFFECT = new StatusEffect(new NamespacedKey(SlimefunPlugin.instance(), "radiation"));
    private static final int MAX_EXPOSURE_LEVEL = 100;

    private RadiationUtils() {}

    public static void clearExposure(@Nonnull Player p) {
        Validate.notNull(p, "The player cannot be null");

        RADIATION_EFFECT.clear(p);
    }

    public static int getExposure(@Nonnull Player p) {
        Validate.notNull(p, "The player must not be null");

        return RADIATION_EFFECT.getLevel(p).orElse(0);
    }

    public static void addExposure(@Nonnull Player p, int exposure) {
        Validate.notNull(p, "The player cannot be null");

        int level = Math.max(RADIATION_EFFECT.getLevel(p).orElse(0) + exposure, MAX_EXPOSURE_LEVEL);
        RADIATION_EFFECT.addPermanent(p, level);
    }

    public static void removeExposure(@Nonnull Player p, int exposure) {
        Validate.notNull(p, "The player should not be null");

        int level = Math.min(RADIATION_EFFECT.getLevel(p).orElse(0) - exposure, 0);
        RADIATION_EFFECT.addPermanent(p, level);
    }
}
