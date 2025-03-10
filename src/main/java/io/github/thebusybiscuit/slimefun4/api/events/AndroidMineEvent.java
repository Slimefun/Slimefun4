package io.github.thebusybiscuit.slimefun4.api.events;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.block.Block;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import io.github.thebusybiscuit.slimefun4.implementation.items.androids.AndroidInstance;
import io.github.thebusybiscuit.slimefun4.implementation.items.androids.MinerAndroid;

/**
 * This {@link Event} is fired before a {@link MinerAndroid} mines a {@link Block}.
 * If this {@link Event} is cancelled, the {@link Block} will not be mined.
 * 
 * @author poma123, JustAHuman
 */
public class AndroidMineEvent extends AndroidBlockBreakEvent {

    private static final HandlerList handlers = new HandlerList();

    /**
     * @param block
     *            The {@link Block} to be mined
     * @param android
     *            The {@link AndroidInstance} that triggered this {@link AndroidMineEvent event}
     */
    @ParametersAreNonnullByDefault
    public AndroidMineEvent(Block block, AndroidInstance android) {
        super(block, android);
    }

    @Override
    public @Nonnull HandlerList getHandlers() {
        return getHandlerList();
    }

    public static @Nonnull HandlerList getHandlerList() {
        return handlers;
    }

}