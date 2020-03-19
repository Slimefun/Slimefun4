package io.github.thebusybiscuit.slimefun4.implementation.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import io.github.thebusybiscuit.slimefun4.api.player.PlayerProfile;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;

public class PlayerProfileListener implements Listener {

    public PlayerProfileListener(SlimefunPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onDisconnect(PlayerQuitEvent e) {
        if (PlayerProfile.isLoaded(e.getPlayer().getUniqueId())) {
            PlayerProfile.get(e.getPlayer()).markForDeletion();
        }
    }

}
