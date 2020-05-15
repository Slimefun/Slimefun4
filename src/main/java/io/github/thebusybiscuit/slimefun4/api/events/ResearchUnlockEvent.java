package io.github.thebusybiscuit.slimefun4.api.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import io.github.thebusybiscuit.slimefun4.core.researching.Research;

/**
 * This {@link Event} is called whenever a {@link Player} unlocks a {@link Research}.
 * 
 * @author TheBusyBiscuit
 * 
 * @see Research
 *
 */
public class ResearchUnlockEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private Player player;
    private Research research;
    private boolean cancelled;

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public ResearchUnlockEvent(Player p, Research research) {
        this.player = p;
        this.research = research;
    }

    public Player getPlayer() {
        return this.player;
    }

    public Research getResearch() {
        return this.research;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

}
