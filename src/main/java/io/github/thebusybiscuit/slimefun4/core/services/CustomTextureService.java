package io.github.thebusybiscuit.slimefun4.core.services;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import io.github.thebusybiscuit.cscorelib2.config.Config;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;

public class CustomTextureService {
	
	private final Config config;
	private boolean modified = false;
	
	public CustomTextureService(Plugin plugin) {
		this.config = new Config(plugin, "item-models.yml");
	}
	
	public void setup(Iterable<SlimefunItem> items) {
		config.setDefaultValue("SLIMEFUN_GUIDE", 0);
		
		config.setDefaultValue("_UI_BACKGROUND", 0);
		config.setDefaultValue("_UI_BACK", 0);
		config.setDefaultValue("_UI_MENU", 0);
		config.setDefaultValue("_UI_SEARCH", 0);
		config.setDefaultValue("_UI_WIKI", 0);
		config.setDefaultValue("_UI_PREVIOUS_ACTIVE", 0);
		config.setDefaultValue("_UI_PREVIOUS_INACTIVE", 0);
		config.setDefaultValue("_UI_NEXT_ACTIVE", 0);
		config.setDefaultValue("_UI_NEXT_INACTIVE", 0);
		
		for (SlimefunItem item : items) {
			if (item != null && item.getID() != null) {
				config.setDefaultValue(item.getID(), 0);
				
				if (config.getInt(item.getID()) != 0) {
					modified = true;
				}
			}
		}
		
		config.save();
	}
	
	public String getVersion() {
		return config.getString("version");
	}
	
	public boolean isActive() {
		return modified;
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
