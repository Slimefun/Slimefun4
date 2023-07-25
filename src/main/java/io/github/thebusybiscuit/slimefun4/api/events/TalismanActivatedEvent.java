package io.github.thebusybiscuit.slimefun4.api.events;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.implementation.items.magical.talismans.Talisman;

/**
 * 
 * This {@link PlayerEvent} is called when a {@link Player} activates a {@link Talisman}
 * 
 * @author cworldstar
 *
 */

public class TalismanActivatedEvent extends PlayerEvent implements Cancellable {

	private static HandlerList Handlers = new HandlerList();
	private Talisman Talisman;
	private ItemStack TalismanItem;
	private boolean cancelled = false;
	
	
	/**
	 * 
	 * @param who
	 * 		The {@link Player} who activated the talisman.
	 * 
	 * @param talisman
	 * 		The {@link Talisman} that was activated.
	 * 
	 * @param talismanItem
	 * 		The {@link ItemStack} corresponding to the Talisman.
	 */
	@ParametersAreNonnullByDefault
	public TalismanActivatedEvent(Player who, Talisman talisman, ItemStack talismanItem) {
		super(who);
		this.Talisman = talisman;
		this.TalismanItem = talismanItem;
		// TODO Auto-generated constructor stub
	}
	/**
	 * 
	 * @return The {@link Talisman} used.
	 *
	 */
	@Nonnull
	public Talisman getTalisman() {
		return this.Talisman;
	}
	/**
	 * 
	 * @return The {@link ItemStack} of the used {@link Talisman}.
	 */
	@Nonnull
	public ItemStack getTalismanItem() {
		return this.TalismanItem;
	}

	public static HandlerList getHandlerList() {
		return Handlers;
	}
	
	@Override
	public HandlerList getHandlers() {
		// TODO Auto-generated method stub
		return getHandlerList();
	}
	@Override
	public boolean isCancelled() {
		// TODO Auto-generated method stub
		return this.cancelled;
	}
	@Override
	public void setCancelled(boolean cancel) {
		// TODO Auto-generated method stub
		this.cancelled = cancel;
	}

	
	
}
