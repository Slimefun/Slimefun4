package me.mrCookieSlime.Slimefun.Objects.tasks;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

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
	private final Set<PotionEffect> radiationEffects;
	
	public ArmorTask() {
		Set<PotionEffect> effects = new HashSet<>();
		effects.add(new PotionEffect(PotionEffectType.WITHER, 400, 2));
		effects.add(new PotionEffect(PotionEffectType.BLINDNESS, 400, 3));
		effects.add(new PotionEffect(PotionEffectType.CONFUSION, 400, 3));
		effects.add(new PotionEffect(PotionEffectType.WEAKNESS, 400, 2));
		effects.add(new PotionEffect(PotionEffectType.SLOW, 400, 1));
		effects.add(new PotionEffect(PotionEffectType.SLOW_DIGGING, 400, 1));
		radiationEffects = Collections.unmodifiableSet(effects);
	}

	@Override
	public void run() {
		for (Player p: Bukkit.getOnlinePlayers()) {
			if (!p.isValid() || p.isDead()) {
				continue;
			}
			
			PlayerProfile.get(p, profile -> {
				ItemStack[] armor = p.getInventory().getArmorContents();
				HashedArmorpiece[] cachedArmor = profile.getArmor();
				
				for (int slot = 0; slot < 4; slot++) {
					ItemStack item = armor[slot];
					HashedArmorpiece armorpiece = cachedArmor[slot];
					
					if (armorpiece.hasDiverged(item)) {
						SlimefunItem sfItem = SlimefunItem.getByItem(item);
						if (!(sfItem instanceof SlimefunArmorPiece) || !Slimefun.hasUnlocked(p, sfItem, true)) {
							sfItem = null;
						}
						
						armorpiece.update(item, sfItem);
					}
					
					if (item != null && armorpiece.getItem().isPresent()) {
						Bukkit.getScheduler().runTask(SlimefunPlugin.instance, () -> {
							for (PotionEffect effect: armorpiece.getItem().get().getEffects()) {
								p.removePotionEffect(effect.getType());
								p.addPotionEffect(effect);
							}
						});
					}
				}
				
				if (SlimefunManager.isItemSimilar(p.getInventory().getHelmet(), SlimefunItems.SOLAR_HELMET, true) 
					&& Slimefun.hasUnlocked(p, SlimefunItem.getByID("SOLAR_HELMET"), true) 
					&& (p.getWorld().getTime() < 12300 || p.getWorld().getTime() > 23850) 
					&& p.getEyeLocation().getBlock().getLightFromSky() == 15) 
				{
					ItemEnergy.chargeInventory(p, ((Double) Slimefun.getItemValue("SOLAR_HELMET", "charge-amount")).floatValue());
				}

				for (ItemStack radioactive: utilities.radioactiveItems) {
					if (SlimefunManager.containsSimilarItem(p.getInventory(), radioactive, true)) {
						// Check if player is wearing the hazmat suit
						// If so, break the loop
						if (SlimefunManager.isItemSimilar(SlimefunItems.SCUBA_HELMET, p.getInventory().getHelmet(), true) &&
								SlimefunManager.isItemSimilar(SlimefunItems.HAZMATSUIT_CHESTPLATE, p.getInventory().getChestplate(), true) &&
								SlimefunManager.isItemSimilar(SlimefunItems.HAZMATSUIT_LEGGINGS, p.getInventory().getLeggings(), true) &&
								SlimefunManager.isItemSimilar(SlimefunItems.RUBBER_BOOTS, p.getInventory().getBoots(), true)) {
							break;
						}

						// If the item is enabled in the world, then make radioactivity do its job
						if (Slimefun.isEnabled(p, radioactive, false)) {
							SlimefunPlugin.getLocal().sendMessage(p, "messages.radiation");
							Bukkit.getScheduler().runTask(SlimefunPlugin.instance, () -> {
								p.addPotionEffects(radiationEffects);
								p.setFireTicks(400);
							});
							
							break;
						}
					}
				}
			});
		}
	}

}
