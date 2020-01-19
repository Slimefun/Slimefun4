package io.github.thebusybiscuit.slimefun4.api.items;

import java.util.Collection;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

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
