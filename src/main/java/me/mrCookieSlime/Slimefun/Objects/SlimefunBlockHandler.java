package me.mrCookieSlime.Slimefun.Objects;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import io.github.thebusybiscuit.slimefun4.core.handlers.BlockPlaceHandler;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.UnregisterReason;

/**
 * A {@link SlimefunBlockHandler} handles breaking and placing of blocks.
 * You can use this class to initialize block data but also to correctly
 * destroy blocks.
 * 
 * {@code SlimefunItem.registerBlockHandler(String, SlimefunBlockHandler); }
 * 
 * @author TheBusyBiscuit
 * 
 * 
 */
@FunctionalInterface
public interface SlimefunBlockHandler {

    /**
     * This method gets called when the {@link Block} is placed.
     * Use this method to initialize block data.
     * 
     * @deprecated Use a {@link BlockPlaceHandler} instead
     * 
     * @param p
     *            The {@link Player} who placed it
     * @param b
     *            The {@link Block} that was placed
     * @param item
     *            The {@link SlimefunItem} that will be stored inside the {@link Block}
     */
    @Deprecated
    default void onPlace(Player p, Block b, SlimefunItem item) {
        // This has been deprecated
    }

    /**
     * This method gets called when the {@link Block} is broken.
     * The {@link Player} will be null if the {@link Block} exploded
     * 
     * @param p
     *            The {@link Player} who broke the {@link Block}
     * @param b
     *            The {@link Block} that was broken
     * @param item
     *            The {@link SlimefunItem} that was stored in that {@link Block}
     * @param reason
     *            The reason for the {@link Block} breaking
     * @return Whether the {@link Event} should be cancelled
     */
    boolean onBreak(Player p, Block b, SlimefunItem item, UnregisterReason reason);
}
