package io.github.thebusybiscuit.slimefun4.core.guide;

import io.github.thebusybiscuit.slimefun4.api.researches.Research;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.integrations.VaultIntegration;
import java.util.Locale;
import javax.annotation.Nonnull;
import org.bukkit.entity.Player;

/**
 * This enum holds the different unlock research modes a {@link SlimefunGuide} can have.
 * Each constant corresponds to a research unlock mode and a unlock provider
 *
 * @author StarWishsama
 * @see SlimefunGuide
 * @see SlimefunGuideImplementation
 * @see SlimefunGuideUnlockProvider
 */
public enum SlimefunGuideUnlockMode {
    /**
     * Unlock research by withdrawing player's experience level.
     */
    EXPERIENCE(new SlimefunGuideUnlockProvider() {
        @Override
        public boolean canUnlock(@Nonnull Research research, @Nonnull Player p) {
            return p.getLevel() >= research.getCost();
        }

        @Override
        public void processPayment(@Nonnull Research research, @Nonnull Player p) {
            p.setLevel(p.getLevel() - research.getCost());
        }
    }),

    /**
     * Unlock research by withdrawing player's balance.
     */
    CURRENCY(new SlimefunGuideUnlockProvider() {
        @Override
        public boolean canUnlock(@Nonnull Research research, @Nonnull Player p) {
            if (!VaultIntegration.isAvailable()) {
                throw new IllegalStateException("Vault integration is unavailable!");
            }

            return VaultIntegration.getPlayerBalance(p) >= research.getCost();
        }

        @Override
        public void processPayment(@Nonnull Research research, @Nonnull Player p) {
            VaultIntegration.withdrawPlayer(p, research.getCost());
        }
    });

    /**
     * Research unlock provider
     * <p>
     * Process player can unlock research and process research payment.
     *
     * @see SlimefunGuideUnlockProvider
     */
    @Nonnull
    private final SlimefunGuideUnlockProvider unlockProvider;

    SlimefunGuideUnlockMode(@Nonnull SlimefunGuideUnlockProvider unlockProvider) {
        this.unlockProvider = unlockProvider;
    }

    /**
     * Convert string to certain {@link SlimefunGuideUnlockMode}.
     * If string is invalid will fall back to default one (player level)
     *
     * @param s text to validate
     * @return {@link SlimefunGuideUnlockMode}
     */
    @Nonnull
    public static SlimefunGuideUnlockMode check(String s) {
        if (s == null) {
            return SlimefunGuideUnlockMode.EXPERIENCE;
        }

        for (SlimefunGuideUnlockMode value : SlimefunGuideUnlockMode.values()) {
            if (value.toString().equalsIgnoreCase(s)) {
                return value;
            }
        }

        return SlimefunGuideUnlockMode.EXPERIENCE;
    }

    @Nonnull
    public SlimefunGuideUnlockProvider getUnlockProvider() {
        return unlockProvider;
    }

    @Nonnull
    public String getTokenName() {
        return Slimefun.getLocalization().getMessage("guide.unlock-mode-" + toString().toLowerCase(Locale.ROOT));
    }
}
