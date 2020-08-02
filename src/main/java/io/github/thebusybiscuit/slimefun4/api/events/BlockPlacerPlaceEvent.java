package io.github.thebusybiscuit.slimefun4.api.events;

import org.apache.commons.lang.Validate;
import org.bukkit.block.Block;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockEvent;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.implementation.items.blocks.BlockPlacer;

/**
 * This {@link Event} is fired whenever a {@link BlockPlacer} wants to place a {@link Block}.
 * 
 * @author TheBusyBiscuit
 * 
 */
public class BlockPlacerPlaceEvent extends BlockEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private final Block blockPlacer;
    private ItemStack placedItem;
    private boolean cancelled = false;

    /**
     * This creates a new {@link BlockPlacerPlaceEvent}.
     * 
     * @param blockPlacer
     *            The {@link BlockPlacer}
     * @param block
     *            The placed {@link Block}
     */
    public BlockPlacerPlaceEvent(Block blockPlacer, ItemStack placedItem, Block block) {
        super(block);

        this.placedItem = placedItem;
        this.blockPlacer = blockPlacer;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    /**
     * This method returns the {@link BlockPlacer}
     *
     * @return The {@link BlockPlacer}
     */
    public Block getBlockPlacer() {
        return blockPlacer;
    }

    /**
     * This returns the placed {@link ItemStack}.
     * 
     * @return The placed {@link ItemStack}
     */
    public ItemStack getItemStack() {
        return placedItem;
    }

    /**
     * This sets the placed {@link ItemStack}.
     * 
     * @param item
     *            The {@link ItemStack} to be placed
     */
    public void setItemStack(ItemStack item) {
        Validate.notNull(item, "The ItemStack must not be null!");
        this.placedItem = item;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }

}