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
 * @author poma123
 * 
 */
public class AndroidMineEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private final Block block;
    private final AndroidInstance android;
    private boolean cancelled;

    /**
     * @param block
     *            The mined {@link Block}
     * @param android
     *            The {@link AndroidInstance} that triggered this {@link Event}
     */
    @ParametersAreNonnullByDefault
    public AndroidMineEvent(Block block, AndroidInstance android) {
        this.block = block;
        this.android = android;
    }

    /**
     * This method returns the mined {@link Block}
     *
     * @return the mined {@link Block}
     */
    @Nonnull
    public Block getBlock() {
        return block;
    }

    /**
     * This method returns the {@link AndroidInstance} who
     * triggered this {@link Event}
     *
     * @return the involved {@link AndroidInstance}
     */
    @Nonnull
    public AndroidInstance getAndroid() {
        return android;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }

    @Nonnull
    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Nonnull
    @Override
    public HandlerList getHandlers() {
        return getHandlerList();
    }

}