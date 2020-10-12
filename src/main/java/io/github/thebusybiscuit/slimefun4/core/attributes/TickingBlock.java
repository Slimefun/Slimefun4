package io.github.thebusybiscuit.slimefun4.core.attributes;

import javax.annotation.Nonnull;

import org.bukkit.block.Block;

import io.github.thebusybiscuit.slimefun4.implementation.tasks.TickerTask;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;

/**
 * Implement this interface for a {@link SlimefunItem} to perform a task repeatedly
 * on a placed {@link Block}.
 * 
 * @author TheBusyBiscuit
 * 
 * @see TickerTask
 * @see TickingMethod
 *
 */
public interface TickingBlock extends ItemAttribute {

    /**
     * This returns the {@link TickingMethod} of this {@link TickingBlock} block.
     * It defines on what {@link Thread} the ticker should run.
     * 
     * @return The {@link TickingMethod} for this {@link TickingBlock}
     */
    @Nonnull
    TickingMethod getTickingMethod();

    /**
     * This method gets called when a certain {@link Block} is ticked.
     * Implement your ticker logic in this method.
     * 
     * @param b
     *            The {@link Block} which is being ticked.
     */
    void tick(@Nonnull Block b);

    /**
     * This method is called when a new "tick cycle" starts, it is called <strong>once</strong>
     * before the {@link #tick(Block)} method is called for every {@link Block} seperately.
     * This method gets called once per cycle only, use it to handle cooldowns etc...
     */
    default void onTickCycleStart() {
        // override this method as necessary
    }

}
