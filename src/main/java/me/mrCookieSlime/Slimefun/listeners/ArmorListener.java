package me.mrCookieSlime.Slimefun.listeners;

import me.mrCookieSlime.Slimefun.SlimefunStartup;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Setup.SlimefunManager;
import me.mrCookieSlime.Slimefun.api.Slimefun;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class ArmorListener implements Listener {
	
	public ArmorListener(SlimefunStartup plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler(priority=EventPriority.MONITOR)
	public void onDamage(EntityDamageEvent e) {
		if (e.getEntity() instanceof Player && !e.isCancelled()) {
			Player p = (Player) e.getEntity();
			for (ItemStack armor: p.getInventory().getArmorContents()) {
				if (armor != null && SlimefunItem.getByItem(armor) != null) {
					if (SlimefunItem.getByItem(armor).isItem(SlimefunItems.ENDER_BOOTS) && Slimefun.hasUnlocked(p, SlimefunItems.ENDER_BOOTS, true)) {
						if (e instanceof EntityDamageByEntityEvent) {
							if (((EntityDamageByEntityEvent) e).getDamager() instanceof EnderPearl) e.setCancelled(true);
						}
					}
					else if (SlimefunItem.getByItem(armor).isItem(SlimefunItems.SLIME_BOOTS) && Slimefun.hasUnlocked(p, SlimefunItems.SLIME_BOOTS, true)) {
						if (e.getCause() == DamageCause.FALL) e.setCancelled(true);
					}
					else if (SlimefunItem.getByItem(armor).isItem(SlimefunItems.BOOTS_OF_THE_STOMPER) && Slimefun.hasUnlocked(p, SlimefunItems.BOOTS_OF_THE_STOMPER, true)) {
						if (e.getCause() == DamageCause.FALL) {
							e.setCancelled(true);
							p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ZOMBIE_BREAK_WOODEN_DOOR, 2F, 2F);
							p.setVelocity(new Vector(0.0, 0.7, 0.0));
							for (Entity n: p.getNearbyEntities(4, 4, 4)) {
								if (n instanceof LivingEntity && !n.getUniqueId().toString().equalsIgnoreCase(p.getUniqueId().toString())) {
									n.setVelocity(n.getLocation().toVector().subtract(p.getLocation().toVector()).normalize().multiply(1.4));
									if (p.getWorld().getPVP()) {
										EntityDamageByEntityEvent event = new EntityDamageByEntityEvent(p, n, DamageCause.ENTITY_ATTACK, e.getDamage() / 2);
										Bukkit.getPluginManager().callEvent(event);
										if (!event.isCancelled()) ((LivingEntity) n).damage(e.getDamage() / 2);
									}
								}
							}
							for (int i = 0; i < 2; i++) {
								for (BlockFace face: BlockFace.values()) {
									p.getWorld().playEffect(p.getLocation().getBlock().getRelative(BlockFace.DOWN).getRelative(face).getLocation(), Effect.STEP_SOUND, p.getLocation().getBlock().getRelative(BlockFace.DOWN).getRelative(face).getType());
								}
							}
						}
					}
					else if (SlimefunItem.getByItem(armor).isItem(SlimefunItems.SLIME_BOOTS_STEEL) && Slimefun.hasUnlocked(p, SlimefunItems.SLIME_BOOTS_STEEL, true)) {
						if (e.getCause() == DamageCause.FALL) e.setCancelled(true);
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onTrample(PlayerInteractEvent e) {
		if (e.getAction() == Action.PHYSICAL && e.getClickedBlock().getType() == Material.FARMLAND) {
			if (SlimefunManager.isItemSimiliar(e.getPlayer().getInventory().getBoots(), SlimefunItem.getItem("FARMER_SHOES"), true)) e.setCancelled(true);
		}
	}
}
