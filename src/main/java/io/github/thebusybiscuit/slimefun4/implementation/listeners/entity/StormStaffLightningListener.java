package io.github.thebusybiscuit.slimefun4.implementation.listeners.entity;

import javax.annotation.Nonnull;

import org.bukkit.Bukkit;
import org.bukkit.entity.LightningStrike;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.weather.LightningStrikeEvent;

import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.implementation.items.magical.staves.StormStaff;

import java.util.UUID;

/**
 * This {@link Listener} makes sure that if a {@link LightningStrike} spawned by a {@link StormStaff}
 * causes damage to an {@link org.bukkit.entity.Entity}, the event is transformed so the damage is
 * attributed to the StormStaff caster.
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
            UUID casterUuid = StormStaff.getStormStaffLightnings().get(lightningStrikeUuid);
            if (casterUuid != null) {
                e.setCancelled(true);
                Player caster = Bukkit.getPlayer(casterUuid);
                EntityDamageByEntityEvent newEvent = new EntityDamageByEntityEvent(caster, e.getEntity(), e.getCause(), e.getDamage());
                Bukkit.getPluginManager().callEvent(newEvent);
            }
        }
    }

    // To prevent memory leaks, we want the stored lightnings to always be removed whether they dealt damage or not
    @EventHandler(ignoreCancelled = true)
    public void onLightningFall(LightningStrikeEvent e) {
        StormStaff.getStormStaffLightnings().remove(e.getLightning().getUniqueId());
    }

}

