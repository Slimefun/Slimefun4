package io.github.thebusybiscuit.slimefun4.implementation.listeners;

import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

public class CrashHelmetListener implements Listener {

    public CrashHelmetListener(@Nonnull SlimefunPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerCrash(EntityDamageEvent e) {
        if (!(e.getCause() == EntityDamageEvent.DamageCause.FLY_INTO_WALL) || !(e.getEntity() instanceof Player)) {
            return;
        }

        ItemStack stack = ((Player) e.getEntity()).getInventory().getHelmet();
        SlimefunItem item = SlimefunItem.getByItem(stack);
        if ((item != null) && (item.getID().equals("CRASH_HELMET"))) {
            e.setCancelled(true);
        }
    }
}
