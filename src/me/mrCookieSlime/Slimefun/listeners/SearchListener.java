package me.mrCookieSlime.Slimefun.listeners;

import me.mrCookieSlime.Slimefun.SlimefunGuide;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class SearchListener implements Listener {

    private static Set<UUID> waitingSearchTerm = ConcurrentHashMap.newKeySet();

    @EventHandler
    public void onSearch(AsyncPlayerChatEvent e) {
        if (waitingSearchTerm.remove(e.getPlayer().getUniqueId())) {
            Bukkit.getScheduler().runTask(SlimefunPlugin.instance,
                () -> SlimefunGuide.openSearch(e.getPlayer(), e.getMessage(), true, true));

            e.setCancelled(true);
        }
    }

    public static void addSearchingPlayer(UUID uniqueId) {
        waitingSearchTerm.add(uniqueId);
    }

    public static void cleanUp() {
        waitingSearchTerm.clear();
    }
}
