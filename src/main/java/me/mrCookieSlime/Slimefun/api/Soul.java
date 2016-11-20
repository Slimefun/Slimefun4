package me.mrCookieSlime.Slimefun.api;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import me.mrCookieSlime.Slimefun.Variables;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Soul {
	
	public static void storeItem(UUID uuid, ItemStack drop) {
		List<ItemStack> items = new ArrayList<ItemStack>();
		if (Variables.soulbound.containsKey(uuid)) items = Variables.soulbound.get(uuid);
		items.add(drop);
		Variables.soulbound.put(uuid, items);
	}
	
	public static void retrieveItems(Player p) {
		if (Variables.soulbound.containsKey(p.getUniqueId())) {
			for (ItemStack item: Variables.soulbound.get(p.getUniqueId())) {
				p.getInventory().addItem(item);
			}
			Variables.soulbound.remove(p.getUniqueId());
		}
	}

}
