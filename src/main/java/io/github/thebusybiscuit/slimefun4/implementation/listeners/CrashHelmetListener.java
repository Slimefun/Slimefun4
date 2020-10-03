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
        if (!(e.getEntity() instanceof Player)) return;
        if (!(e.getCause() == EntityDamageEvent.DamageCause.FALL ||
            e.getCause() == EntityDamageEvent.DamageCause.FLY_INTO_WALL)) return;

        Player p = (Player) e.getEntity();
        if (p.isGliding()) {
            ItemStack stack = p.getInventory().getHelmet();
            SlimefunItem item = SlimefunItem.getByItem(stack);
            if ((item != null) && (item.getID().equals("CRASH_HELMET"))) {
                e.setDamage(0);
            }
        }
    }
}
