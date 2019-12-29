package me.mrCookieSlime.Slimefun.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.EntityKillHandler;
import me.mrCookieSlime.Slimefun.Objects.handlers.ItemHandler;
import me.mrCookieSlime.Slimefun.Setup.SlimefunManager;
import me.mrCookieSlime.Slimefun.api.Slimefun;

public class DamageListener implements Listener {

    public DamageListener(SlimefunPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onDamage(EntityDeathEvent e) {
        if (e.getEntity().getKiller() instanceof Player) {
            Player p = e.getEntity().getKiller();
            ItemStack item = p.getInventory().getItemInMainHand();
            
            if (SlimefunPlugin.getUtilities().drops.containsKey(e.getEntity().getType())) {
                for (ItemStack drop : SlimefunPlugin.getUtilities().drops.get(e.getEntity().getType())) {
                    if (Slimefun.hasUnlocked(p, drop, true)) {
                        if (SlimefunManager.isItemSimilar(drop, SlimefunItems.BASIC_CIRCUIT_BOARD, true) && !((boolean) Slimefun.getItemValue("BASIC_CIRCUIT_BOARD", "drop-from-golems"))) {
                        	continue;
                        }
                        
                        e.getDrops().add(drop);
                    }
                }
            }
            
            if (item.getType() != Material.AIR && Slimefun.hasUnlocked(p, item, true)) {
            	for (ItemHandler handler : SlimefunItem.getHandlers("EntityKillHandler")) {
    				if (((EntityKillHandler) handler).onKill(e, e.getEntity(), p, item)) return;
    			}
            }
        }
    }

	@EventHandler
    public void onArrowHit(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player && e.getCause() == DamageCause.FALL && SlimefunPlugin.getUtilities().damage.contains(e.getEntity().getUniqueId())) {
            e.setCancelled(true);
            SlimefunPlugin.getUtilities().damage.remove(e.getEntity().getUniqueId());
        }
    }
}
