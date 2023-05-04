package io.github.thebusybiscuit.slimefun4.implementation.listeners;

import java.util.*;

import javax.annotation.Nonnull;

import io.github.bakedlibs.dough.config.Config;
import io.github.thebusybiscuit.slimefun4.utils.itemstack.ItemStackWrapper;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.core.attributes.Soulbound;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;

/**
 * This {@link Listener} is responsible for handling any {@link Soulbound} items.
 * A {@link Soulbound} {@link ItemStack} will not drop upon a {@link Player Player's} death.
 * Instead the {@link ItemStack} is saved and given back to the {@link Player} when they respawn.
 *
 * @author TheBusyBiscuit
 */
public class SoulboundListener implements Listener {

    private final Map<UUID, Map<Integer, ItemStack>> soulbound = new HashMap<>();

    private final Config soulboundCache = new Config("plugins/Slimefun/cache/soulbound/death.yml");

    public SoulboundListener(@Nonnull Slimefun plugin) {
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
            Set<String> keys = soulboundCache.getKeys(e.getPlayer().getUniqueId().toString());
            Map<Integer, ItemStack> items = new HashMap<>();
            keys.forEach((s) -> {
                String value = soulboundCache.getString(e.getPlayer().getUniqueId() + "." + s);
                ItemStack item = ItemStackWrapper.fromJSON(value);
                items.put(Integer.parseInt(s), item);
            });
            soulbound.put(e.getPlayer().getUniqueId(), items);
        }
        soulboundCache.setValue(e.getPlayer().getUniqueId().toString(), null);
        soulboundCache.save();
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        if (e.getPlayer().getHealth() == 0)
            saveCache(e.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onKick(PlayerKickEvent e) {
        if (e.getPlayer().getHealth() == 0)
            saveCache(e.getPlayer().getUniqueId());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPluginDisable(PluginDisableEvent event) {
        System.out.println(soulbound);
        if(event.getPlugin() instanceof Slimefun)
            System.out.println(soulbound);
        System.out.println(soulbound);
    }

    private void saveCache(@Nonnull UUID uuid) {
        soulbound.get(uuid).forEach((integer, itemStack) -> {
            ItemStackWrapper itemStackWrapper = ItemStackWrapper.wrap(itemStack);
            soulboundCache.setValue(uuid + "." + integer, itemStackWrapper.toJSON());

        });
        soulboundCache.save();
    }

    private void saveCache(@Nonnull UUID uuid, @Nonnull Map<Integer, ItemStack> items) {
        items.forEach((integer, itemStack) -> {
            ItemStackWrapper itemStackWrapper = ItemStackWrapper.wrap(itemStack);
            soulboundCache.setValue(uuid + "." + integer, itemStackWrapper.toJSON());

        });
        soulboundCache.save();
    }


    //save all soulbound items when the plugin is disabled


    //TODO sauvegarder lorsque le plugin se désactive
    public void saveAll() {
        soulbound.forEach((uuid, integerItemStackMap) -> saveCache(uuid));
    }
}
