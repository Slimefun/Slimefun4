package io.github.thebusybiscuit.slimefun4.implementation.listeners.crafting;

import javax.annotation.Nonnull;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryType;

import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.VanillaContainer;
import me.mrCookieSlime.Slimefun.api.BlockStorage;

/**
 * This {@link Listener} prevents item from being transferred to a
 * {@link VanillaContainer} using a hopper.
 *
 */

public class HopperListener implements SlimefunCraftingListener {

    public HopperListener(@Nonnull SlimefunPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void fromHopper(InventoryMoveItemEvent e) {
        Location loc = e.getDestination().getLocation();
        if (e.getSource().getType() == InventoryType.HOPPER && loc != null && BlockStorage.hasBlockInfo(loc)) {
            if (BlockStorage.check(loc.getBlock()) instanceof VanillaContainer) {
                e.setCancelled(true);
            }
        }
    }
}
