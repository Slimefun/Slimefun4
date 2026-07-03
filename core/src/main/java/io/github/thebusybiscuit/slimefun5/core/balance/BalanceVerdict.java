package io.github.thebusybiscuit.slimefun5.core.balance;

import javax.annotation.Nonnull;

/**
 * The balance verdict for an item, derived from its power score AND its {@link ItemEffortHeuristic effort}
 * to obtain. Power alone is not imbalance - power relative to cost is: a strong item that is cheap to make
 * is {@link #OVERPOWERED}, while an equally strong item gated behind a deep tech tree is merely
 * {@link #ENDGAME} (earned, and fine).
 */
public enum BalanceVerdict {

    /** Weak/cheap filler - not a balance concern. */
    FILLER,
    /** Reasonable power for its cost. */
    BALANCED,
    /** Very strong, but its cost/grind justifies it. */
    ENDGAME,
    /** Very strong AND cheaply obtained - the items that actually break progression. */
    OVERPOWERED;

    /** Effort at or above this is considered "earned"; below it, high power reads as overpowered. */
    public static final int EARNED_EFFORT_THRESHOLD = 45;

    @Nonnull
    public static BalanceVerdict from(int power, int effort) {
        if (power >= BalanceTier.OVERPOWERED.getMin()) {
            return effort < EARNED_EFFORT_THRESHOLD ? OVERPOWERED : ENDGAME;
        }

        if (power >= BalanceTier.POWERFUL.getMin()) {
            return BALANCED;
        }

        return FILLER;
    }
}
