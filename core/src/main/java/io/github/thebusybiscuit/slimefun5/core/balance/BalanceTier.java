package io.github.thebusybiscuit.slimefun5.core.balance;

import javax.annotation.Nonnull;

/**
 * Coarse power classification of a Slimefun item, relative to vanilla Minecraft. The numeric score
 * (0-100) is the source of truth; the tier is a human-readable band derived from it. Vanilla items
 * sit on the same scale (an end crystal ~70, a mace ~85), so OVERPOWERED means "as strong as vanilla's
 * strongest", not "broken".
 */
public enum BalanceTier {

    TRIVIAL(0, 10),
    STANDARD(11, 25),
    ADVANCED(26, 40),
    POWERFUL(41, 69),
    OVERPOWERED(70, 100);

    private final int min;
    private final int max;

    BalanceTier(int min, int max) {
        this.min = min;
        this.max = max;
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }

    /** The tier whose band contains the given score (clamped to 0..100). */
    @Nonnull
    public static BalanceTier fromScore(int score) {
        int s = Math.max(0, Math.min(100, score));

        for (BalanceTier tier : values()) {
            if (s <= tier.max) {
                return tier;
            }
        }

        return OVERPOWERED;
    }
}
