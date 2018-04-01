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
				if (((!p.getInventory().getHelmet().isSimilar(item)) &&
				(!p.getInventory().getChestplate().isSimilar(item)) &&
				(!p.getInventory().getLeggings().isSimilar(item)) &&
				(!p.getInventory().getBoots().isSimilar(item)) &&
				(!p.getInventory().getItemInOffHand().isSimilar(item))) &&
				(!p.getInventory().contains(item))) {
					p.getInventory().addItem(item);
				}				
			}
			Variables.soulbound.remove(p.getUniqueId());
		}
	}

}
