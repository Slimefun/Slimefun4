package io.github.thebusybiscuit.slimefun4.implementation.listeners.entity;

import javax.annotation.Nonnull;

import io.github.thebusybiscuit.slimefun4.implementation.items.magical.staves.StormStaff;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Firework;
import org.bukkit.entity.LightningStrike;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.weather.LightningStrikeEvent;
import org.bukkit.inventory.meta.FireworkMeta;

import io.github.thebusybiscuit.slimefun4.api.researches.Research;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;

import java.util.UUID;

/**
 * This {@link Listener} makes sure that if a {@link LightningStrike} spawned by a {@link StormStaff}
 * causes damage {@link Player}, the event is transformed into a {@link}
 * unlocking a {@link Research} does not cause damage to be dealt.
 *
 * @author Sfiguz7
 *
 */
public class StormStaffLightningListener implements Listener {

    public StormStaffLightningListener(@Nonnull Slimefun plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onLightningDamage(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof LightningStrike) {
            LightningStrike lightningStrike = (LightningStrike) e.getDamager();
            UUID lightningStrikeUuid = lightningStrike.getUniqueId();
            if (StormStaff.stormStaffLightnings.containsKey(lightningStrikeUuid)) {
                e.setCancelled(true);
                Player caster = Bukkit.getPlayer(StormStaff.stormStaffLightnings.get(lightningStrikeUuid));
                EntityDamageByEntityEvent newEvent = new EntityDamageByEntityEvent(caster, e.getEntity(), e.getCause(), e.getDamage());
                Bukkit.getPluginManager().callEvent(newEvent);
            }
        }
    }

    // To prevent memory leaks, we want the stored lightnings to always be removed whether they dealt damage or not
    @EventHandler(ignoreCancelled = true)
    public void onLightningFall(LightningStrikeEvent e) {
        StormStaff.stormStaffLightnings.remove(e.getLightning().getUniqueId());
    }

}

