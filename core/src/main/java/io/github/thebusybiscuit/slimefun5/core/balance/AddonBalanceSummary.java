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
}
