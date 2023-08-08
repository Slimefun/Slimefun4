package io.github.thebusybiscuit.slimefun4.core.guide;

import io.github.thebusybiscuit.slimefun4.api.researches.Research;
import javax.annotation.Nonnull;
import org.bukkit.entity.Player;

public interface SlimefunGuideUnlockProvider {
    boolean canUnlock(@Nonnull Research research, @Nonnull Player p);

    void processPayment(@Nonnull Research research, @Nonnull Player p);
}
