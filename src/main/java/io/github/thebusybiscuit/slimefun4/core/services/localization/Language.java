package io.github.thebusybiscuit.slimefun4.core.services.localization;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.cscorelib2.skull.SkullItem;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;

public final class Language {

	private final String id;
	private final FileConfiguration config;
	private final ItemStack item;

	public Language(String id, FileConfiguration config, String hash) {
		this.id = id;
		this.config = config;
		
		item = SkullItem.fromHash(hash);
		SlimefunPlugin.getItemTextureService().setTexture(item, "_UI_LANGUAGE_" + id.toUpperCase());
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

	public String getName(Player p) {
		return SlimefunPlugin.getLocal().getMessage(p, "languages." + id);
	}

	public boolean isDefault() {
		return this == SlimefunPlugin.getLocal().getDefaultLanguage();
	}

}
