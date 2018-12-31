package me.mrCookieSlime.Slimefun.api.item_transport;

import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

public interface CargoTransportEvent {
	
	public ItemStack onEvent(Block b, int slot, ItemStack previous, ItemStack next);

}
