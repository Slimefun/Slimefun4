package org.bukkit.event.block;

import java.util.List;

import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * Compile-only stub for {@code org.bukkit.event.block.BlockDropItemEvent} (Minecraft 1.13+). Not
 * shaded; on modern servers the real concrete class is used at runtime. Stubbed as a class (not an
 * interface) so that virtual method calls resolve to {@code invokevirtual}, matching the real type.
 */
public class BlockDropItemEvent extends BlockEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    public BlockDropItemEvent(Block block, BlockState blockState, Player player, List<Item> items) {
        super(block);
    }

    public Player getPlayer() {
        return null;
    }

    public BlockState getBlockState() {
        return null;
    }

    public List<Item> getItems() {
        return null;
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public void setCancelled(boolean cancel) {
        // no-op stub
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
