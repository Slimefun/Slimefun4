package me.mrCookieSlime.Slimefun.listeners;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.ChestedHorse;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.EmeraldEnchants.EmeraldEnchants;
import me.mrCookieSlime.EmeraldEnchants.ItemEnchantment;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SoulboundItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.Talisman;
import me.mrCookieSlime.Slimefun.Objects.handlers.EntityKillHandler;
import me.mrCookieSlime.Slimefun.Objects.handlers.ItemHandler;
import me.mrCookieSlime.Slimefun.Setup.SlimefunManager;
import me.mrCookieSlime.Slimefun.api.Slimefun;
import me.mrCookieSlime.Slimefun.api.Soul;
import me.mrCookieSlime.Slimefun.utils.Utilities;

public class DamageListener implements Listener {

    private SimpleDateFormat format = new SimpleDateFormat("(MMM d, yyyy @ hh:mm)");
	private Utilities utilities;

    public DamageListener(SlimefunPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        utilities = SlimefunPlugin.getUtilities();
    }

    @EventHandler
    public void onDamage(EntityDeathEvent e) {
        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            if (p.getInventory().containsAtLeast(SlimefunItems.GPS_EMERGENCY_TRANSMITTER, 1)) {
                Slimefun.getGPSNetwork().addWaypoint(p, "&4Deathpoint &7" + format.format(new Date()), p.getLocation().getBlock().getLocation());
            }
            
            for (int slot = 0; slot < p.getInventory().getSize(); slot++) {
            	ItemStack item = p.getInventory().getItem(slot);
            	
            	if (isSoulbound(item)) {
            		Soul.storeItem(p.getUniqueId(), slot, item);
            	}
            }
            
            Iterator<ItemStack> drops = e.getDrops().iterator();
            while (drops.hasNext()) {
                ItemStack item = drops.next();
                if (isSoulbound(item)) drops.remove();
            }

        }
        
        if (e.getEntity().getKiller() instanceof Player) {
            Player p = e.getEntity().getKiller();
            ItemStack item = p.getInventory().getItemInMainHand();

            if (SlimefunPlugin.getUtilities().drops.containsKey(e.getEntity().getType())) {
                for (ItemStack drop : SlimefunPlugin.getUtilities().drops.get(e.getEntity().getType())) {
                    if (Slimefun.hasUnlocked(p, drop, true)) {
                        e.getDrops().add(drop);
                    }
                }
            }
            
            if (item != null && item.getType() != null && item.getType() != Material.AIR) {
            	for (ItemHandler handler : SlimefunItem.getHandlers("EntityKillHandler")) {
    				if (((EntityKillHandler) handler).onKill(e, e.getEntity(), p, item)) return;
    			}
            }

            if (!e.getEntity().getCanPickupItems() && Talisman.checkFor(e, SlimefunItem.getByID("HUNTER_TALISMAN")) && !(e.getEntity() instanceof Player)) {
                List<ItemStack> extraDrops = new ArrayList<>(e.getDrops());
                
            	if (e.getEntity() instanceof ChestedHorse) {
            		for (ItemStack invItem : ((ChestedHorse) e.getEntity()).getInventory().getStorageContents()) {
            			extraDrops.remove(invItem);
            		}
            		
            		//The chest is not included in getStorageContents()
            		extraDrops.remove(new ItemStack(Material.CHEST));
            	}
            	
                for (ItemStack drop: extraDrops) {
                    e.getDrops().add(drop);
                }
            }
        }
    }

    private boolean isSoulbound(ItemStack item) {
    	if (item == null || item.getType() == null || item.getType() == Material.AIR) return false;
    	else if (SlimefunManager.isItemSimiliar(item, SlimefunItems.BOUND_BACKPACK, false)) return true;
    	else if (SlimefunItem.getByItem(removeEnchantments(item)) instanceof SoulboundItem) return true;
    	else return false;
	}

	@EventHandler
    public void onArrowHit(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player && e.getCause() == DamageCause.FALL && utilities.damage.contains(e.getEntity().getUniqueId())) {
            e.setCancelled(true);
            utilities.damage.remove(e.getEntity().getUniqueId());
        }
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
        Soul.retrieveItems(e.getPlayer());
    }

    private ItemStack removeEnchantments(ItemStack itemStack) {
        ItemStack strippedItem = itemStack.clone();

        for (Enchantment enchantment : itemStack.getEnchantments().keySet()) {
            strippedItem.removeEnchantment(enchantment);
        }

        if (SlimefunPlugin.getHooks().isEmeraldEnchantsInstalled()) {
            for(ItemEnchantment enchantment : EmeraldEnchants.getInstance().getRegistry().getEnchantments(itemStack)){
                EmeraldEnchants.getInstance().getRegistry().applyEnchantment(strippedItem, enchantment.getEnchantment(), 0);
            }
        }
        return strippedItem;
    }
}
