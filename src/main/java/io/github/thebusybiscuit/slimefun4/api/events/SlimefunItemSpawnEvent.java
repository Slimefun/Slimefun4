package io.github.thebusybiscuit.slimefun4.api.events;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.apache.commons.lang.Validate;
import org.bukkit.Location;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.items.ItemSpawnReason;

/**
 * This {@link Event} is fired whenever slimefun drops an {@link ItemStack}.
 * Creating a custom {@link Event} for this allows other plugins to provide
 * compatibility with auto-pickup options or similar.
 *
 * @author TheBusyBiscuit
 * 
 * @see ItemSpawnReason
 */
public class SlimefunItemSpawnEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private Location location;
    private ItemStack itemStack;
    private boolean cancelled;
    private final ItemSpawnReason itemSpawnReason;

    @ParametersAreNonnullByDefault
    public SlimefunItemSpawnEvent(Location location, ItemStack itemStack, ItemSpawnReason itemSpawnReason) {
        this.location = location;
        this.itemStack = itemStack;
        this.itemSpawnReason = itemSpawnReason;
        this.cancelled = false;
    }

    /**
     * This returns the {@link ItemSpawnReason} why we dropped an {@link ItemStack}.
     * 
     * @return the {@link ItemSpawnReason}.
     */
    public @Nonnull ItemSpawnReason getItemSpawnReason() {
        return itemSpawnReason;
    }

    /**
     * This returns the {@link Location} where we will drop the item.
     * 
     * @return The {@link Location} where the item will be dropped
     */
    public @Nonnull Location getLocation() {
        return location;
    }

    /**
     * This sets the {@link Location} on where to drop this item.
     * 
     * @param location
     *            The {@link Location} where to drop the {@link ItemStack}
     */
    public void setLocation(@Nonnull Location location) {
        Validate.notNull(location, "The Location cannot be null!");

        this.location = location;
    }

    /**
     * This returns the {@link ItemStack} that will be dropped.
     * 
     * @return The {@link ItemStack} that will be dropped
     */
    public @Nonnull ItemStack getItemStack() {
        return itemStack;
    }

    /**
     * This method sets the {@link ItemStack} that should be dropped.
     * 
     * @param itemStack
     *            The {@link ItemStack} to drop
     */
    public void setItemStack(@Nonnull ItemStack itemStack) {
        Validate.notNull(itemStack, "Cannot drop null.");
        Validate.isTrue(!itemStack.getType().isAir(), "Cannot drop air.");

        this.itemStack = itemStack;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public static @Nonnull HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public @Nonnull HandlerList getHandlers() {
        return getHandlerList();
    }
}
