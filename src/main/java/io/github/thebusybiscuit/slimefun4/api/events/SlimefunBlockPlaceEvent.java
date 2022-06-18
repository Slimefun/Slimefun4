package io.github.thebusybiscuit.slimefun4.api.events;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.implementation.items.blocks.BlockPlacer;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockEvent;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * This {@link Event} is fired whenever a {@link BlockPlacer} wants to place a {@link Block}.
 * 
 * @author J3fftw1
 * 
 */
public class SlimefunBlockPlaceEvent extends BlockEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private final Block blockPlaced;
    private final SlimefunItem slimefunItem;
    private final ItemStack placedItem;
    private final Player player;

    private boolean cancelled = false;


    /**
     *
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
        super(blockPlaced);

        this.player = player;
        this.placedItem = placedItem;
        this.blockPlaced = blockPlaced;
        this.slimefunItem = slimefunItem;
    }

    /**
     * This returns the placed {@link Block}
     *
     * @return The placed {@link Block}
     */
    @Nonnull
    public Block getBlockPlaced() {
        return blockPlaced;
    }

    /**
     * This returns the {@link SlimefunItem}
     *
     * @return The {@link SlimefunItem}
     */
    @Nonnull
    public SlimefunItem getSlimefunItem() {
        return slimefunItem;
    }

    /**
     * This returns the placed {@link ItemStack}.
     * 
     * @return The placed {@link ItemStack}
     */
    @Nonnull
    public ItemStack getItemStack() {
        return placedItem;
    }

    /**
     * This returns the {@link Player}
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