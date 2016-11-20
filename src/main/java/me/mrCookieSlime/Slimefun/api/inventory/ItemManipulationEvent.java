package me.mrCookieSlime.Slimefun.api.inventory;

import org.bukkit.inventory.ItemStack;

public interface ItemManipulationEvent {
	
	public void onEvent(int slot, ItemStack previous, ItemStack next);

}
