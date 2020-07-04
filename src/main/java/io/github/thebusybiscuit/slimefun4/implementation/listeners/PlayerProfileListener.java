package io.github.thebusybiscuit.slimefun4.implementation.listeners;

import io.github.thebusybiscuit.slimefun4.api.player.PlayerProfile;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Optional;

public class PlayerProfileListener implements Listener {

    public PlayerProfileListener(SlimefunPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onDisconnect(PlayerQuitEvent e) {
        Optional<PlayerProfile> profile = PlayerProfile.find(e.getPlayer());
        // if we still have a profile of this Player in memory, delete it
        profile.ifPresent(PlayerProfile::markForDeletion);
    }

    @EventHandler(ignoreCancelled = true)
    public void onKick(PlayerKickEvent e) {
        Optional<PlayerProfile> profile = PlayerProfile.find(e.getPlayer());

        // if we still have a profile of this Player in memory, delete it
        profile.ifPresent(PlayerProfile::markForDeletion);
    }

}
