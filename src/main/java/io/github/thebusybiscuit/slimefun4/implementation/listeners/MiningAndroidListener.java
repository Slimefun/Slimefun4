package io.github.thebusybiscuit.slimefun4.implementation.listeners;

import javax.annotation.Nonnull;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import io.github.thebusybiscuit.slimefun4.api.events.AndroidMineEvent;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockBreakHandler;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.BlockStorage;

public class MiningAndroidListener implements Listener {

    public MiningAndroidListener(@Nonnull SlimefunPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(ignoreCancelled = true)
    public void onAndroidMine(AndroidMineEvent e) {
        SlimefunItem item = BlockStorage.check(e.getBlock());

        item.callItemHandler(BlockBreakHandler.class, handler -> {
            if (handler.isAndroidAllowed(e.getBlock())) {
                handler.onAndroidBreak(e);
            } else {
                e.setCancelled(true);
            }
        });
    }
}
