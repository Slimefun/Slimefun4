package io.github.thebusybiscuit.slimefun4.core.services.plugins;

import java.util.Iterator;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import me.minebuilders.clearlag.events.EntityRemoveEvent;

class ClearLagHook implements Listener {

    ClearLagHook(SlimefunPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onEntityRemove(EntityRemoveEvent e) {
        Iterator<Entity> iterator = e.getEntityList().iterator();

        while (iterator.hasNext()) {
            Entity n = iterator.next();

            if (n instanceof Item && SlimefunUtils.hasNoPickupFlag((Item) n)) {
                iterator.remove();
            }
        }
    }
}
