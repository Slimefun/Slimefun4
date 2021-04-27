package io.github.thebusybiscuit.slimefun4.implementation.handlers;

import java.util.List;

import javax.annotation.Nonnull;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.events.AndroidMineEvent;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockBreakHandler;
import io.github.thebusybiscuit.slimefun4.implementation.items.androids.MinerAndroid;

/**
 * This simple implementation of {@link BlockBreakHandler} will execute the same code
 * for when the {@link Block} is broken by a {@link Player}, by a {@link MinerAndroid} or an explosion.
 * By default, both androids and explosions are allowed.
 * 
 * @author TheBusyBiscuit
 * 
 * @see BlockBreakHandler
 *
 */
public abstract class SimpleBlockBreakHandler extends BlockBreakHandler {

    /**
     * This constructs a new {@link SimpleBlockBreakHandler}.
     */
    protected SimpleBlockBreakHandler() {
        super(true, true);
    }

    /**
     * This method is called when a {@link Block} of this type is broken by a {@link Player},
     * by a {@link MinerAndroid} or through an explosion.
     * 
     * @param b
     *            The broken {@link Block}
     */
    public abstract void onBlockBreak(@Nonnull Block b);

    @Override
    public void onPlayerBreak(BlockBreakEvent e, ItemStack item, List<ItemStack> drops) {
        onBlockBreak(e.getBlock());
    }

    @Override
    public void onAndroidBreak(AndroidMineEvent e) {
        onBlockBreak(e.getBlock());
    }

    @Override
    public void onExplode(Block b, List<ItemStack> drops) {
        onBlockBreak(b);
    }

}
