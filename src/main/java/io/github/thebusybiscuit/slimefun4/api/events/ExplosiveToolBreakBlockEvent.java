package io.github.thebusybiscuit.slimefun4.api.events;

import io.github.thebusybiscuit.slimefun4.implementation.items.tools.ExplosiveTool;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * This {@link Event} is called when a {@link Block} is destroyed by an {@link ExplosiveTool}.
 *
 * @author GallowsDove
 *
 */
public class ExplosiveToolBreakBlockEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private final ItemStack tool;
    private final Block block;
    private boolean cancelled;

    @ParametersAreNonnullByDefault
    public ExplosiveToolBreakBlockEvent(Player player, Block block, ItemStack item) {
        super(player);
        this.block = block;
        this.tool = item;
    }

    /**
     * Gets the {@link ItemStack} of the tool used to destroy this block
     *
     */
    @Nonnull
    public ItemStack getTool() {
        return this.tool;
    }

    /**
     * Gets the {@link Block} destroyed in the event
     *
     */
    @Nonnull
    public Block getBlock() {
        return this.block;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        this.cancelled = true;
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
