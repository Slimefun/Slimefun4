package me.mrCookieSlime.Slimefun.listeners;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.util.Vector;

import me.mrCookieSlime.Slimefun.SlimefunStartup;
import me.mrCookieSlime.Slimefun.Variables;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.handlers.BowShootHandler;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.handlers.ItemHandler;

public class BowListener implements Listener {
	
	public BowListener(SlimefunStartup plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	public void onBowUse(EntityShootBowEvent e) {
		if (!(e.getEntity() instanceof Player) || !(e.getProjectile() instanceof Arrow)) return;
		if (SlimefunItem.getByItem(e.getBow()) != null) Variables.arrows.put(e.getProjectile().getUniqueId(), e.getBow());
	}
	
	@EventHandler
	public void onArrowHit(final ProjectileHitEvent e) {
		Bukkit.getScheduler().scheduleSyncDelayedTask(SlimefunStartup.instance, () -> {
			if (!e.getEntity().isValid()) return;
			if (Variables.arrows.containsKey(e.getEntity().getUniqueId())) Variables.arrows.remove(e.getEntity().getUniqueId());
			if (e.getEntity() instanceof Arrow) handleGrapplingHook((Arrow) e.getEntity());
		}, 4L);
	}
	
	private void handleGrapplingHook(Arrow arrow) {
		if (arrow != null) {
			if (arrow.getShooter() instanceof Player && Variables.jump_state.containsKey(((Player) arrow.getShooter()).getUniqueId())) {
				final Player p = (Player) arrow.getShooter();
				if (p.getGameMode() != GameMode.CREATIVE && Variables.jump_state.get(p.getUniqueId())) arrow.getWorld().dropItem(arrow.getLocation(), SlimefunItem.getItem("GRAPPLING_HOOK"));
				if (p.getLocation().distance(arrow.getLocation()) < 3.0D) {
					if (arrow.getLocation().getY() > p.getLocation().getY()) {
						p.setVelocity(new Vector(0.0D, 0.25D, 0.0D));
					}
					else p.setVelocity(arrow.getLocation().toVector().subtract(p.getLocation().toVector()));
					for (Entity n: Variables.remove.get(p.getUniqueId())) {
		    	    	n.remove();
		    	    }
		    	    
		    	    Bukkit.getScheduler().scheduleSyncDelayedTask(SlimefunStartup.instance, () -> {
						Variables.jump_state.remove(p.getUniqueId());
						Variables.remove.remove(p.getUniqueId());
					}, 20L);
				}
				else {
					Location l = p.getLocation();
					l.setY(l.getY() + 0.5D);
					p.teleport(l);
					
					double g = -0.08D;
		    	    double d = arrow.getLocation().distance(l);
		    	    double t = d;
		    	    double v_x = (1.0D + 0.08000000000000001D * t) * (arrow.getLocation().getX() - l.getX()) / t;
		    	    double v_y = (1.0D + 0.04D * t) * (arrow.getLocation().getY() - l.getY()) / t - 0.5D * g * t;
		    	    double v_z = (1.0D + 0.08000000000000001D * t) * (arrow.getLocation().getZ() - l.getZ()) / t;

		    	    Vector v = p.getVelocity();
		    	    
		    	    v.setX(v_x);
		    	    v.setY(v_y);
		    	    v.setZ(v_z);
		    	    
		    	    p.setVelocity(v);
		    	    
		    	    for (Entity n: Variables.remove.get(p.getUniqueId())) {
		    	    	n.remove();
		    	    }
		    	    
		    	    Bukkit.getScheduler().scheduleSyncDelayedTask(SlimefunStartup.instance, () -> {
						Variables.jump_state.remove(p.getUniqueId());
						Variables.remove.remove(p.getUniqueId());
					}, 20L);
				}
			}
		}
	}

	@EventHandler
	public void onArrowSuccessfulHit(EntityDamageByEntityEvent e) {
		if (e.getDamager() instanceof Arrow) {
			if (Variables.arrows.containsKey(e.getDamager().getUniqueId()) && e.getEntity() instanceof LivingEntity) {
				 for (ItemHandler handler: SlimefunItem.getHandlers("BowShootHandler")) {
					 if (((BowShootHandler) handler).onHit(e, (LivingEntity) e.getEntity())) break;
				 }
				 Variables.arrows.remove(e.getDamager().getUniqueId());
			}
			
			handleGrapplingHook((Arrow) e.getDamager());
		}
	}

}
