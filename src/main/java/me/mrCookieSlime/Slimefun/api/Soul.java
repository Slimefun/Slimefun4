package me.mrCookieSlime.Slimefun.api;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.Slimefun.SlimefunPlugin;

public final class Soul {
	
	private Soul() {}
	
	public static void storeItem(UUID uuid, int slot, ItemStack item) {
		Map<Integer, ItemStack> items = SlimefunPlugin.getUtilities().soulbound.get(uuid);
		
		if (items == null) {
			items = new HashMap<>();
			SlimefunPlugin.getUtilities().soulbound.put(uuid, items);
		}
		
		items.put(slot, item);
	}
	
	public static void retrieveItems(Player p) {
		Map<Integer, ItemStack> items = SlimefunPlugin.getUtilities().soulbound.get(p.getUniqueId());
		
		if (items != null) {
			for (Map.Entry<Integer, ItemStack> entry : items.entrySet()) {
				p.getInventory().setItem(entry.getKey(), entry.getValue());
			}
		}
		
		SlimefunPlugin.getUtilities().soulbound.remove(p.getUniqueId());
	}

}
