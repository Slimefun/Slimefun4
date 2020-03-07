package io.github.thebusybiscuit.slimefun4.api.events;

import org.bukkit.block.Block;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import io.github.thebusybiscuit.slimefun4.implementation.items.androids.AndroidInstance;

/**
 * This event is fired before a miner android mines a block.
 * If this event is cancelled, the block will not be mined.
 * 
 * @author poma123
 * 
 */
public class AndroidMineEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private final Block block;
    private final AndroidInstance android;
    private boolean cancelled;

    /**
     * @param block
     *            - mined block
     * @param android
     *            - the block of the android
     */
    public AndroidMineEvent(Block block, AndroidInstance android) {
        this.block = block;
        this.android = android;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    /**
     * This method returns the mined block
     *
     * @return the mined block
     */
    public Block getBlock() {
        return block;
    }

    /**
     * This method returns the block of the
     * android who wants to mine a block.
     *
     * @return the block of the android
     */
    public AndroidInstance getAndroid() {
        return android;
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