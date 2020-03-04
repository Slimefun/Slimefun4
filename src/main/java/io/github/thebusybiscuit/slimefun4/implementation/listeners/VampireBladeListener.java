package io.github.thebusybiscuit.slimefun4.implementation.listeners;

import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.Setup.SlimefunManager;

public class VampireBladeListener implements Listener {

    public VampireBladeListener(SlimefunPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onDamage(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player && ThreadLocalRandom.current().nextInt(100) < 45) {
            Player p = (Player) e.getDamager();

            if (SlimefunManager.isItemSimilar(p.getInventory().getItemInMainHand(), SlimefunItems.BLADE_OF_VAMPIRES, true)) {
                p.playSound(p.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 0.7F, 0.7F);
                p.addPotionEffect(new PotionEffect(PotionEffectType.HEAL, 1, 1));
            }
        }
    }

}
