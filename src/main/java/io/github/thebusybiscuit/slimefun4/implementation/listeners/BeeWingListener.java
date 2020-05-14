package io.github.thebusybiscuit.slimefun4.implementation.listeners;

import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.api.Slimefun;
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

public class BeeWingListener implements Listener {

    public BeeWingListener(SlimefunPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onApproachGround(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        ItemStack helmet = e.getPlayer().getInventory().getChestplate();
        if (!SlimefunUtils.isItemSimilar(helmet, SlimefunItems.BEE_WINGS, true) && !Slimefun.hasUnlocked(e.getPlayer(), helmet, true)) return;
        if (!player.isGliding()) return;
        if (getDistanceToGround(player) == 0) return; //More accurate than Entity#isOnGround()

        if (getDistanceToGround(player) <= 6)
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 30, 0));
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

