package me.mrCookieSlime.Slimefun.api.item_transport;

import java.util.ArrayList;
import java.util.List;

import me.mrCookieSlime.Slimefun.SlimefunStartup;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

public final class ChestManipulator {

	private ChestManipulator() {}
	
	public static void registerListener(CargoTransportEvent listener) {
        SlimefunStartup.instance.getUtilities().cargoTransportEvents.add(listener);
	}
	
	public static ItemStack trigger(Block b, int slot, ItemStack prev, ItemStack next) {
		for (CargoTransportEvent listener: SlimefunStartup.instance.getUtilities().cargoTransportEvents) {
			next = listener.onEvent(b, slot, prev, next);
		}
		
		return next;
	}
	
}
