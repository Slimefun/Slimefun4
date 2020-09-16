package io.github.thebusybiscuit.slimefun4.implementation.listeners;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.HeightMap;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import javax.annotation.Nonnull;

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
    public void onApproachGround(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        
        // Checking if the player is even falling.
        if (e.getTo().getBlockY() == e.getFrom().getBlockY()) {
            return;
        }
        
        if (!player.isGliding()) return;
        if (player.isOnGround()) return;
        
        if (player.hasPotionEffect(PotionEffectType.SLOW_FALLING)) return;
        
        ItemStack chestplate = player.getInventory().getChestplate();
        if (!SlimefunUtils.isItemSimilar(chestplate, SlimefunItems.BEE_WINGS, true) || !Slimefun.hasUnlocked(player, chestplate, true)) {
            return;
        }

        Location loc = player.getLocation();
        double distanceToHighestBlock = (loc.getBlockY() - player.getWorld().getHighestBlockYAt(loc, HeightMap.WORLD_SURFACE));

        // getDistanceToGround will only fire when playerDistanceToHighestBlock is negative (which happens when a player is flying under an existing structure)
        if (distanceToHighestBlock < 0) {
            if (getDistanceToGround(loc.clone()) > 6) return;
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 40, 0));
            return;
        }

        if (distanceToHighestBlock <= 6) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 40, 0));
        }
    }

    /** 
     * Calculates the distance of the given {@link Location} from the ground.
     *
     * @param loc
     *            The {@link Location} to calculate from.
     *
     */
    private int getDistanceToGround(@Nonnull Location loc) {
        int y = loc.getBlockY();
        for (int i = y; i >= 0; i--) {
            loc.setY(i);
            if (loc.getBlock().getType().isSolid()) {
                return y - i;
            }
        }
        return 0;
    }
}
