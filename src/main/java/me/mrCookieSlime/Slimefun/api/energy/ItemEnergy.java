package me.mrCookieSlime.Slimefun.api.energy;

import java.math.BigDecimal;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public final class ItemEnergy {
	
	private ItemEnergy() {}
	
	//	"&c&o&8\u21E8 &e\u26A1 &70 / 50 J"
	
	public static float getStoredEnergy(ItemStack item) {
		if (item == null || item.getType() == null || item.getType() == Material.AIR) return 0F;
		if (!item.hasItemMeta() || !item.getItemMeta().hasLore()) return 0F;
		
		for (String line: item.getItemMeta().getLore()) {
			if (line.startsWith(ChatColor.translateAlternateColorCodes('&', "&c&o&8\u21E8 &e\u26A1 &7")) && line.contains(" / ") && line.endsWith(" J")) {
				return Float.valueOf(line.split(" / ")[0].replace(ChatColor.translateAlternateColorCodes('&', "&c&o&8\u21E8 &e\u26A1 &7"), ""));
			}
		}

		return 0F;
	}

	public static float getMaxEnergy(ItemStack item) {
		if (item == null || item.getType() == null || item.getType() == Material.AIR) return 0F;
		if (!item.hasItemMeta() || !item.getItemMeta().hasLore()) return 0F;

		for (String line: item.getItemMeta().getLore()) {
			if (line.startsWith(ChatColor.translateAlternateColorCodes('&', "&c&o&8\u21E8 &e\u26A1 &7")) && line.contains(" / ") && line.endsWith(" J")) {
				return Float.valueOf(line.split(" / ")[1].replace(" J", ""));
			}
		}

		return 0F;
	}

	public static float addStoredEnergy(ItemStack item, float energy) {
		if (item == null || item.getType() == null || item.getType() == Material.AIR) return 0F;
		if (!item.hasItemMeta() || !item.getItemMeta().hasLore()) return 0F;

		float rest = 0F;
		float capacity = getMaxEnergy(item);

		if ((int) capacity == 0) {
			return rest;
		}

		float stored = getStoredEnergy(item);

		if (stored + energy > capacity) {
			rest = (stored + energy) - capacity;
			stored = capacity;
		}
		else if (stored + energy < 0) {
			stored = 0F;
		}
		else {
			stored = stored + energy;
		}

		List<String> lore = item.getItemMeta().getLore();

		int index = -1;
		for (int i = 0; i < lore.size(); i++) {
			String line = lore.get(i);
			if (line.startsWith(ChatColor.translateAlternateColorCodes('&', "&c&o&8\u21E8 &e\u26A1 &7")) && line.contains(" / ") && line.endsWith(" J")) {
				index = i;
				break;
			}
		}

		BigDecimal decimal = BigDecimal.valueOf(stored).setScale(2, BigDecimal.ROUND_HALF_UP);

		lore.set(index, ChatColor.translateAlternateColorCodes('&', "&c&o&8\u21E8 &e\u26A1 &7") + decimal.floatValue() + " / " + capacity + " J");
		
		ItemMeta im = item.getItemMeta();
		im.setLore(lore);
		item.setItemMeta(im);
		return rest;
	}
	
	public static ItemStack chargeItem(ItemStack item, float energy) {
		addStoredEnergy(item, energy);
		return item;
	}

	public static void chargeInventory(Player p, float energy) {
		p.getInventory().setItemInMainHand(chargeItem(p.getInventory().getItemInMainHand(), energy));
		p.getInventory().setItemInOffHand(chargeItem(p.getInventory().getItemInOffHand(), energy));
		p.getInventory().setHelmet(chargeItem(p.getInventory().getHelmet(), energy));
		p.getInventory().setChestplate(chargeItem(p.getInventory().getChestplate(), energy));
		p.getInventory().setLeggings(chargeItem(p.getInventory().getLeggings(), energy));
		p.getInventory().setBoots(chargeItem(p.getInventory().getBoots(), energy));
	}

}
