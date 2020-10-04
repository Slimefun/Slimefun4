package io.github.thebusybiscuit.slimefun4.implementation.listeners;

import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.implementation.items.armor.ElytraCap;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

/**
 * The {@link Listener} for the {@link ElytraCap}.
 *
 * @author Seggan
 */
public class ElytraCapListener implements Listener {

    public ElytraCapListener(@Nonnull SlimefunPlugin plugin) {
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
            if (item instanceof ElytraCap) {
                e.setDamage(0);
                p.playSound(p.getLocation(), Sound.BLOCK_STONE_HIT, 20, 1);
            }
        }
    }
}
