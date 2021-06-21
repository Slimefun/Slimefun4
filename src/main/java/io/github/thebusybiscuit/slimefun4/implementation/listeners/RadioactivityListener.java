package io.github.thebusybiscuit.slimefun4.implementation.listeners;

import io.github.thebusybiscuit.slimefun4.core.attributes.Radioactive;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.implementation.tasks.RadioactivityTask;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import javax.annotation.Nonnull;

/**
 * {@link RadioactivityListener} handles logouts for players to
 * remove from the `radioactivityLevel` {@link java.util.HashMap}
 * and handles radioactivity removal from death
 *
 * @author Semisol
 *
 */
public class RadioactivityListener implements Listener {

    public RadioactivityListener(@Nonnull SlimefunPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerQuit(@Nonnull PlayerQuitEvent e){
        RadioactivityTask.removePlayer(e.getPlayer());
    }

    @EventHandler
    public void onPlayerDeaath(@Nonnull PlayerDeathEvent e){
        RadioactivityTask.removePlayer(e.getEntity());
    }
}
