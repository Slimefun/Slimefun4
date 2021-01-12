package io.github.thebusybiscuit.slimefun4.implementation.listeners;

import javax.annotation.Nonnull;

import io.github.thebusybiscuit.slimefun4.core.attributes.NotHopperable;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryType;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AContainer;

import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.api.BlockStorage;

/**
 * This {@link Listener} prevents item from being transferred to a
 * {@link AContainer} using a hopper.
 *
 * @author CURVX
 *
 */
public class HopperListener implements Listener {

    public HopperListener(@Nonnull SlimefunPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onHopperInsert(InventoryMoveItemEvent e) {
        Location loc = e.getDestination().getLocation();
        if (e.getSource().getType() == InventoryType.HOPPER && loc != null && BlockStorage.check(loc.getBlock()) instanceof NotHopperable) {
            e.setCancelled(true);
        }
    }
}
