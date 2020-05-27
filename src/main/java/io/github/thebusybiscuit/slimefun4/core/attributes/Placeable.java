package io.github.thebusybiscuit.slimefun4.core.attributes;

import java.util.Collection;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;

/**
 * DO NOT IMPLEMENT THIS INTERFACE.
 * This is implemented by every {@link SlimefunItem} by default.
 * Might be changed in the future.
 * 
 * @author TheBusyBiscuit
 *
 */
public interface Placeable {

    Collection<ItemStack> getDrops();

    Collection<ItemStack> getDrops(Player p);

    default void onPlace(Player p, Block b) {
        // Override this as necessary
    }

    default boolean onBreak(Player p, Block b) {
        // Override this as necessary
        return true;
    }

    default boolean onExplode(Block b) {
        // Override this as necessary
        return true;
    }

}
