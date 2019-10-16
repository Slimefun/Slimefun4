package me.mrCookieSlime.Slimefun.api.inventory;

import org.bukkit.inventory.ItemStack;

@FunctionalInterface
public interface ItemManipulationEvent {
	
	ItemStack onEvent(int slot, ItemStack previous, ItemStack next);

}
