package io.github.thebusybiscuit.slimefun4.implementation.listeners;

import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.utils.RadiationUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import javax.annotation.Nonnull;

/**
 * {@link RadioactivityListener} handles radioactivity level resets
 * on death
 *
 * @author Semisol
 *
 */
public class RadioactivityListener implements Listener {

    public RadioactivityListener(@Nonnull Slimefun plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerDeath(@Nonnull PlayerDeathEvent e) {
        RadiationUtils.clearExposure(e.getEntity());
    }
}
