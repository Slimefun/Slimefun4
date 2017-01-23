package me.mrCookieSlime.Slimefun.api.inventory;

import org.bukkit.inventory.ItemStack;

public interface ItemManipulationEvent {
	
	public ItemStack onEvent(int slot, ItemStack previous, ItemStack next);

}
