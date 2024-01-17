package io.github.thebusybiscuit.slimefun4.api.events;

import javax.annotation.Nonnull;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;


/**
 * This {@link Event} is fired after {@link Slimefun} finishes loading the
 * {@link SlimefunItem} registry before auto loading
 *
 * @author ProfElements
 */
public class SlimefunRegistryFinalizedEvent extends Event {
    
    private static final HandlerList handlers = new HandlerList();

    public SlimefunRegistryFinalizedEvent() {}

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
