package me.mrCookieSlime.Slimefun.Events;

import me.mrCookieSlime.Slimefun.Objects.MultiBlock;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class MultiBlockInteractEvent extends Event implements Cancellable {
	
	private static final HandlerList handlers = new HandlerList();
	
	private Player p;
	private MultiBlock mb;
	private Block b;
	private boolean cancelled;
	
	public HandlerList getHandlers() {
	    return handlers;
	}
	
	public static HandlerList getHandlerList() {
        return handlers;
    }
	
	public MultiBlockInteractEvent(Player p, MultiBlock mb, Block clicked) {
		this.p = p;
		this.mb = mb;
		this.b = clicked;
	}
	
	public Player getPlayer() {
		return this.p;
	}
	
	public MultiBlock getMultiBlock() {
		return this.mb;
	}
	
	public Block getClickedBlock() {
		return this.b;
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
