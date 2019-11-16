package me.mrCookieSlime.Slimefun.Events;

import me.mrCookieSlime.Slimefun.Objects.MultiBlock;

import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class DisenchanterEvent extends Event implements Cancellable {
	
	private static final HandlerList handlers = new HandlerList();
	

	private SlimefunItem sfItem;
	private boolean cancelled;

	public HandlerList getHandlers() {
	    return handlers;
	}
	
	public static HandlerList getHandlerList() {
        return handlers;
    }
	
	public DisenchanterEvent(SlimefunItem sfItem) {
		this.sfItem = sfItem;
	}
	
	public SlimefunItem getSfItem() {
		return this.sfItem;
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
