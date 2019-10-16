package me.mrCookieSlime.Slimefun.listeners;

import me.mrCookieSlime.Slimefun.SlimefunPlugin;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;

/**
 * Listens to the ItemPickup events to prevent it if the item has the "no_pickup" metadata or is an ALTAR_PROBE.
 *
 * This Listener uses the new {@link EntityPickupItemEvent} due to the deprecation of {@link org.bukkit.event.player.PlayerPickupItemEvent}.
 *
 * @since 4.1.11
 */
public class ItemPickupListener implements Listener {

	public ItemPickupListener(SlimefunPlugin plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler
	public void onPickup(EntityPickupItemEvent e) {
		if (e.getItem().hasMetadata("no_pickup")) e.setCancelled(true);
		else if (!e.getItem().hasMetadata("no_pickup") && e.getItem().getItemStack().hasItemMeta() && e.getItem().getItemStack().getItemMeta().hasDisplayName() && e.getItem().getItemStack().getItemMeta().getDisplayName().startsWith(ChatColor.translateAlternateColorCodes('&', "&5&dALTAR &3Probe - &e"))) {
			e.setCancelled(true);
			e.getItem().remove();
		}
	}

	@EventHandler
	public void onMinecartPickup(InventoryPickupItemEvent e) {
		if (e.getItem().hasMetadata("no_pickup")) e.setCancelled(true);
		else if (!e.getItem().hasMetadata("no_pickup") && e.getItem().getItemStack().hasItemMeta() && e.getItem().getItemStack().getItemMeta().hasDisplayName() && e.getItem().getItemStack().getItemMeta().getDisplayName().startsWith(ChatColor.translateAlternateColorCodes('&', "&5&dALTAR &3Probe - &e"))) {
			e.setCancelled(true);
			e.getItem().remove();
		}
	}
}
