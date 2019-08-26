package me.mrCookieSlime.Slimefun.listeners;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;

import me.mrCookieSlime.Slimefun.SlimefunStartup;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Setup.SlimefunManager;
import me.mrCookieSlime.Slimefun.api.BackpackInventory;
import me.mrCookieSlime.Slimefun.api.PlayerProfile;

public class CoolerListener implements Listener {
	
	public CoolerListener(SlimefunStartup plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	public void onStarve(FoodLevelChangeEvent e) {
		if (e.getFoodLevel() < ((Player) e.getEntity()).getFoodLevel()) {
			Player p = (Player) e.getEntity();
			for (ItemStack item: p.getInventory().getContents()) {
				if (SlimefunManager.isItemSimiliar(item, SlimefunItem.getItem("COOLER"), false)) {
					BackpackInventory backpack = PlayerProfile.getBackpack(item);
					if (backpack != null) {
						Inventory inv = backpack.getInventory();
						ItemStack drink = null;
						
						for (ItemStack i: inv.getContents()) {
							if (i != null && i.getType() == Material.POTION && i.hasItemMeta()) {
								drink = i;
								break;
							}
						}
						if (drink != null) {
							PotionMeta im = (PotionMeta) drink.getItemMeta();
							for (PotionEffect effect: im.getCustomEffects()) {
								p.addPotionEffect(effect);
							}
							p.setSaturation(6F);
							p.playSound(p.getLocation(), Sound.ENTITY_GENERIC_DRINK, 1F, 1F);
							inv.removeItem(drink);
							backpack.markDirty();
							break;
						}
					}
				}
			}
		}
	}

}
