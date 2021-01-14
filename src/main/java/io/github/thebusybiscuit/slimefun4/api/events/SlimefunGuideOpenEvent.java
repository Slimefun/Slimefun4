package io.github.thebusybiscuit.slimefun4.api.events;

import javax.annotation.Nonnull;

import org.apache.commons.lang.Validate;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.core.guide.SlimefunGuideMode;

/**
 * This {@link Event} is called whenever a {@link Player} tries to open the Slimefun Guide book.
 * 
 * @author Linox
 *
 * @see SlimefunGuideMode
 */
public class SlimefunGuideOpenEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private final Player player;
    private final ItemStack guide;
    private SlimefunGuideMode layout;
    private boolean cancelled;

    public SlimefunGuideOpenEvent(@Nonnull Player p, @Nonnull ItemStack guide, @Nonnull SlimefunGuideMode layout) {
        Validate.notNull(p, "The Player cannot be null");
        Validate.notNull(guide, "Guide cannot be null");
        Validate.notNull(layout, "Layout cannot be null");
        this.player = p;
        this.guide = guide;
        this.layout = layout;
    }

    /**
     * This returns the {@link Player} that tries to open
     * the Slimefun Guide.
     *
     * @return The {@link Player}
     */
    @Nonnull
    public Player getPlayer() {
        return player;
    }

    /**
     * This returns the {@link ItemStack} that {@link Player}
     * tries to open the Slimefun Guide with.
     *
     * @return The {@link ItemStack}
     */
    @Nonnull
    public ItemStack getGuide() {
        return guide;
    }

    /**
     * This returns the {@link SlimefunGuideMode} of the Slimefun Guide
     * that {@link Player} tries to open.
     *
     * @return The {@link SlimefunGuideMode}
     */
    @Nonnull
    public SlimefunGuideMode getGuideLayout() {
        return layout;
    }

    /**
     * Changes the {@link SlimefunGuideMode} that was tried to be opened with.
     *
     * @param layout
     *            The new {@link SlimefunGuideMode}
     */
    public void setGuideLayout(@Nonnull SlimefunGuideMode layout) {
        Validate.notNull(layout, "You must specify a layout that is not-null!");
        this.layout = layout;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
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
