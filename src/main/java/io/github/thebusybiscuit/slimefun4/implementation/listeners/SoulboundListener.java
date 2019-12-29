package io.github.thebusybiscuit.slimefun4.implementation.listeners;

import java.util.Iterator;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Setup.SlimefunManager;
import me.mrCookieSlime.Slimefun.api.Soul;

public class SoulboundListener implements Listener {

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
            		Soul.storeItem(p.getUniqueId(), slot, item);
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
        Soul.retrieveItems(e.getPlayer());
    }
}
