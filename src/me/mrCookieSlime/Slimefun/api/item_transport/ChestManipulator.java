package me.mrCookieSlime.Slimefun.api.item_transport;

import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

public final class ChestManipulator {

	private ChestManipulator() {}
	
	public static void registerListener(CargoTransportEvent listener) {
        SlimefunPlugin.getUtilities().cargoTransportEvents.add(listener);
	}
	
	public static ItemStack trigger(Block b, int slot, ItemStack prev, ItemStack next) {
		for (CargoTransportEvent listener: SlimefunPlugin.getUtilities().cargoTransportEvents) {
			next = listener.onEvent(b, slot, prev, next);
		}
		
		return next;
	}
	
}
