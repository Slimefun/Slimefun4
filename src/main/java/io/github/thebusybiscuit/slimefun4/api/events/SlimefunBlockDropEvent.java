package io.github.thebusybiscuit.slimefun4.api.events;

import org.apache.commons.lang.Validate;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockEvent;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * This {@link Event} is called whenever a {@link Player} breaks a Slimefun block which should drop some {@link ItemStack}.
 *
 * @author CarmJos
 * @see io.github.thebusybiscuit.slimefun4.implementation.listeners.BlockListener
 */
public class SlimefunBlockDropEvent extends BlockEvent {

    private static final HandlerList handlers = new HandlerList();

    private final Player player;
    private List<ItemStack> drops;

    public SlimefunBlockDropEvent(Player player, Block theBlock, List<ItemStack> drops) {
        super(theBlock);
        this.player = player;
        this.drops = drops;
    }


    /**
     * This returns the {@link Player} that broke the block.
     *
     * @return The {@link Player}
     */
    @Nonnull
    public Player getPlayer() {
        return player;
    }

    /**
     * Gets the {@link List} of {@link ItemStack}s that should be dropped.
     *
     * @return The {@link List} of {@link ItemStack}s
     */
    @Nonnull
    public List<ItemStack> getDrops() {
        return drops;
    }

    /**
     * Sets the {@link List} of {@link ItemStack}s that should be dropped.
     *
     * @param drops The {@link List} of {@link ItemStack}s
     */
    public void setDrops(@Nonnull List<ItemStack> drops) {
        Validate.notNull(drops, "The drops list should not be null!");
        this.drops = drops;
    }

    @Nonnull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }


}
