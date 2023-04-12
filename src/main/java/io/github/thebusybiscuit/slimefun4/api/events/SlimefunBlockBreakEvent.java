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
import io.github.thebusybiscuit.slimefun4.implementation.items.blocks.BlockPlacer;

/**
 * This {@link Event} is fired whenever a {@link SlimefunItem} placed as a {@link Block} in the world is broken.
 * 
 * @author J3fftw1
 */
public class SlimefunBlockBreakEvent extends BlockEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private final Block blockBroken;
    private final SlimefunItem slimefunItem;
    private final ItemStack brokenItem;
    private final Player player;

    private boolean cancelled = false;


    /**
     * @param player
     *        The {@link Player} who broke this {@link SlimefunItem}
     * @param brokenItem
     *        The {@link ItemStack} held by the {@link Player}
     * @param blockBroken
     *        The {@link Block} broken by the {@link Player}
     * @param slimefunItem
     *        The {@link SlimefunItem} within the {@link ItemStack}
     */
    @ParametersAreNonnullByDefault
    public SlimefunBlockBreakEvent(Player player, ItemStack brokenItem, Block blockBroken, SlimefunItem slimefunItem) {
        super(blockBroken);
        
        this.player = player;
        this.brokenItem = brokenItem;
        this.blockBroken = blockBroken;
        this.slimefunItem = slimefunItem;
    }

    /**
     * This gets the broken {@link Block}
     * 
     * @return The broken {@link Block}
     */
    @Nonnull
    public Block getBlockBroken() {
        return blockBroken;
    }

    /**
     * This gets the {@link SlimefunItem}
     * 
     * @return The {@link SlimefunItem}
     */
    @Nonnull
    public SlimefunItem getSlimefunItem() {
        return slimefunItem;
    }

    /**
     * This gets the broken {@link ItemStack}.
     * 
     * @return The broken {@link ItemStack}
     */
    @Nonnull
    public ItemStack getItemStack() {
        return brokenItem;
    }

    /**
     * This gets the {@link Player}
     * 
     * @return The {@link Player}
     */
    @Nonnull
    public Player getPlayer() {
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
