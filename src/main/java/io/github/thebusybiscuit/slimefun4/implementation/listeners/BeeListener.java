package io.github.thebusybiscuit.slimefun4.implementation.listeners;

import org.bukkit.entity.Bee;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.cscorelib2.inventory.ItemUtils;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;

/**
 * The listener for Hazmat Suit's {@link Bee} sting protection.
 * Only applied if the whole set is worn.
 *
 * @author Linox
 *
 */
public class BeeListener implements Listener {

    public BeeListener(SlimefunPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Bee) {
            if (e.getEntity() instanceof Player) {
                Player p = (Player) e.getEntity();

                // Check for a Hazmat Suit
                if (!SlimefunUtils.isItemSimilar(SlimefunItems.SCUBA_HELMET, p.getInventory().getHelmet(), true) &&
                        !SlimefunUtils.isItemSimilar(SlimefunItems.HAZMAT_CHESTPLATE, p.getInventory().getChestplate(), true) &&
                        !SlimefunUtils.isItemSimilar(SlimefunItems.HAZMAT_LEGGINGS, p.getInventory().getLeggings(), true) &&
                        !SlimefunUtils.isItemSimilar(SlimefunItems.RUBBER_BOOTS, p.getInventory().getBoots(), true)) {
                    e.setDamage(0D);
                    for (ItemStack armor : p.getInventory().getArmorContents()) {
                        ItemUtils.damageItem(armor, 1, false);
                    }
                }
            }
        }
    }
}
