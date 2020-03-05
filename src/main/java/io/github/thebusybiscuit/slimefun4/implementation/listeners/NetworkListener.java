package io.github.thebusybiscuit.slimefun4.implementation.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import io.github.thebusybiscuit.slimefun4.api.network.Network;
import io.github.thebusybiscuit.slimefun4.api.network.NetworkManager;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;

/**
 * This {@link Listener} is responsible for all updates to a {@link Network}.
 * 
 * @author meiamsome
 * 
 * @see Network
 * @see NetworkManager
 *
 */
public class NetworkListener implements Listener {

    private final NetworkManager manager;

    public NetworkListener(SlimefunPlugin plugin) {
        manager = SlimefunPlugin.getNetworkManager();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent e) {
        manager.handleAllNetworkLocationUpdate(e.getBlock().getLocation());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlaceBreak(BlockPlaceEvent e) {
        manager.handleAllNetworkLocationUpdate(e.getBlock().getLocation());
    }
}
