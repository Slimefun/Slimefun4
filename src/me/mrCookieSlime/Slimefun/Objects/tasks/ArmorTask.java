package me.mrCookieSlime.Slimefun.Objects.tasks;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunArmorPiece;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Setup.SlimefunManager;
import me.mrCookieSlime.Slimefun.api.HashedArmorpiece;
import me.mrCookieSlime.Slimefun.api.PlayerProfile;
import me.mrCookieSlime.Slimefun.api.Slimefun;
import me.mrCookieSlime.Slimefun.api.energy.ItemEnergy;
import me.mrCookieSlime.Slimefun.utils.Utilities;

public class ArmorTask implements Runnable {
	
	private final Utilities utilities = SlimefunPlugin.getUtilities();

	@Override
	public void run() {
		for (Player p: Bukkit.getOnlinePlayers()) {
			if (!p.isValid() || p.isDead()) {
				continue;
			}
			
			ItemStack[] armor = p.getInventory().getArmorContents();
			HashedArmorpiece[] cachedArmor = PlayerProfile.get(p).getArmor();
			
			for (int slot = 0; slot < 4; slot++) {
				ItemStack item = armor[slot];
				HashedArmorpiece armorpiece = cachedArmor[slot];
				
				if (armorpiece.hasDiverged(item)) {
					SlimefunItem sfItem = SlimefunItem.getByItem(item);
					if (!(sfItem instanceof SlimefunArmorPiece) || !Slimefun.hasUnlocked(p, sfItem, true)) {
						sfItem = null;
					}
					
					armorpiece.update(item == null ? 0: item.hashCode(), sfItem);
				}
				
				if (cachedArmor[slot].getItem().isPresent()) {
					for (PotionEffect effect: cachedArmor[slot].getItem().get().getEffects()) {
						p.removePotionEffect(effect.getType());
						p.addPotionEffect(effect);
					}
				}
			}
			
			if (SlimefunManager.isItemSimiliar(p.getInventory().getHelmet(), SlimefunItems.SOLAR_HELMET, false) && Slimefun.hasUnlocked(p, SlimefunItem.getByID("SOLAR_HELMET"), true) && (p.getWorld().getTime() < 12300 || p.getWorld().getTime() > 23850 && p.getEyeLocation().getBlock().getLightFromSky() == 15)) {
				ItemEnergy.chargeInventory(p, Float.valueOf(String.valueOf(Slimefun.getItemValue("SOLAR_HELMET", "charge-amount"))));
			}

			for (ItemStack radioactive: utilities.radioactiveItems) {
				if (SlimefunManager.containsSimilarItem(p.getInventory(), radioactive, true)) {
					// Check if player is wearing the hazmat suit
					// If so, break the loop
					if (SlimefunManager.isItemSimiliar(SlimefunItems.SCUBA_HELMET, p.getInventory().getHelmet(), true) &&
							SlimefunManager.isItemSimiliar(SlimefunItems.HAZMATSUIT_CHESTPLATE, p.getInventory().getChestplate(), true) &&
							SlimefunManager.isItemSimiliar(SlimefunItems.HAZMATSUIT_LEGGINGS, p.getInventory().getLeggings(), true) &&
							SlimefunManager.isItemSimiliar(SlimefunItems.RUBBER_BOOTS, p.getInventory().getBoots(), true)) {
						break;
					}

					// If the item is enabled in the world, then make radioactivity do its job
					if (Slimefun.isEnabled(p, radioactive, false)) {
						p.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 400, 3));
						p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 400, 3));
						p.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 400, 3));
						p.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 400, 3));
						p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 400, 1));
						p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 400, 1));
						p.setFireTicks(400);
						break; 
						// Break the loop to save some calculations
					}
				}
			}
		}
	}

}
