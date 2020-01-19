package io.github.thebusybiscuit.slimefun4.implementation.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.api.PlayerProfile;

public class PlayerProfileListener implements Listener {

	public PlayerProfileListener(SlimefunPlugin plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	public void onDisconnect(PlayerQuitEvent e) {
		if (PlayerProfile.isLoaded(e.getPlayer().getUniqueId())) {
			PlayerProfile.get(e.getPlayer()).markForDeletion();
		}
	}

}
