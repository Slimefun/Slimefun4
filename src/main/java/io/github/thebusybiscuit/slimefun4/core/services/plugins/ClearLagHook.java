package io.github.thebusybiscuit.slimefun4.core.services.plugins;

import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import me.minebuilders.clearlag.events.EntityRemoveEvent;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

class ClearLagHook implements Listener {

    ClearLagHook(SlimefunPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onEntityRemove(EntityRemoveEvent e) {

        e.getEntityList().removeIf(n -> n instanceof Item && SlimefunUtils.hasNoPickupFlag((Item) n));
    }
}
