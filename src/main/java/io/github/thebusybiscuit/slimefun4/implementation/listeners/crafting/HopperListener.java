package io.github.thebusybiscuit.slimefun4.implementation.listeners.crafting;

import javax.annotation.Nonnull;

import io.github.thebusybiscuit.slimefun4.api.inventory.AbstractVanillaBlockInventory;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryType;

import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.api.BlockStorage;

/**
 * This {@link Listener} prevents item from being transferred to a
 * {@link AbstractVanillaBlockInventory} using a hopper.
 *
 * @author CURVX
 *
 */

public class HopperListener implements SlimefunCraftingListener {

    public HopperListener(@Nonnull SlimefunPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onHopperInsert(InventoryMoveItemEvent e) {
        Location loc = e.getDestination().getLocation();
        if (e.getSource().getType() == InventoryType.HOPPER && loc != null && BlockStorage.check(loc.getBlock()) instanceof AbstractVanillaBlockInventory) {
                e.setCancelled(true);
        }
    }
}
