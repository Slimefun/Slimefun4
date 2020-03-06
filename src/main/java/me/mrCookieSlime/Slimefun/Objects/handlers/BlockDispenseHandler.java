package me.mrCookieSlime.Slimefun.Objects.handlers;

import org.bukkit.block.Block;
import org.bukkit.block.Dispenser;
import org.bukkit.event.block.BlockDispenseEvent;

import io.github.thebusybiscuit.slimefun4.implementation.items.blocks.BlockPlacer;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;

/**
 * This {@link ItemHandler} is triggered when the {@link SlimefunItem} it was assigned to
 * is a {@link Dispenser} and was triggered.
 * 
 * This {@link ItemHandler} is used for the {@link BlockPlacer}.
 * 
 * @author TheBusyBiscuit
 *
 * @see ItemHandler
 * @see BlockPlacer
 * 
 */
@FunctionalInterface
public interface BlockDispenseHandler extends ItemHandler {

    void onBlockDispense(BlockDispenseEvent e, Dispenser dispenser, Block facedBlock, SlimefunItem machine);

    @Override
    default Class<? extends ItemHandler> getIdentifier() {
        return BlockDispenseHandler.class;
    }
}
