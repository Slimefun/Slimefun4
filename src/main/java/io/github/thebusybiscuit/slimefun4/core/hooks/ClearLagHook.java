package io.github.thebusybiscuit.slimefun4.core.hooks;

import me.minebuilders.clearlag.events.EntityRemoveEvent;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

class ClearLagHook implements Listener {

    public ClearLagHook(SlimefunPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onEntityRemove(EntityRemoveEvent e) {
        e.getEntityList().removeIf(n -> n instanceof Item && n.hasMetadata("no_pickup"));
	}
}
