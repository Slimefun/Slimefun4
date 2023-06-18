package io.github.thebusybiscuit.slimefun4.api.events;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockEvent;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;

/**
 * This {@link Event} is fired whenever a {@link SlimefunItem} placed as a {@link Block} in the world is broken.
 * 
 * @author J3fftw1
 */
public class SlimefunBlockBreakEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private final Block blockBroken;
    private final SlimefunItem slimefunItem;
    private final ItemStack heldItem;
    private final Player player;

    private boolean cancelled = false;

    /**
     * @param player
     *        The {@link Player} who broke this {@link SlimefunItem}
     * @param heldItem
     *        The {@link ItemStack} held by the {@link Player}
     * @param blockBroken
     *        The {@link Block} broken by the {@link Player}
     * @param slimefunItem
     *        The {@link SlimefunItem} within the {@link ItemStack}
     */
    @ParametersAreNonnullByDefault
    public SlimefunBlockBreakEvent(Player player, ItemStack heldItem, Block blockBroken, SlimefunItem slimefunItem) {
        super();
        
        this.player = player;
        this.heldItem = heldItem;
        this.blockBroken = blockBroken;
        this.slimefunItem = slimefunItem;
    }

    /**
     * This gets the broken {@link Block}
     * 
     * @return The broken {@link Block}
     */
    public @Nonnull Block getBlockBroken() {
        return blockBroken;
    }

    /**
     * This gets the {@link SlimefunItem} being broken
     * 
     * @return The {@link SlimefunItem} being broken
     */
    public @Nonnull SlimefunItem getSlimefunItem() {
        return slimefunItem;
    }

    /**
     * The {@link ItemStack} held by the {@link Player}
     * 
     * @return The held {@link ItemStack}
     */
    public @Nonnull ItemStack getHeldItem() {
        return heldItem;
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
