package io.github.thebusybiscuit.slimefun4.core.guide;

import io.github.thebusybiscuit.slimefun4.api.researches.Research;
import javax.annotation.Nonnull;
import org.bukkit.entity.Player;

/**
 * The {@link SlimefunGuideUnlockProvider} used for process unlock research
 * in Slimefun Guide.
 * <p>
 * You could trail the method to unlock your research,
 * not only use experience.
 */
public interface SlimefunGuideUnlockProvider {

    /**
     * This method used for check {@link Player}
     * could unlock specific research or not
     *
     * @param research {@link Research}
     * @param p {@link Player}
     *
     * @return whether player can unlock research or not
     */
    boolean canUnlock(@Nonnull Research research, @Nonnull Player p);

    /**
     * This method used for processing unlock research
     * For example, taken player's experience level or money.
     *
     * @param research {@link Research}
     * @param p {@link Player}
     */
    void processUnlock(@Nonnull Research research, @Nonnull Player p);

    /**
     * This returns the unit name of research unlock token
     *
     * @return unit name
     */
    @Nonnull
    String getUnitName();
}
