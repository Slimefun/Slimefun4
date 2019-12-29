package me.mrCookieSlime.Slimefun.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
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
import org.bukkit.util.Vector;

import io.github.thebusybiscuit.cscorelib2.protection.ProtectableAction;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Setup.SlimefunManager;
import me.mrCookieSlime.Slimefun.api.Slimefun;

public class ArmorListener implements Listener {
	
	public ArmorListener(SlimefunPlugin plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onDamage(EntityDamageEvent e) {
		if (e.getEntity() instanceof Player && !e.isCancelled()) {
			Player p = (Player) e.getEntity();
			SlimefunItem item = SlimefunItem.getByItem(p.getInventory().getBoots());
			if (item != null) {
				if (item.getID().equals("ENDER_BOOTS")
					&& Slimefun.hasUnlocked(p, item, true)
					&& e instanceof EntityDamageByEntityEvent
					&& ((EntityDamageByEntityEvent) e).getDamager() instanceof EnderPearl
				) {
					e.setCancelled(true);
				}
				else if (item.getID().equals("SLIME_BOOTS")
					&& Slimefun.hasUnlocked(p, item, true)
					&& e.getCause() == DamageCause.FALL
				) {
					e.setCancelled(true);
				}
				else if (item.getID().equals("BOOTS_OF_THE_STOMPER")
					&& Slimefun.hasUnlocked(p, item, true)
					&& e.getCause() == DamageCause.FALL
				) {
					e.setCancelled(true);
					p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ZOMBIE_BREAK_WOODEN_DOOR, 1F, 2F);
					p.setVelocity(new Vector(0.0, 0.7, 0.0));
					for (Entity n : p.getNearbyEntities(4, 4, 4)) {
						if (n instanceof LivingEntity && !n.getUniqueId().equals(p.getUniqueId())) {
							n.setVelocity(n.getLocation().toVector().subtract(p.getLocation().toVector())
								.normalize().multiply(1.4));
							if (p.getWorld().getPVP()
								&& SlimefunPlugin.getProtectionManager()
								.hasPermission(p, n.getLocation(), ProtectableAction.PVP)
							) {
								EntityDamageByEntityEvent event = new EntityDamageByEntityEvent(p, n,
									DamageCause.ENTITY_ATTACK, e.getDamage() / 2);
								Bukkit.getPluginManager().callEvent(event);
								if (!event.isCancelled()) ((LivingEntity) n).damage(e.getDamage() / 2);
							}
						}
					}

					for (BlockFace face : BlockFace.values()) {
						Block b = p.getLocation().getBlock().getRelative(BlockFace.DOWN).getRelative(face);
						p.getWorld().playEffect(b.getLocation(), Effect.STEP_SOUND, b.getType());
					}
				}
				else if (item.getID().equals("SLIME_STEEL_BOOTS")
					&& Slimefun.hasUnlocked(p, item, true)
					&& e.getCause() == DamageCause.FALL
				)
					e.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onTrample(PlayerInteractEvent e) {
		if (e.getAction() == Action.PHYSICAL
			&& e.getClickedBlock() != null
			&& e.getClickedBlock().getType() == Material.FARMLAND
			&& SlimefunManager.isItemSimilar(e.getPlayer().getInventory().getBoots(),
			SlimefunItems.FARMER_SHOES, true)
		)
			e.setCancelled(true);
	}
}
