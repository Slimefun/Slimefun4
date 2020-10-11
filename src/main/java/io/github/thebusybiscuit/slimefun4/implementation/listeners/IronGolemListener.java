package io.github.thebusybiscuit.slimefun4.implementation.listeners;

import javax.annotation.Nonnull;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.IronGolem;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.implementation.items.VanillaItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;

/**
 * This {@link Listener} makes sure that an {@link IronGolem} cannot be healed with
 * a {@link SlimefunItem}.
 * This fixes Issue 1332.
 * 
 * @author TheBusyBiscuit
 *
 */
public class IronGolemListener implements Listener {

    public IronGolemListener(@Nonnull SlimefunPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onIronGolemHeal(PlayerInteractEntityEvent e) {
        if (e.getRightClicked().getType() == EntityType.IRON_GOLEM) {
            PlayerInventory inv = e.getPlayer().getInventory();
            ItemStack item = null;

            if (e.getHand() == EquipmentSlot.HAND) {
                item = inv.getItemInMainHand();
            } else if (e.getHand() == EquipmentSlot.OFF_HAND) {
                item = inv.getItemInOffHand();
            }

            if (item != null && item.getType() == Material.IRON_INGOT) {
                SlimefunItem sfItem = SlimefunItem.getByItem(item);

                if (sfItem != null && !(sfItem instanceof VanillaItem)) {
                    e.setCancelled(true);
                    SlimefunPlugin.getLocalization().sendMessage(e.getPlayer(), "messages.no-iron-golem-heal");

                    // This is just there to update the Inventory...
                    // Somehow cancelling it isn't enough.
                    if (e.getHand() == EquipmentSlot.HAND) {
                        inv.setItemInMainHand(item);
                    } else if (e.getHand() == EquipmentSlot.OFF_HAND) {
                        inv.setItemInOffHand(item);
                    }
                }
            }
        }
    }

}
