package io.github.thebusybiscuit.slimefun4.utils;

import javax.annotation.Nonnull;

import com.google.common.base.Preconditions;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;

import io.github.thebusybiscuit.slimefun4.api.player.StatusEffect;
import io.github.thebusybiscuit.slimefun4.core.attributes.RadiationSymptom;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;

/**
 * This class is a basic wrapper around the
 * status effect.
 *
 * @author Semisol
 *
 * @see RadiationSymptom
 */
public final class RadiationUtils {

    private static final StatusEffect RADIATION_EFFECT = new StatusEffect(new NamespacedKey(Slimefun.instance(), "radiation"));
    private static final int MAX_EXPOSURE_LEVEL = 100;

    public static void clearExposure(@Nonnull Player p) {
        Preconditions.checkNotNull(p, "The player cannot be null");

        RADIATION_EFFECT.clear(p);
    }

    public static int getExposure(@Nonnull Player p) {
        Preconditions.checkNotNull(p, "The player must not be null");

        return RADIATION_EFFECT.getLevel(p).orElse(0);
    }

    public static void addExposure(@Nonnull Player p, int exposure) {
        Preconditions.checkNotNull(p, "The player cannot be null");

        int level = Math.min(RADIATION_EFFECT.getLevel(p).orElse(0) + exposure, MAX_EXPOSURE_LEVEL);
        RADIATION_EFFECT.addPermanent(p, level);
    }

    public static void removeExposure(@Nonnull Player p, int exposure) {
        Preconditions.checkNotNull(p, "The player should not be null");

        int level = Math.max(RADIATION_EFFECT.getLevel(p).orElse(0) - exposure, 0);
        RADIATION_EFFECT.addPermanent(p, level);
    }
}
