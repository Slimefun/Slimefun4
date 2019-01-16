package me.mrCookieSlime.CSCoreLibPlugin.events.Listeners;

import me.mrCookieSlime.CSCoreLibPlugin.events.ItemUseEvent;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;

public class ItemUseListener implements Listener {
	
	public ItemUseListener(Plugin plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	public void onRightClick(PlayerInteractEvent e) throws Exception {
		
		if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			ItemUseEvent event = new ItemUseEvent(e, e.getAction() == Action.RIGHT_CLICK_BLOCK ? e.getClickedBlock(): null);
			Bukkit.getPluginManager().callEvent(event);
			if (!e.isCancelled()) e.setCancelled(event.isCancelled());
		}
	}

}
