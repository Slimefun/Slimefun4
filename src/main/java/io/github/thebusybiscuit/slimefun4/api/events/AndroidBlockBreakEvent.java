package io.github.thebusybiscuit.slimefun4.api.events;

import io.github.thebusybiscuit.slimefun4.implementation.items.androids.AndroidInstance;
import io.github.thebusybiscuit.slimefun4.implementation.items.androids.ProgrammableAndroid;
import org.bukkit.block.Block;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * This {@link Event} is fired before an {@link ProgrammableAndroid android} breaks a {@link Block}.
 * If this {@link Event} is cancelled, the {@link Block} will not be broken.
 *
 * @author JustAHuman, poma123 (creator of this classes predecessor)
 */
public class AndroidBlockBreakEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private final Block block;
    private final AndroidInstance android;
    private boolean cancelled = false;

    /**
     * @param block
     *            The {@link Block} to be broken
     * @param android
     *            The {@link AndroidInstance} that triggered this {@link AndroidBlockBreakEvent event}
     */
    @ParametersAreNonnullByDefault
    public AndroidBlockBreakEvent(Block block, AndroidInstance android) {
        this.block = block;
        this.android = android;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    /**
     * @return The broken {@link Block}
     */
    public @Nonnull Block getBlock() {
        return this.block;
    }

    /**
     * @return The {@link AndroidInstance android} who triggered this {@link AndroidBlockBreakEvent event}
     */
    public @Nonnull AndroidInstance getAndroid() {
        return this.android;
    }

    @Override
    public @Nonnull HandlerList getHandlers() {
        return getHandlerList();
    }

    public static @Nonnull HandlerList getHandlerList() {
        return handlers;
    }
}
