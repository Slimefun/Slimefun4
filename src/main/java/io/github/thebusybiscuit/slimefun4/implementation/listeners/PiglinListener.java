package io.github.thebusybiscuit.slimefun4.implementation.listeners;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Piglin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;

/**
 * This {@link Listener} prevents a {@link Piglin} from bartering with a {@link SlimefunItem}.
 *
 * @author poma123
 * 
 */
public class PiglinListener implements Listener {

    public PiglinListener(SlimefunPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onEntityPickup(EntityPickupItemEvent e) {
        if (e.getEntityType() == EntityType.PIGLIN) {
            ItemStack item = e.getItem().getItemStack();

            // Don't let Piglins pick up gold from Slimefun
            if (SlimefunItem.getByItem(item) != null) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onInteractEntity(PlayerInteractEntityEvent e) {
        if (!e.getRightClicked().isValid() || e.getRightClicked().getType() != EntityType.PIGLIN) {
            return;
        }

        Player p = e.getPlayer();
        ItemStack item;

        if (e.getHand() == EquipmentSlot.OFF_HAND) {
            item = p.getInventory().getItemInOffHand();
        }
        else {
            item = p.getInventory().getItemInMainHand();
        }

        // We only care about Gold since it's the actual "Bartering" we wanna prevent
        if (item.getType() == Material.GOLD_INGOT) {
            SlimefunItem sfItem = SlimefunItem.getByItem(item);

            if (sfItem != null) {
                SlimefunPlugin.getLocalization().sendMessage(p, "messages.piglin-barter", true);
                e.setCancelled(true);
            }
        }
    }
}
