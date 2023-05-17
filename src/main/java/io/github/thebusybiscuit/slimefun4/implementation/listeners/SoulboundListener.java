package io.github.thebusybiscuit.slimefun4.implementation.listeners;

import java.util.*;

import javax.annotation.Nonnull;

import io.github.bakedlibs.dough.config.Config;
import org.bukkit.configuration.MemorySection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.core.attributes.Soulbound;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;

/**
 * This {@link Listener} is responsible for handling any {@link Soulbound} items.
 * A {@link Soulbound} {@link ItemStack} will not drop upon a {@link Player Player's} death.
 * Instead the {@link ItemStack} is saved and given back to the {@link Player} when they respawn.
 * If the server restarts while there are {@link Player Player's} on the death screen, the {@link ItemStack} is saved in a {@link Config} file.
 *
 * @author TheBusyBiscuit
 */
public class SoulboundListener implements Listener {

    private final Map<UUID, Map<Integer, ItemStack>> soulbound = new HashMap<>();

    private final Config soulboundCache = new Config("plugins/Slimefun/cache/soulbound/death.yml");

    public void register(@Nonnull Slimefun plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onDamage(PlayerDeathEvent e) {
        Map<Integer, ItemStack> items = new HashMap<>();
        Player p = e.getEntity();

        for (int slot = 0; slot < p.getInventory().getSize(); slot++) {
            ItemStack item = p.getInventory().getItem(slot);

            // Store soulbound items for later retrieval
            if (SlimefunUtils.isSoulbound(item, p.getWorld())) {
                items.put(slot, item);
            }
        }

        // There shouldn't even be any items in there, but let's be extra safe!
        Map<Integer, ItemStack> existingItems = soulbound.get(p.getUniqueId());

        if (existingItems == null) {
            soulbound.put(p.getUniqueId(), items);
        } else {
            existingItems.putAll(items);
        }

        // Remove soulbound items from our drops
        e.getDrops().removeIf(itemStack -> SlimefunUtils.isSoulbound(itemStack, p.getWorld()));
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
        returnSoulboundItems(e.getPlayer());
    }

    private void returnSoulboundItems(@Nonnull Player p) {
        Map<Integer, ItemStack> items = soulbound.remove(p.getUniqueId());

        if (items != null) {
            for (Map.Entry<Integer, ItemStack> entry : items.entrySet()) {
                p.getInventory().setItem(entry.getKey(), entry.getValue());
            }
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        if (!soulbound.containsKey(e.getPlayer().getUniqueId())) {
            MemorySection o = (MemorySection) soulboundCache.getConfiguration().get(e.getPlayer().getUniqueId().toString());
            if(o != null) {
                Set<String> keys = o.getKeys(false);
                Map<Integer, ItemStack> items = new HashMap<>();
                keys.forEach((s) -> items.put(Integer.parseInt(s), (ItemStack) o.get(s)));
                soulbound.put(e.getPlayer().getUniqueId(), items);
            }
        }
        soulboundCache.setValue(e.getPlayer().getUniqueId().toString(), null);
        soulboundCache.save();
    }

    private void saveCache(@Nonnull UUID uuid) {
        soulbound.get(uuid).forEach((integer, itemStack) -> {
            soulboundCache.setValue(uuid + "." + integer, itemStack);

        });
        soulboundCache.save();
    }

    //save all soulbound items when the plugin is disabled
    public void saveAll() {
        soulbound.forEach((uuid, integerItemStackMap) -> saveCache(uuid));
    }
}
