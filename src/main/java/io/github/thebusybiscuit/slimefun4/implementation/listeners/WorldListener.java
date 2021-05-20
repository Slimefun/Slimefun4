package io.github.thebusybiscuit.slimefun4.implementation.listeners;

import java.util.logging.Level;

import javax.annotation.Nonnull;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldUnloadEvent;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.api.BlockStorage;

public class WorldListener implements Listener {

    public WorldListener(@Nonnull SlimefunPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onWorldLoad(WorldLoadEvent e) {
        System.out.println(ChatColor.RED+"SLIMEFUN- Loading world ... "+e.getWorld().getName());
        SlimefunPlugin.getWorldSettingsService().load(e.getWorld());
        BlockStorage.getOrCreate(e.getWorld());
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onWorldUnload(WorldUnloadEvent e) {
        System.out.println(ChatColor.RED+"SLIMEFUN- Unloading world ... "+e.getWorld().getName());
        BlockStorage storage = BlockStorage.getStorage(e.getWorld());

        if (storage != null) {
            storage.saveAndRemove();
        } else {
            SlimefunPlugin.logger().log(Level.SEVERE, "Could not save Slimefun Blocks for World \"{0}\"", e.getWorld().getName());
        }
    }

}
