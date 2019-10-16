package me.mrCookieSlime.Slimefun.hooks;

import java.util.Iterator;

import me.minebuilders.clearlag.events.EntityRemoveEvent;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ClearLagHook implements Listener {
	
	public ClearLagHook(SlimefunPlugin plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	public void onEntityRemove(EntityRemoveEvent e) {
		Iterator<Entity> iterator = e.getEntityList().iterator();
		while (iterator.hasNext()) {
			Entity n = iterator.next();
			if (n instanceof Item && n.hasMetadata("no_pickup")) iterator.remove();
		}
	}
}
