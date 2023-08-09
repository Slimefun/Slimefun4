package io.github.thebusybiscuit.slimefun4.core.guide.unlockprovider;

import javax.annotation.Nonnull;

import org.bukkit.entity.Player;

import io.github.thebusybiscuit.slimefun4.api.researches.Research;
import io.github.thebusybiscuit.slimefun4.api.guide.SlimefunGuideUnlockProvider;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.integrations.VaultIntegration;

/**
 * Unlock research by withdrawing player's balance.
 *
 * @see SlimefunGuideUnlockProvider
 */
public final class CurrencyUnlockProvider implements SlimefunGuideUnlockProvider {
    @Override
    public boolean canUnlock(@Nonnull Research research, @Nonnull Player p) {
        if (!VaultIntegration.isAvailable()) {
            throw new IllegalStateException("Vault integration is unavailable!");
        }

        return VaultIntegration.getPlayerBalance(p) >= research.getCost();
    }

    @Override
    public void processUnlock(@Nonnull Research research, @Nonnull Player p) {
        VaultIntegration.withdrawPlayer(p, research.getCost());
    }

    @Override
    public @Nonnull String getUnitName() {
        return Slimefun.getLocalization().getMessage("guide.unlock-mode.currency");
    }
}
