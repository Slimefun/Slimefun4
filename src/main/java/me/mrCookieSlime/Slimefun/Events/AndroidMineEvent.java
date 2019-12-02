package me.mrCookieSlime.Slimefun.Events;

import org.bukkit.block.Block;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * This event is fired before a miner android mines a block.
 * If this event is cancelled, the block will not be mined.
 */
public class AndroidMineEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private final Block block;
    private final Block android;
    private boolean cancelled;

    /**
     * @param block - mined block
     * @param android - the block of the android
     */
    public AndroidMineEvent(Block block, Block android) {
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
        return this.block;
    }

    /**
     * This method returns the block of the
     * android who wants to mine a block.
     *
     * @return the block of the android
     */
    public Block getAndroid() {
        return this.android;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

}