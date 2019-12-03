package io.github.thebusybiscuit.slimefun4.api.items;

import java.util.Collection;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface Placeable {
	
	boolean isTicking();
	
	Collection<ItemStack> getDrops();
	Collection<ItemStack> getDrops(Player p);

}
