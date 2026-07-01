package io.github.thebusybiscuit.slimefun5.core.guide.installer;

import javax.annotation.Nonnull;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;

/**
 * Tells an admin once, on join, about any pending addon/Slimefun updates the installer found at
 * startup. The decision (permission check, already-told, nothing pending) lives in
 * {@link AddonInstaller#announceTo(org.bukkit.entity.Player)}.
 */
public class AddonUpdateJoinListener implements Listener {

    public AddonUpdateJoinListener(@Nonnull Slimefun plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onJoin(@Nonnull PlayerJoinEvent e) {
        // A short delay so permission plugins have settled and the async startup check has likely run.
        Slimefun.instance().getServer().getScheduler().runTaskLater(Slimefun.instance(),
            () -> AddonInstallerMenu.installer().announceTo(e.getPlayer()), 40L);
    }
}
