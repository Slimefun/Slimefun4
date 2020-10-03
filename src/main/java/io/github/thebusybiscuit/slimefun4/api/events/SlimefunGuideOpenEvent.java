package io.github.thebusybiscuit.slimefun4.api.events;

import javax.annotation.Nonnull;

import org.apache.commons.lang.Validate;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.core.guide.SlimefunGuideLayout;

/**
 * This {@link Event} is called whenever a {@link Player} tries to open the Slimefun Guide book. 
 * 
 * @author Linox
 *
 * @see SlimefunGuideLayout
 */
public class SlimefunGuideOpenEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private final Player player;
    private final ItemStack guide;
    private final SlimefunGuideLayout layout;
    private boolean cancelled;

    public SlimefunGuideOpenEvent(@Nonnull Player p, @Nonnull ItemStack guide, @Nonnull SlimefunGuideLayout layout) {
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
     */
    @Nonnull
    public Player getPlayer() {
        return player;
    }

    /**
     * This returns the {@link ItemStack} that {@link Player} 
     * tries to open the Slimefun Guide with.
     */
    @Nonnull
    public ItemStack getGuide() {
        return guide;
    }
    
    /**
     * This returns the {@link SlimefunGuideLayout} of the Slimefun Guide
     * that {@link Player} tries to open.
     */
    @Nonnull
    public SlimefunGuideLayout getGuideLayout() {
        return layout;
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
