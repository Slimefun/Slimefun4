package io.github.thebusybiscuit.slimefun5.core.balance;

import javax.annotation.Nonnull;

/** An item's balance score (0-100) plus its derived {@link BalanceTier}. Immutable value. */
public final class BalanceScore {

    private final int score;
    private final BalanceTier tier;

    private BalanceScore(int score) {
        this.score = score;
        this.tier = BalanceTier.fromScore(score);
    }

    /** Clamps to 0..100 and derives the tier. */
    @Nonnull
    public static BalanceScore of(int score) {
        return new BalanceScore(Math.max(0, Math.min(100, score)));
    }

    public int getScore() {
        return score;
    }

    @Nonnull
    public BalanceTier getTier() {
        return tier;
    }
}
