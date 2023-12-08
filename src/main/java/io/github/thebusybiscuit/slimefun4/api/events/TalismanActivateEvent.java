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
 * This {@link PlayerEvent} is called when a {@link Player} activates a {@link Talisman}
 * 
 * @author cworldstar
 */
public class TalismanActivateEvent extends PlayerEvent implements Cancellable {

	private static final HandlerList handlers = new HandlerList();
	private final Talisman talisman;
	private final ItemStack talismanItemStack;
	private boolean preventConsumption = false;
	private boolean cancelled = false;

	/**
	 * @param player
	 * 		The {@link Player} who activated the talisman.
	 * 
	 * @param talisman
	 * 		The {@link Talisman} that was activated.
	 * 
	 * @param talismanItem
	 * 		The {@link ItemStack} corresponding to the Talisman.
	 */
	@ParametersAreNonnullByDefault
	public TalismanActivateEvent(Player player, Talisman talisman, ItemStack talismanItem) {
		super(player);
		this.talisman = talisman;
		this.talismanItemStack = talismanItem;
	}

	/**
	 * @return The {@link Talisman} used.
	 */
	public @Nonnull Talisman getTalisman() {
		return this.talisman;
	}

	/**
	 * @return The {@link ItemStack} of the used {@link Talisman}.
	 */
	public @Nonnull ItemStack getTalismanItem() {
		return this.talismanItemStack;
	}

	/**
	 * Only applies if {@link Talisman#isConsumable()} is true.
	 * Defaults to false.
	 *
	 * @return Whether the {@link ItemStack} should not be consumed.
	 */
	public boolean preventsConsumption() {
		return this.preventConsumption;
	}

	/**
	 * Only applies if {@link Talisman#isConsumable()} is true.
	 *
	 * @param preventConsumption
	 * 		Whether the {@link ItemStack} should not be consumed.
	 */
	public void setPreventConsumption(boolean preventConsumption) {
		this.preventConsumption = preventConsumption;
	}

	@Override
	public boolean isCancelled() {
		return this.cancelled;
	}

	@Override
	public void setCancelled(boolean cancel) {
		this.cancelled = cancel;
	}

	@Override
	public @Nonnull HandlerList getHandlers() {
		return getHandlerList();
	}

	public static @Nonnull HandlerList getHandlerList() {
		return handlers;
	}
}