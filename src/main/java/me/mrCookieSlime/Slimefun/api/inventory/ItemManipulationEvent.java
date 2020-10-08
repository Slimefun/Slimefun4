package me.mrCookieSlime.Slimefun.api.inventory;

import org.bukkit.inventory.ItemStack;

/**
 * @deprecated Please use {@link BlockMenuPreset#onItemStackChange(DirtyChestMenu, int, ItemStack, ItemStack)} instead.
 * 
 * @author TheBusyBiscuit
 *
 */
@Deprecated
@FunctionalInterface
public interface ItemManipulationEvent {

    ItemStack onEvent(int slot, ItemStack previous, ItemStack next);

}
