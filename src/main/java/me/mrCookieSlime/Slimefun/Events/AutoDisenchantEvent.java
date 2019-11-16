package me.mrCookieSlime.Slimefun.Events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

public class AutoDisenchantEvent extends Event implements Cancellable {
	
	private static final HandlerList handlers = new HandlerList();

	private final ItemStack sfItem;
	private boolean cancelled;

	public HandlerList getHandlers() {
	    return handlers;
	}
	
	public static HandlerList getHandlerList() {
        return handlers;
    }
	
	public AutoDisenchantEvent(ItemStack sfitem){
		super(true);
		this.sfItem = sfitem;
	}
	
	public ItemStack getSfItem() {
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
