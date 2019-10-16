package me.mrCookieSlime.Slimefun.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import me.mrCookieSlime.Slimefun.SlimefunGuide;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.api.SlimefunGuideLayout;

public class GuideOnJoinListener implements Listener {

	public GuideOnJoinListener(SlimefunPlugin plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		if (!e.getPlayer().hasPlayedBefore()) {
			Player p = e.getPlayer();
			if (!SlimefunPlugin.getWhitelist().getBoolean(p.getWorld().getName() + ".enabled")) return;
			if (!SlimefunPlugin.getWhitelist().getBoolean(p.getWorld().getName() + ".enabled-items.SLIMEFUN_GUIDE")) return;
			
			SlimefunGuideLayout type = SlimefunPlugin.getCfg().getBoolean("guide.default-view-book") ? SlimefunGuideLayout.BOOK : SlimefunGuideLayout.CHEST;
			p.getInventory().addItem(SlimefunGuide.getItem(type));
		}
	}

}
