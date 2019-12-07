package me.mrCookieSlime.Slimefun.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.api.PlayerProfile;

public class PlayerQuitListener implements Listener {

	public PlayerQuitListener(SlimefunPlugin plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	public void onDisconnect(PlayerQuitEvent e) {
		if (PlayerProfile.isLoaded(e.getPlayer().getUniqueId())) {
			PlayerProfile.get(e.getPlayer()).markForDeletion();
		}
	}

}
