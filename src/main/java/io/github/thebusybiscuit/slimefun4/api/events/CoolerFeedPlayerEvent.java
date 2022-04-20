package io.github.thebusybiscuit.slimefun4.api.events;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.apache.commons.lang.Validate;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;

import io.github.thebusybiscuit.slimefun4.implementation.items.backpacks.Cooler;

/**
 * This {@link Event} is called whenever a {@link Player} is
 * fed through a {@link Cooler}.
 *
 * @author TheBusyBiscuit
 *
 * @see Cooler
 *
 */
public class CoolerFeedPlayerEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private final Cooler cooler;
    private final ItemStack coolerItem;

    private ItemStack consumedItem;
    private boolean cancelled;

    @ParametersAreNonnullByDefault
    public CoolerFeedPlayerEvent(Player player, Cooler cooler, ItemStack coolerItem, ItemStack consumedItem) {
        super(player);

        this.cooler = cooler;
        this.coolerItem = coolerItem;
        this.consumedItem = consumedItem;
    }

    /**
     * This returns the {@link Cooler} that was used.
     *
     * @return The {@link Cooler} that was used
     */
    @Nonnull
    public Cooler getCooler() {
        return cooler;
    }

    /**
     * This returns the {@link Cooler} that was used (as an {@link ItemStack})
     *
     * @return The {@link Cooler} that was used
     */
    @Nonnull
    public ItemStack getCoolerItem() {
        return coolerItem;
    }

    /**
     * This returns the {@link ItemStack} that was consumed.
     * The returned {@link ItemStack} is immutable.
     *
     * @return The {@link ItemStack} that was consumed
     */
    @Nonnull
    public ItemStack getConsumedItem() {
        return consumedItem.clone();
    }

    /**
     * This sets the {@link ItemStack} that should be "consumed".
     * The {@link ItemStack} must be a potion.
     * The {@link Player} will receive the {@link PotionEffect PotionEffects} of the
     * provided potion upon consumption.
     * 
     * @param item
     *            The new {@link ItemStack}
     */
    public void setConsumedItem(@Nonnull ItemStack item) {
        Validate.notNull(item, "The consumed Item cannot be null!");
        Validate.isTrue(item.getItemMeta() instanceof PotionMeta, "The item must be a potion!");

        this.consumedItem = item;
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
