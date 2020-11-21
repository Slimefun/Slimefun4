package io.github.thebusybiscuit.slimefun4.implementation.listeners;

import javax.annotation.Nonnull;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

import io.github.thebusybiscuit.slimefun4.core.handlers.ItemDropHandler;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.ItemHandler;

/**
 * Listens to the {@link PlayerDropItemEvent} to call any {@link ItemDropHandler}.
 *
 * @author TheBusyBiscuit
 * 
 * @see ItemDropHandler
 */
public class ItemDropListener implements Listener {

    public ItemDropListener(@Nonnull SlimefunPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent e) {
        for (ItemHandler handler : SlimefunItem.getPublicItemHandlers(ItemDropHandler.class)) {
            if (((ItemDropHandler) handler).onItemDrop(e, e.getPlayer(), e.getItemDrop())) {
                return;
            }
        }
    }
}
