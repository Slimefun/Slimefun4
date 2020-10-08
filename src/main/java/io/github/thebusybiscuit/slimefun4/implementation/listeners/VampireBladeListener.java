package io.github.thebusybiscuit.slimefun4.implementation.listeners;

import java.util.concurrent.ThreadLocalRandom;

import javax.annotation.Nonnull;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;

import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.implementation.items.weapons.VampireBlade;
import me.mrCookieSlime.Slimefun.api.Slimefun;

/**
 * This {@link Listener} is exclusively used for the {@link VampireBlade}.
 * It handles the {@link PotionEffect}
 * 
 * @author TheBusyBiscuit
 * 
 * @see VampireBlade
 *
 */
public class VampireBladeListener implements Listener {

    private final VampireBlade blade;

    public VampireBladeListener(@Nonnull SlimefunPlugin plugin, @Nonnull VampireBlade blade) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);

        this.blade = blade;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onDamage(EntityDamageByEntityEvent e) {
        if (blade == null || blade.isDisabled()) {
            return;
        }

        if (e.getDamager() instanceof Player && ThreadLocalRandom.current().nextInt(100) < blade.getChance()) {
            Player p = (Player) e.getDamager();

            if (blade.isItem(p.getInventory().getItemInMainHand())) {
                if (Slimefun.hasUnlocked(p, blade, true)) {
                    blade.heal(p);
                } else {
                    e.setCancelled(true);
                }
            }
        }
    }

}
