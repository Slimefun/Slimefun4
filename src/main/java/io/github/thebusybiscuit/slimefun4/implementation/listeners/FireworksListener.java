package io.github.thebusybiscuit.slimefun4.implementation.listeners;

import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import me.mrCookieSlime.Slimefun.SlimefunPlugin;

public class FireworksListener implements Listener {

    public FireworksListener(SlimefunPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onResearchFireworkDamage(EntityDamageByEntityEvent e) {
        if (e.getDamager().getType() == EntityType.FIREWORK && e.getDamager().hasMetadata("no_fireworks_damage")) {
            e.setCancelled(true);
        }
    }

}
