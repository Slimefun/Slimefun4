package io.github.thebusybiscuit.slimefun4.api.events;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.apache.commons.lang.Validate;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.ItemStack;

/**
 * This {@link Event} is called when a {@link TableSaw} is used.
 *
 * @author svr333
 * 
 * @see TableSaw
 *
 */
public class TableSawUsedEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private Block block;
    private ItemStack input;
    private ItemStack output;
    private boolean cancelled;

    @ParametersAreNonnullByDefault
    public TableSawUsedEvent(Player player, Block block, ItemStack input, ItemStack output) {
        super(player);

        Validate.notNull(block, "The block cannot be null!");
        Validate.notNull(input, "The input cannot be null!");
        Validate.notNull(output, "The output cannot be null!");

        this.block = block;
        this.input = input;
        this.output = output;
    }

    public Block getBlock() {
        return this.block;
    }

    public ItemStack getInputItems() {
        return this.input;
    }

    public ItemStack getOutputItems() {
        return this.output;
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
