package io.github.thebusybiscuit.slimefun4.api.events;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;

/**
 * This {@link Event} is fired whenever a {@link SlimefunItem} is placed as a {@link Block} in the world.
 * 
 * @author J3fftw1
 */
public class SlimefunBlockPlaceEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private final Block blockPlaced;
    private final SlimefunItem slimefunItem;
    private final ItemStack placedItem;
    private final Player player;

    private boolean cancelled = false;

    /**
     * @param player
     *        The {@link Player} who placed this {@link SlimefunItem}
     * @param placedItem
     *        The {@link ItemStack} held by the {@link Player}
     * @param blockPlaced
     *        The {@link Block} placed by the {@link Player}
     * @param slimefunItem
     *        The {@link SlimefunItem} within the {@link ItemStack}
     */
    @ParametersAreNonnullByDefault
    public SlimefunBlockPlaceEvent(Player player, ItemStack placedItem, Block blockPlaced, SlimefunItem slimefunItem) {
        super();

        this.player = player;
        this.placedItem = placedItem;
        this.blockPlaced = blockPlaced;
        this.slimefunItem = slimefunItem;
    }

    /**
     * This gets the placed {@link Block}
     *
     * @return The placed {@link Block}
     */
    public @Nonnull Block getBlockPlaced() {
        return blockPlaced;
    }

    /**
     * This gets the {@link SlimefunItem} being placed
     *
     * @return The {@link SlimefunItem} being placed
     */
    public @Nonnull SlimefunItem getSlimefunItem() {
        return slimefunItem;
    }

    /**
     * This gets the placed {@link ItemStack}.
     * 
     * @return The placed {@link ItemStack}
     */
    public @Nonnull ItemStack getItemStack() {
        return placedItem;
    }

    /**
     * This gets the {@link Player}
     *
     * @return The {@link Player}
     */
    public @Nonnull Player getPlayer() {
        return player;
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
