package io.github.thebusybiscuit.slimefun5.core.balance;

import java.util.Collection;

import javax.annotation.Nonnull;

/**
 * Aggregate balance picture of one addon, computed from its scored (non-trivial) items: average power,
 * the single strongest item's score, and how many items are OVERPOWERED (>=70). A sum is deliberately
 * NOT used - it would punish content-rich addons.
 */
public final class AddonBalanceSummary {

    public static final AddonBalanceSummary EMPTY = new AddonBalanceSummary(0, 0, 0, 0);

    private final int average;
    private final int peak;
    private final int opCount;
    private final int scoredItems;

    public AddonBalanceSummary(int average, int peak, int opCount, int scoredItems) {
        this.average = average;
        this.peak = peak;
        this.opCount = opCount;
        this.scoredItems = scoredItems;
    }

    public int getAverage() {
        return average;
    }

    public int getPeak() {
        return peak;
    }

    public int getOpCount() {
        return opCount;
    }

    public int getScoredItems() {
        return scoredItems;
    }

    public boolean isEmpty() {
        return scoredItems == 0;
    }

    @Nonnull
    public BalanceTier getAverageTier() {
        return BalanceTier.fromScore(average);
    }

    @Nonnull
    public BalanceTier getPeakTier() {
        return BalanceTier.fromScore(peak);
    }

    /**
     * The addon's overall verdict, effort-aware. An addon is only {@link BalanceVerdict#OVERPOWERED} when
     * it actually contains cheap-yet-strong items ({@code opCount > 0}); an addon whose strongest items are
     * all gated behind a heavy grind reads as {@link BalanceVerdict#ENDGAME} (earned power, still balanced)
     * even though its peak power is high. This is what keeps a grind-heavy addon from being mislabelled OP.
     */
    @Nonnull
    public BalanceVerdict getVerdict() {
        if (opCount > 0) {
            return BalanceVerdict.OVERPOWERED;
        }

        if (peak >= BalanceTier.OVERPOWERED.getMin()) {
            return BalanceVerdict.ENDGAME;
        }

        if (peak >= BalanceTier.POWERFUL.getMin()) {
            return BalanceVerdict.BALANCED;
        }

        return BalanceVerdict.FILLER;
    }

    /** Aggregates per-item scores (already filtered to non-trivial). Empty input => EMPTY. */
    @Nonnull
    public static AddonBalanceSummary of(@Nonnull Collection<Integer> scores) {
        if (scores.isEmpty()) {
            return EMPTY;
        }

        int total = 0;
        int peak = 0;
        int op = 0;

        for (int s : scores) {
            total += s;

            if (s > peak) {
                peak = s;
            }

            if (s >= BalanceTier.OVERPOWERED.getMin()) {
                op++;
            }
        }

        return new AddonBalanceSummary(Math.round((float) total / scores.size()), peak, op, scores.size());
    }

    /**
     * Same as {@link #of(Collection)} but with an externally-computed overpowered count - used when OP is
     * a {@link BalanceVerdict#OVERPOWERED} tally (power AND low effort) rather than a raw power threshold.
     */
    @Nonnull
    public static AddonBalanceSummary of(@Nonnull Collection<Integer> scores, int opCount) {
        if (scores.isEmpty()) {
            return EMPTY;
        }

        int total = 0;
        int peak = 0;

        for (int s : scores) {
            total += s;

            if (s > peak) {
                peak = s;
            }
        }

        return new AddonBalanceSummary(Math.round((float) total / scores.size()), peak, opCount, scores.size());
    }
}
