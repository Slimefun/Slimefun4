package me.mrCookieSlime.Slimefun.hooks;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.OfflinePlayer;

import me.clip.placeholderapi.expansion.Configurable;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.mrCookieSlime.Slimefun.SlimefunStartup;

public class PlaceholderAPIHook extends PlaceholderExpansion implements Configurable {

	@Override
	public String getAuthor() {
		return SlimefunStartup.instance.getDescription().getAuthors().toString();
	}

	@Override
	public String getIdentifier() {
		return "slimefun";
	}

	@Override
	public String getVersion() {
		return SlimefunStartup.instance.getDescription().getVersion();
	}
	
	@Override
	public boolean persist() {
		return true;
	}
	
	@Override
	public boolean canRegister() {
		return true;
	}

	@Override
	public Map<String, Object> getDefaults() {
		Map<String, Object> defaults = new HashMap<>();
		
		return defaults;
	}
	
	@Override
	public String onRequest(OfflinePlayer p, String params) {
		// TODO: Add Placeholders
		return null;
	}

}
