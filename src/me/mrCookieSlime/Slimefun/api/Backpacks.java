package me.mrCookieSlime.Slimefun.api;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class Backpacks {
	
	public static String createBackpack(Player p, int size) {
		List<Integer> ids = new ArrayList<>();
		Config cfg = new Config(new File("data-storage/Slimefun/Players/" + p.getUniqueId() + ".yml"));
		for (int i = 0; i < 1000; i++) {
			if (cfg.contains("backpacks." + i + ".size")) ids.add(i);
			else break;
		}
		int id = ids.isEmpty() ? 0: ids.get(ids.size() - 1) + 1;
		ids.add(id);
		
		cfg.setValue("backpacks." + id + ".size", size);
		cfg.save();
		return p.getUniqueId() + "#" + id;
	}
	
	public static void openBackpack(Player p, ItemStack item) {
		Inventory inv = getInventory(p, item);
		if (inv != null) p.openInventory(inv);
	}
	
	public static Inventory getInventory(Player p, ItemStack item) {
		if (item == null || !item.hasItemMeta() || !item.getItemMeta().hasLore()) return null;
		int id = -1;
		String uuid = "";
		for (String line: item.getItemMeta().getLore()) {
			if (line.startsWith(ChatColor.translateAlternateColorCodes('&', "&7ID: ")) && line.contains("#")) {
				try {
					id = Integer.parseInt(line.split("#")[1]);
					uuid = line.split("#")[0].replace(ChatColor.translateAlternateColorCodes('&', "&7ID: "), "");
				} catch(NumberFormatException x) {
				}
			}
		}
		
		if (id >= 0) {
			Config cfg = new Config(new File("data-storage/Slimefun/Players/" + uuid + ".yml"));
			int size = cfg.getInt("backpacks." + id + ".size");
			Inventory inv = Bukkit.createInventory(null, size, "Backpack [" + size + " Slots]");
			for (int i = 0; i < size; i++) {
				inv.setItem(i, cfg.getItem("backpacks." + id + ".contents." + i));
			}
			return inv;
		}
		else return null;
	}
	
	public static void saveBackpack(Inventory inv, ItemStack item) {
		int id = -1;
		String uuid = "";
		for (String line: item.getItemMeta().getLore()) {
			if (line.startsWith(ChatColor.translateAlternateColorCodes('&', "&7ID: ")) && line.contains("#")) {
				try {
					id = Integer.parseInt(line.split("#")[1]);
					uuid = line.split("#")[0].replace(ChatColor.translateAlternateColorCodes('&', "&7ID: "), "");
				} catch(NumberFormatException x) {
				}
			}
		}
		
		if (id >= 0) {
			Config cfg = new Config(new File("data-storage/Slimefun/Players/" + uuid + ".yml"));
			for (int i = 0; i < inv.getContents().length; i++) {
				cfg.setValue("backpacks." + id + ".contents." + i, inv.getContents()[i]);
			}
			cfg.save();
		}
	}

}
