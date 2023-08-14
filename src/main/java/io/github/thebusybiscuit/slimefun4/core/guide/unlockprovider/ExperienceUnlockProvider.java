package io.github.thebusybiscuit.slimefun4.core.guide.unlockprovider;

import javax.annotation.Nonnull;

import org.bukkit.entity.Player;

import io.github.thebusybiscuit.slimefun4.api.researches.Research;
import io.github.thebusybiscuit.slimefun4.api.guide.SlimefunGuideUnlockProvider;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;

/**
 * Unlock research by withdrawing player's experience level.
 *
 * @see SlimefunGuideUnlockProvider
 */
public final class ExperienceUnlockProvider implements SlimefunGuideUnlockProvider {

    @Override
    public boolean canUnlock(@Nonnull Research research, @Nonnull Player p) {
        return p.getLevel() >= research.getCost();
    }

    @Override
    public void processUnlock(@Nonnull Research research, @Nonnull Player p) {
        p.setLevel(p.getLevel() - research.getCost());
    }

    @Override
    public @Nonnull String getUnitName() {
        return Slimefun.getLocalization().getMessage("guide.unlock-mode.experience");
    }
}
