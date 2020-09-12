package io.github.thebusybiscuit.slimefun4.implementation.listeners;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;

public class SoulboundListener implements Listener {

    private final Map<UUID, Map<Integer, ItemStack>> soulbound = new HashMap<>();

    public SoulboundListener(@Nonnull SlimefunPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onDamage(PlayerDeathEvent e) {
        Player p = e.getEntity();

        for (int slot = 0; slot < p.getInventory().getSize(); slot++) {
            ItemStack item = p.getInventory().getItem(slot);

            // Store soulbound items for later retrieval
            if (SlimefunUtils.isSoulbound(item)) {
                storeItem(p.getUniqueId(), slot, item);
            }
        }

        // Remove soulbound items from our drops
        Iterator<ItemStack> drops = e.getDrops().iterator();
        while (drops.hasNext()) {
            ItemStack item = drops.next();

            if (SlimefunUtils.isSoulbound(item)) {
                drops.remove();
            }
        }
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
        retrieveItems(e.getPlayer());
    }

    @ParametersAreNonnullByDefault
    private void storeItem(UUID uuid, int slot, ItemStack item) {
        Map<Integer, ItemStack> items = soulbound.computeIfAbsent(uuid, uid -> new HashMap<>());
        items.put(slot, item);
    }

    private void retrieveItems(@Nonnull Player p) {
        Map<Integer, ItemStack> items = soulbound.remove(p.getUniqueId());

        if (items != null) {
            for (Map.Entry<Integer, ItemStack> entry : items.entrySet()) {
                p.getInventory().setItem(entry.getKey(), entry.getValue());
            }
        }
    }
}
