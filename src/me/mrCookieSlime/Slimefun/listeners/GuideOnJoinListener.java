package me.mrCookieSlime.Slimefun.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import me.mrCookieSlime.Slimefun.SlimefunGuide;
import me.mrCookieSlime.Slimefun.SlimefunStartup;
import me.mrCookieSlime.Slimefun.Misc.BookDesign;

public class GuideOnJoinListener implements Listener {

	public GuideOnJoinListener(SlimefunStartup plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		if (!e.getPlayer().hasPlayedBefore()) {
			Player p = e.getPlayer();
			if (!SlimefunStartup.getWhitelist().getBoolean(p.getWorld().getName() + ".enabled")) return;
			if (!SlimefunStartup.getWhitelist().getBoolean(p.getWorld().getName() + ".enabled-items.SLIMEFUN_GUIDE")) return;
			
			BookDesign type = SlimefunStartup.getCfg().getBoolean("guide.default-view-book") ? BookDesign.BOOK : BookDesign.CHEST;
			p.getInventory().addItem(SlimefunGuide.getItem(type));
		}
	}

}
