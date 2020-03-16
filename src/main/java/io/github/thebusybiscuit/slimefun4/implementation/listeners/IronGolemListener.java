package io.github.thebusybiscuit.slimefun4.implementation.listeners;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;

public class IronGolemListener implements Listener {

    public IronGolemListener(SlimefunPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onIronGolemHeal(PlayerInteractEntityEvent e) {
        if (e.getRightClicked().getType() == EntityType.IRON_GOLEM) {
            PlayerInventory inv = e.getPlayer().getInventory();
            ItemStack item = null;

            if (e.getHand() == EquipmentSlot.HAND) {
                item = inv.getItemInMainHand();
            }
            else if (e.getHand() == EquipmentSlot.OFF_HAND) {
                item = inv.getItemInOffHand();
            }

            if (item != null && item.getType() == Material.IRON_INGOT && SlimefunItem.getByItem(item) != null) {
                e.setCancelled(true);
                SlimefunPlugin.getLocal().sendMessage(e.getPlayer(), "messages.no-iron-golem-heal");

                // This is just there to update the Inventory...
                // Somehow cancelling it isn't enough.
                if (e.getHand() == EquipmentSlot.HAND) {
                    inv.setItemInMainHand(item);
                }
                else if (e.getHand() == EquipmentSlot.OFF_HAND) {
                    inv.setItemInOffHand(item);
                }
            }
        }
    }

}
