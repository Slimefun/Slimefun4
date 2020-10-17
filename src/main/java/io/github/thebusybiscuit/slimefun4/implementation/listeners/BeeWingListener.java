package io.github.thebusybiscuit.slimefun4.implementation.listeners;

import javax.annotation.Nonnull;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import me.mrCookieSlime.Slimefun.api.Slimefun;

/**
 * This {@link Listener} is responsible for the slow falling effect given to the player
 * when nearing the ground while using the Bee Wings.
 *
 * @author beSnow
 * @author Linox
 * 
 */
public class BeeWingListener implements Listener {

    public BeeWingListener(@Nonnull SlimefunPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onApproachGround(playe e) {
        Player player = e.getPlayer();

        ItemStack chestplate = player.getInventory().getChestplate();
        if (!SlimefunUtils.isItemSimilar(chestplate, SlimefunItems.BEE_WINGS, true) || !Slimefun.hasUnlocked(player, chestplate, true)) {
            return;
        }

        
    }
}
