package io.github.thebusybiscuit.slimefun4.core.services.plugins;

import javax.annotation.Nonnull;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import com.gmail.nossr50.mcMMO;

import io.github.thebusybiscuit.slimefun4.api.events.BlockPlacerPlaceEvent;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;

class McMMOIntegration implements Listener {

    McMMOIntegration(@Nonnull SlimefunPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockPlacerPlace(BlockPlacerPlaceEvent e) {
        // This registers blocks placed by the BlockPlacer as "player-placed"
        mcMMO.getPlaceStore().setTrue(e.getBlock());
    }

}
