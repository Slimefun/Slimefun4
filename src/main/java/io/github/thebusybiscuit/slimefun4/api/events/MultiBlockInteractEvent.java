package io.github.thebusybiscuit.slimefun4.api.events;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

import io.github.thebusybiscuit.slimefun4.core.multiblocks.MultiBlock;

/**
 * This {@link Event} is called when a {@link Player} interacts with a {@link MultiBlock}.
 * 
 * @author TheBusyBiscuit
 *
 */
public class MultiBlockInteractEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private final MultiBlock multiBlock;
    private final Block clickedBlock;
    private final BlockFace clickedFace;
    private boolean cancelled;

    @ParametersAreNonnullByDefault
    public MultiBlockInteractEvent(Player p, MultiBlock mb, Block clicked, BlockFace face) {
        super(p);
        this.multiBlock = mb;
        this.clickedBlock = clicked;
        this.clickedFace = face;
    }

    /**
     * This method returns the {@link MultiBlock} which was interacted with.
     * 
     * @return The {@link MultiBlock} of this {@link MultiBlockInteractEvent}
     */
    @Nonnull
    public MultiBlock getMultiBlock() {
        return multiBlock;
    }

    /**
     * This returns the specific {@link Block} that was interacted with.
     * 
     * @return The {@link Block} that was clicked
     */
    @Nonnull
    public Block getClickedBlock() {
        return clickedBlock;
    }

    /**
     * This returns the {@link BlockFace} that was clicked.
     * 
     * @return The {@link BlockFace} that was clicked
     */
    @Nonnull
    public BlockFace getClickedFace() {
        return clickedFace;
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
