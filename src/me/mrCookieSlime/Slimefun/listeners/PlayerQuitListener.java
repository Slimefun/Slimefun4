package me.mrCookieSlime.Slimefun.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import me.mrCookieSlime.Slimefun.SlimefunGuide;
import me.mrCookieSlime.Slimefun.SlimefunStartup;

public class PlayerQuitListener implements Listener {

	public PlayerQuitListener(SlimefunStartup plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler
	public void onDisconnect(PlayerQuitEvent e) {
		SlimefunGuide.history.remove(e.getPlayer().getUniqueId());
	}

}
