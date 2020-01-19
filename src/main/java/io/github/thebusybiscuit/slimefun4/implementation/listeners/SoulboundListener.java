package io.github.thebusybiscuit.slimefun4.implementation.listeners;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Setup.SlimefunManager;

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
            	
            	if (SlimefunManager.isItemSoulbound(item)) {
            		storeItem(p.getUniqueId(), slot, item);
            	}
            }
            
            Iterator<ItemStack> drops = e.getDrops().iterator();
            while (drops.hasNext()) {
                ItemStack item = drops.next();
                if (SlimefunManager.isItemSoulbound(item)) drops.remove();
            }

        }
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
        retrieveItems(e.getPlayer());
    }
    
    private void storeItem(UUID uuid, int slot, ItemStack item) {
		Map<Integer, ItemStack> items = soulbound.computeIfAbsent(uuid, id -> new HashMap<>());
		items.put(slot, item);
	}
	
    private void retrieveItems(Player p) {
		Map<Integer, ItemStack> items = soulbound.get(p.getUniqueId());
		
		if (items != null) {
			for (Map.Entry<Integer, ItemStack> entry : items.entrySet()) {
				p.getInventory().setItem(entry.getKey(), entry.getValue());
			}
		}
		
		soulbound.remove(p.getUniqueId());
	}
}
