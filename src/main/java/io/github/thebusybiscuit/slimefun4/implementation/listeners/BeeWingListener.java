package io.github.thebusybiscuit.slimefun4.implementation.listeners;

import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.api.Slimefun;
import org.bukkit.HeightMap;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * This {@link Listener} is responsible for the slow falling effect given to the player
 * When Nearing the ground when using the Bee Wings.
 *
 * @author beSnow
 */
public class BeeWingListener implements Listener {

    public BeeWingListener(SlimefunPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onApproachGround(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        if (!player.isGliding()) return;
        if (player.isOnGround()) return;
        ItemStack helmet = player.getInventory().getChestplate();
        if (!SlimefunUtils.isItemSimilar(helmet, SlimefunItems.BEE_WINGS, true) && !Slimefun.hasUnlocked(player, helmet, true))
            return;

        double playerDistanceToHighestBlock = (player.getLocation().getY() - player.getWorld().getHighestBlockYAt(player.getLocation(), HeightMap.WORLD_SURFACE));

        // getDistanceToGround will only fire when playerDistanceToHighestBlock is negative (which happens when a player is flying under an existing structure)
        if (playerDistanceToHighestBlock < 0) {
            if (getDistanceToGround(player) > 6) return;
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 40, 0));
            return;
        }

        if (playerDistanceToHighestBlock <= 6) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 40, 0));
        }
    }

    private static int getDistanceToGround(Entity e) {
        Location loc = e.getLocation().clone();
        double y = loc.getBlockY();
        int distance = 0;
        for (double i = y; i >= 0; i--) {
            loc.setY(i);
            if (loc.getBlock().getType().isSolid()) break;
            distance++;
        }
        return distance;
    }
}