package io.github.thebusybiscuit.slimefun4.core.services;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.cscorelib2.skull.SkullItem;

public final class Language {
	
	private final String id;
	private final FileConfiguration config;
	private final ItemStack item;
	
	public Language(String id, FileConfiguration config, String hash) {
		this.id = id;
		this.config = config;
		this.item = SkullItem.fromHash(hash);
	}
	
	public String getID() {
		return id;
	}
	
	public FileConfiguration getConfig() {
		return config;
	}
	
	public ItemStack getItem() {
		return item;
	}

}
