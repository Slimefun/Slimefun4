package io.github.thebusybiscuit.slimefun4.implementation.listeners;

import io.github.thebusybiscuit.slimefun4.api.MinecraftVersion;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;

import io.github.thebusybiscuit.cscorelib2.chat.ChatColors;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;

/**
 * Listens to the ItemPickup events to prevent it if the item has the "no_pickup" metadata or is an ALTAR_PROBE
 * or if a piglin wants to pickup SlimefunItems.
 *
 * @author TheBusyBiscuit
 */
public class ItemPickupListener implements Listener {

    private static final String ITEM_PREFIX = ChatColors.color("&5&dALTAR &3Probe - &e");

    public ItemPickupListener(SlimefunPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onEntityPickup(EntityPickupItemEvent e) {
        if (SlimefunUtils.hasNoPickupFlag(e.getItem())) {
            e.setCancelled(true);
        }
        else if (e.getItem().getItemStack().hasItemMeta() && e.getItem().getItemStack().getItemMeta().hasDisplayName() && e.getItem().getItemStack().getItemMeta().getDisplayName().startsWith(ITEM_PREFIX)) {
            e.setCancelled(true);
            e.getItem().remove();
        } else if (SlimefunPlugin.getMinecraftVersion().isAtLeast(MinecraftVersion.MINECRAFT_1_16)) {
            if (e.getEntityType().equals(EntityType.PIGLIN) || e.getEntityType().equals(EntityType.ZOMBIFIED_PIGLIN)) {
                if (SlimefunItem.getByItem(e.getItem().getItemStack()) != null) {
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onHopperPickup(InventoryPickupItemEvent e) {
        if (SlimefunUtils.hasNoPickupFlag(e.getItem())) {
            e.setCancelled(true);
        }
        else if (e.getItem().getItemStack().hasItemMeta() && e.getItem().getItemStack().getItemMeta().hasDisplayName() && e.getItem().getItemStack().getItemMeta().getDisplayName().startsWith(ITEM_PREFIX)) {
            e.setCancelled(true);
            e.getItem().remove();
        }
    }
}
