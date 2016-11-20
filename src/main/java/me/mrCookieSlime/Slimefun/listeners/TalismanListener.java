package me.mrCookieSlime.Slimefun.listeners;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import me.mrCookieSlime.Slimefun.SlimefunStartup;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.Talisman;
import me.mrCookieSlime.Slimefun.Setup.SlimefunManager;
import me.mrCookieSlime.Slimefun.api.Slimefun;

import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.event.player.PlayerToggleSprintEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class TalismanListener implements Listener {
	
	public TalismanListener(SlimefunStartup plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler(priority=EventPriority.MONITOR)
	public void onDamageGet(EntityDamageEvent e) {
		if (!e.isCancelled()) {
			if (e instanceof EntityDamageByEntityEvent) {
				if (((EntityDamageByEntityEvent) e).getDamager() instanceof Player && SlimefunStartup.chance(100, 45)) {
					if (SlimefunManager.isItemSimiliar(((Player) ((EntityDamageByEntityEvent) e).getDamager()).getInventory().getItemInMainHand(), SlimefunItem.getItem("BLADE_OF_VAMPIRES"), true)) {
						((Player) ((EntityDamageByEntityEvent) e).getDamager()).playSound(((EntityDamageByEntityEvent) e).getDamager().getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 0.7F, 0.7F);
						((Player) ((EntityDamageByEntityEvent) e).getDamager()).addPotionEffect(new PotionEffect(PotionEffectType.HEAL, 1, 1));
					}
				}
			}
			if (e.getEntity() instanceof Player) {
				if (!e.isCancelled()) {
					if (e.getCause() == DamageCause.LAVA) Talisman.checkFor(e, SlimefunItem.getByName("LAVA_TALISMAN"));
					if (e.getCause() == DamageCause.DROWNING) Talisman.checkFor(e, SlimefunItem.getByName("WATER_TALISMAN"));
					if (e.getCause() == DamageCause.FALL) Talisman.checkFor(e, SlimefunItem.getByName("ANGEL_TALISMAN"));
					if (e.getCause() == DamageCause.FIRE) Talisman.checkFor(e, SlimefunItem.getByName("FIRE_TALISMAN"));
					if (e.getCause() == DamageCause.ENTITY_ATTACK) Talisman.checkFor(e, SlimefunItem.getByName("WARRIOR_TALISMAN"));
					if (e.getCause() == DamageCause.ENTITY_ATTACK) Talisman.checkFor(e, SlimefunItem.getByName("KNIGHT_TALISMAN"));
					if (e.getCause() == DamageCause.PROJECTILE) {
						if (Talisman.checkFor(e, SlimefunItem.getByName("WHIRLWIND_TALISMAN"))) {
							if (((EntityDamageByEntityEvent) e).getDamager() instanceof Projectile) {
								Vector direction = ((Player) e.getEntity()).getEyeLocation().getDirection().multiply(2.0);
								Projectile projectile = (Projectile) e.getEntity().getWorld().spawnEntity(((LivingEntity) e.getEntity()).getEyeLocation().add(direction.getX(), direction.getY(), direction.getZ()), ((EntityDamageByEntityEvent) e).getDamager().getType());
								projectile.setVelocity(direction);
								((EntityDamageByEntityEvent) e).getDamager().remove();
							}
						}
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onItemBreak(PlayerItemBreakEvent e) {
		if (Talisman.checkFor(e, SlimefunItem.getByName("ANVIL_TALISMAN"))) e.getBrokenItem().setAmount(1);
	}
	
	@EventHandler
	public void onSprint(PlayerToggleSprintEvent e) {
		if (e.isSprinting()) Talisman.checkFor(e, SlimefunItem.getByName("TRAVELLER_TALISMAN"));
	}
	
	@EventHandler
	public void onEnchant(EnchantItemEvent e) {
		if (Talisman.checkFor(e, SlimefunItem.getByName("MAGICIAN_TALISMAN"))) {
			List<String> enchantments = new ArrayList<String>();
			for (Enchantment en: Enchantment.values()) {
				for (int i = 1; i <= en.getMaxLevel(); i++) {
					if ((Boolean) Slimefun.getItemValue("MAGICIAN_TALISMAN", "allow-enchantments." + en.getName() + ".level." + i) && en.canEnchantItem(e.getItem())) enchantments.add(en.getName() + "-" + i);
				}
			}
			String enchant = enchantments.get(SlimefunStartup.randomize(enchantments.size()));
			e.getEnchantsToAdd().put(Enchantment.getByName(enchant.split("-")[0]), Integer.parseInt(enchant.split("-")[1]));
			
		}
		if (!e.getEnchantsToAdd().containsKey(Enchantment.SILK_TOUCH) && Enchantment.LOOT_BONUS_BLOCKS.canEnchantItem(e.getItem())) {
			if (Talisman.checkFor(e, SlimefunItem.getByName("WIZARD_TALISMAN"))) {
				if (e.getEnchantsToAdd().containsKey(Enchantment.LOOT_BONUS_BLOCKS)) e.getEnchantsToAdd().remove(Enchantment.LOOT_BONUS_BLOCKS);
				Set<Enchantment> enchantments = e.getEnchantsToAdd().keySet();
				for (Enchantment en: enchantments) {
					if (SlimefunStartup.chance(100, 40)) e.getEnchantsToAdd().put(en, SlimefunStartup.randomize(2) + 1);
				}
				
				e.getItem().addUnsafeEnchantment(Enchantment.LOOT_BONUS_BLOCKS, SlimefunStartup.randomize(2) + 3);
			}
		}
	}
}
