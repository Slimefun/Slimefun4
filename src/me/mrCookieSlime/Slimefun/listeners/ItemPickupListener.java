package me.mrCookieSlime.Slimefun.listeners;

import me.mrCookieSlime.Slimefun.SlimefunStartup;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

/**
 * Listens to the ItemPickup events to prevent it if the item has the "no_pickup" metadata or is an ALTAR_PROBE.
 *
 * @since 4.1.12
 */
public class ItemPickupListener implements Listener {

    public ItemPickupListener(SlimefunStartup plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPickup(PlayerPickupItemEvent e) {
        if (e.getItem().hasMetadata("no_pickup")) e.setCancelled(true);
        else if (!e.getItem().hasMetadata("no_pickup") && e.getItem().getItemStack().hasItemMeta() && e.getItem().getItemStack().getItemMeta().hasDisplayName() && e.getItem().getItemStack().getItemMeta().getDisplayName().startsWith("&5&dALTAR &3Probe - &e")) {
            e.setCancelled(true);
            e.getItem().remove();
        }
    }

    @EventHandler
    public void onMinecartPickup(InventoryPickupItemEvent e) {
        if (e.getItem().hasMetadata("no_pickup")) e.setCancelled(true);
        else if (!e.getItem().hasMetadata("no_pickup") && e.getItem().getItemStack().hasItemMeta() && e.getItem().getItemStack().getItemMeta().hasDisplayName() && e.getItem().getItemStack().getItemMeta().getDisplayName().startsWith("&5&dALTAR &3Probe - &e")) {
            e.setCancelled(true);
            e.getItem().remove();
        }
    }
}
