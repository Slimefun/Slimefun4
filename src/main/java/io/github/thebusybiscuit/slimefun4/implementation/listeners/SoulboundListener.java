package io.github.thebusybiscuit.slimefun4.implementation.listeners;

import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SoulboundListener implements Listener {

    private final Map<UUID, Map<Integer, ItemStack>> soulbound = new HashMap<>();

    public SoulboundListener(SlimefunPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onDamage(EntityDeathEvent e) {
        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();

            for (int slot = 0; slot < p.getInventory().getSize(); slot++) {
                ItemStack item = p.getInventory().getItem(slot);

                if (SlimefunUtils.isSoulbound(item)) {
                    storeItem(p.getUniqueId(), slot, item);
                }
            }

            e.getDrops().removeIf(SlimefunUtils::isSoulbound);

        }
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
        retrieveItems(e.getPlayer());
    }

    private void storeItem(UUID uuid, int slot, ItemStack item) {
        Map<Integer, ItemStack> items = soulbound.computeIfAbsent(uuid, uid -> new HashMap<>());
        items.put(slot, item);
    }

    private void retrieveItems(Player p) {
        Map<Integer, ItemStack> items = soulbound.remove(p.getUniqueId());

        if (items != null) {
            for (Map.Entry<Integer, ItemStack> entry : items.entrySet()) {
                p.getInventory().setItem(entry.getKey(), entry.getValue());
            }
        }
    }
}
