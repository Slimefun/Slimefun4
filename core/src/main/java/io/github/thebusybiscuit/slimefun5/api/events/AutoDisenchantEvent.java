package io.github.thebusybiscuit.slimefun5.api.events;

import javax.annotation.Nonnull;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun5.implementation.items.electric.machines.enchanting.AutoDisenchanter;

/**
 * An {@link Event} that is called whenever an {@link AutoDisenchanter} has
 * disenchanted an {@link ItemStack}.
 * 
 * @author poma123
 *
 * @see AutoEnchantEvent
 */
public class AutoDisenchantEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private final ItemStack item;
    private boolean cancelled;

    public AutoDisenchantEvent(@Nonnull ItemStack item) {
        // Thread-adaptive: a VIEWED machine ticks on the main thread while unviewed ticks are async;
        // Bukkit rejects an async-flagged event fired sync (and vice-versa), so match the current thread.
        super(!org.bukkit.Bukkit.isPrimaryThread());

        this.item = item;
    }

    /**
     * This returns the {@link ItemStack} that is being disenchanted.
     * 
     * @return The {@link ItemStack} that is being disenchanted
     */
    @Nonnull
    public ItemStack getItem() {
        return item;
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

