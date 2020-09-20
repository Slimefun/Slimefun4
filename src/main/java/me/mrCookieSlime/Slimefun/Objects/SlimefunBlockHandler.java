package me.mrCookieSlime.Slimefun.Objects;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

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
