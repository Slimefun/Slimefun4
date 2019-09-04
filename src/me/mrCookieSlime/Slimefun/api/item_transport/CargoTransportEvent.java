package me.mrCookieSlime.Slimefun.api.item_transport;

import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

@FunctionalInterface
public interface CargoTransportEvent {
	
	ItemStack onEvent(Block b, int slot, ItemStack previous, ItemStack next);
	
}
