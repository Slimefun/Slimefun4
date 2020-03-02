package me.mrCookieSlime.Slimefun.Objects;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

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
 */
@FunctionalInterface
public interface SlimefunBlockHandler {
	
	/**
	 * This method gets called when the Block is placed.
	 * Use this method to initialize block data.
	 * 
	 * @param p		The Player who placed it
	 * @param b		The Block that was placed
	 * @param item	The Item that will be stored inside the Block
	 */
	default void onPlace(Player p, Block b, SlimefunItem item) {
		// This method can optionally be implemented by classes implementing it.
	}
	
	/**
	 * This method gets called when the Block is broken.
	 * The {@link Player} will be null if the Block is exploded
	 * 
	 * @param p			The Player who broke the Block
	 * @param b			The Block that was broken
	 * @param item		The SlimefunItem that was stored in that block
	 * @param reason	The reason for the Block breaking
	 * @return			Whether the Event should be cancelled
	 */
	boolean onBreak(Player p, Block b, SlimefunItem item, UnregisterReason reason);
}
