package me.mrCookieSlime.Slimefun.services;

import java.util.Optional;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import io.github.thebusybiscuit.cscorelib2.data.PersistentDataAPI;

public class CustomItemDataService {
	
	private final NamespacedKey namespacedKey;
	
	public CustomItemDataService(Plugin plugin, String key) {
		namespacedKey = new NamespacedKey(plugin, key);
	}

	public void setItemData(ItemStack item, String id) {
		ItemMeta im = item.getItemMeta();
		setItemData(im, id);
		item.setItemMeta(im);
	}

	public void setItemData(ItemMeta im, String id) {
		PersistentDataAPI.setString(im, namespacedKey, id);
	}

	public Optional<String> getItemData(ItemStack item) {
		return getItemData(item.getItemMeta());
	}
	
	public Optional<String> getItemData(ItemMeta meta) {
		return PersistentDataAPI.getOptionalString(meta, namespacedKey);
	}

}
