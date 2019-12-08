package io.github.thebusybiscuit.slimefun4.core.services;

import java.util.Collection;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import io.github.thebusybiscuit.cscorelib2.config.Config;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;

public class CustomTextureService {
	
	private final Config config;
	
	public CustomTextureService(Plugin plugin) {
		this.config = new Config(plugin, "item-models.yml");
	}
	
	public void setup(Collection<SlimefunItem> items) {
		config.setDefaultValue("SLIMEFUN_GUIDE", 0);
		
		for (SlimefunItem item : items) {
			if (item != null && item.getID() != null) {
				config.setDefaultValue(item.getID(), 0);
			}
		}
		
		config.save();
	}
	
	public int getModelData(String id) {
		return config.getInt(id);
	}

	public void setTexture(ItemStack item, String id) {
		ItemMeta im = item.getItemMeta();
		setTexture(im, id);
		item.setItemMeta(im);
	}

	public void setTexture(ItemMeta im, String id) {
		int data = getModelData(id);
		im.setCustomModelData(data == 0 ? null: data);
	}

}
