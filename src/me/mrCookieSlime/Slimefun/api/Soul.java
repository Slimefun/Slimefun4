package me.mrCookieSlime.Slimefun.api;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.Slimefun.SlimefunStartup;

@Deprecated
public class Soul {
	
	public static void storeItem(UUID uuid, ItemStack drop) {
		List<ItemStack> items = new ArrayList<>();
		if (SlimefunStartup.instance.getUtilities().soulbound.containsKey(uuid)) items = SlimefunStartup.instance.getUtilities().soulbound.get(uuid);
		items.add(drop);
		SlimefunStartup.instance.getUtilities().soulbound.put(uuid, items);
	}
	
	public static void retrieveItems(Player p) {
		if (SlimefunStartup.instance.getUtilities().soulbound.containsKey(p.getUniqueId())) {
			for (ItemStack item: SlimefunStartup.instance.getUtilities().soulbound.get(p.getUniqueId())) {
				if (item.equals(p.getInventory().getHelmet())) continue;
				if (item.equals(p.getInventory().getChestplate())) continue;
				if (item.equals(p.getInventory().getLeggings())) continue;
				if (item.equals(p.getInventory().getBoots())) continue;
				if (item.equals(p.getInventory().getItemInOffHand())) continue;

				if(!p.getInventory().contains(item)) {
					p.getInventory().addItem(item);
				}
			}
			SlimefunStartup.instance.getUtilities().soulbound.remove(p.getUniqueId());
		}
	}

}
