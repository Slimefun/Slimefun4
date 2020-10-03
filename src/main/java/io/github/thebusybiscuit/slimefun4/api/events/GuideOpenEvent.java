package io.github.thebusybiscuit.slimefun4.api.events;

import javax.annotation.Nonnull;

import org.apache.commons.lang.Validate;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

/**
 * This {@link Event} is called whenever a {@link Player} tries to open the Slimefun Guide book. 
 * 
 * @author Linox
 *
 */
public class GuideOpenEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private final Player player;
    private final ItemStack guide;
    private boolean cancelled;

    public GuideOpenEvent(@Nonnull Player p, @Nonnull ItemStack guide) {
        Validate.notNull(p, "The Player cannot be null");
        Validate.notNull(guide, "Guide cannot be null");
        this.player = p;
        this.guide = guide;
    }

    @Nonnull
    public Player getPlayer() {
        return player;
    }

    @Nonnull
    public ItemStack getGuide() {
        return guide;
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
