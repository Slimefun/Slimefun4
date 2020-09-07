package io.github.thebusybiscuit.slimefun4.implementation.listeners;

import javax.annotation.Nonnull;

import org.bukkit.ChatColor;
import org.bukkit.entity.Firework;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.meta.FireworkMeta;

import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;

public class FireworksListener implements Listener {

    public FireworksListener(@Nonnull SlimefunPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onResearchFireworkDamage(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Firework) {
            Firework firework = (Firework) e.getDamager();
            FireworkMeta meta = firework.getFireworkMeta();

            if (meta.hasDisplayName() && meta.getDisplayName().equals(ChatColor.GREEN + "Slimefun Research")) {
                e.setCancelled(true);
            }
        }
    }

}
