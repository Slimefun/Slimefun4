package io.github.thebusybiscuit.slimefun4.implementation.listeners;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

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
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import io.github.thebusybiscuit.cscorelib2.protection.ProtectableAction;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Setup.SlimefunManager;
import me.mrCookieSlime.Slimefun.api.Slimefun;

public class SlimefunBootsListener implements Listener {
	
	private final Map<String, Predicate<EntityDamageEvent>> cancelledEvents = new HashMap<>();
	
	public SlimefunBootsListener(SlimefunPlugin plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		
		cancelledEvents.put("ENDER_BOOTS", e ->
			e instanceof EntityDamageByEntityEvent 
			&& ((EntityDamageByEntityEvent) e).getDamager() instanceof EnderPearl
		);
		
		cancelledEvents.put("BOOTS_OF_THE_STOMPER", e -> {
			if (e.getCause() == DamageCause.FALL) {
				stomp(e);
				return true;
			}
			
			return false;
		});
		
		cancelledEvents.put("SLIME_BOOTS", e ->
			e.getCause() == DamageCause.FALL
		);
		
		cancelledEvents.put("SLIME_STEEL_BOOTS", e ->
			e.getCause() == DamageCause.FALL
		);
	}
	
	private void stomp(EntityDamageEvent e) {
		Player p = (Player) e.getEntity();
		p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ZOMBIE_BREAK_WOODEN_DOOR, 1F, 2F);
		p.setVelocity(new Vector(0.0, 0.7, 0.0));
		
		for (Entity n : p.getNearbyEntities(4, 4, 4)) {
			if (n instanceof LivingEntity && !n.getUniqueId().equals(p.getUniqueId())) {
				Vector velocity = n.getLocation().toVector().subtract(p.getLocation().toVector()).normalize().multiply(1.4);
				n.setVelocity(velocity);
				
				if (!(n instanceof Player) || (p.getWorld().getPVP() && SlimefunPlugin.getProtectionManager().hasPermission(p, n.getLocation(), ProtectableAction.PVP))) {
					EntityDamageByEntityEvent event = new EntityDamageByEntityEvent(p, n, DamageCause.ENTITY_ATTACK, e.getDamage() / 2);
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
	
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled=true)
	public void onDamage(EntityDamageEvent e) {
		if (e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			SlimefunItem boots = SlimefunItem.getByItem(p.getInventory().getBoots());
			
			if (boots != null) {
				for (Map.Entry<String, Predicate<EntityDamageEvent>> event : cancelledEvents.entrySet()) {
					if (boots.getID().equals(event.getKey())) {
						if (Slimefun.hasUnlocked(p, boots, true) && event.getValue().test(e)) {
							e.setCancelled(true);
						}
						
						break;
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onTrample(PlayerInteractEvent e) {
		if (e.getAction() != Action.PHYSICAL) return;
		if (e.getClickedBlock() == null) return;
		if (e.getClickedBlock().getType() != Material.FARMLAND) return;
		
		ItemStack boots = e.getPlayer().getInventory().getBoots();
		if (SlimefunManager.isItemSimilar(boots, SlimefunItems.FARMER_SHOES, true) && Slimefun.hasUnlocked(e.getPlayer(), boots, true)) {
			e.setCancelled(true);
		}
	}
}
