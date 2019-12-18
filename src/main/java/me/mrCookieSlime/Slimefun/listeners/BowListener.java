package me.mrCookieSlime.Slimefun.listeners;

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

import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.BowShootHandler;
import me.mrCookieSlime.Slimefun.Objects.handlers.ItemHandler;
import me.mrCookieSlime.Slimefun.api.Slimefun;

public class BowListener implements Listener {
	
	public BowListener(SlimefunPlugin plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler
	public void onBowUse(EntityShootBowEvent e) {
		if (e.getEntity() instanceof Player && e.getProjectile() instanceof Arrow && SlimefunItem.getByItem(e.getBow()) != null) {
			SlimefunPlugin.getUtilities().arrows.put(e.getProjectile().getUniqueId(), e.getBow());
		}
	}
	
	@EventHandler
	public void onArrowHit(final ProjectileHitEvent e) {
		Slimefun.runSync(() -> {
			if (e.getEntity().isValid()) {
				SlimefunPlugin.getUtilities().arrows.remove(e.getEntity().getUniqueId());
				
				if (e.getEntity() instanceof Arrow) {
					handleGrapplingHook((Arrow) e.getEntity());
				}
			}
		}, 4L);
	}
	
	private void handleGrapplingHook(Arrow arrow) {
		if (arrow != null && arrow.getShooter() instanceof Player && SlimefunPlugin.getUtilities().jumpState.containsKey(((Player) arrow.getShooter()).getUniqueId())) {
			final Player p = (Player) arrow.getShooter();
			
			if (p.getGameMode() != GameMode.CREATIVE && (boolean) SlimefunPlugin.getUtilities().jumpState.get(p.getUniqueId())) {
				arrow.getWorld().dropItem(arrow.getLocation(), SlimefunItems.GRAPPLING_HOOK);
			}
			
			if (p.getLocation().distance(arrow.getLocation()) < 3.0D) {
				if (arrow.getLocation().getY() > p.getLocation().getY()) {
					p.setVelocity(new Vector(0.0D, 0.25D, 0.0D));
				}
				else {
					p.setVelocity(arrow.getLocation().toVector().subtract(p.getLocation().toVector()));
				}

				for (Entity n : SlimefunPlugin.getUtilities().remove.get(p.getUniqueId())) {
					if (n.isValid()) n.remove();
				}

				Slimefun.runSync(() -> {
					SlimefunPlugin.getUtilities().jumpState.remove(p.getUniqueId());
					SlimefunPlugin.getUtilities().remove.remove(p.getUniqueId());
				}, 20L);
			}
			else {
				Location l = p.getLocation();
				l.setY(l.getY() + 0.5D);
				p.teleport(l);

				double g = -0.08D;
				double d = arrow.getLocation().distance(l);
				double t = d;
				double vX = (1.0D + 0.08000000000000001D * t) * (arrow.getLocation().getX() - l.getX()) / t;
				double vY = (1.0D + 0.04D * t) * (arrow.getLocation().getY() - l.getY()) / t - 0.5D * g * t;
				double vZ = (1.0D + 0.08000000000000001D * t) * (arrow.getLocation().getZ() - l.getZ()) / t;

				Vector v = p.getVelocity();

				v.setX(vX);
				v.setY(vY);
				v.setZ(vZ);

				p.setVelocity(v);

				for (Entity n : SlimefunPlugin.getUtilities().remove.get(p.getUniqueId())) {
					if (n.isValid()) n.remove();
				}

				Slimefun.runSync(() -> {
					SlimefunPlugin.getUtilities().jumpState.remove(p.getUniqueId());
					SlimefunPlugin.getUtilities().remove.remove(p.getUniqueId());
				}, 20L);
			}
		}
	}

	@EventHandler
	public void onArrowSuccessfulHit(EntityDamageByEntityEvent e) {
		if (e.getDamager() instanceof Arrow) {
			if (SlimefunPlugin.getUtilities().arrows.containsKey(e.getDamager().getUniqueId()) && e.getEntity() instanceof LivingEntity) {
				 for (ItemHandler handler : SlimefunItem.getHandlers("BowShootHandler")) {
					 if (((BowShootHandler) handler).onHit(e, (LivingEntity) e.getEntity())) {
						 break;
					 }
				 }
				 
				 SlimefunPlugin.getUtilities().arrows.remove(e.getDamager().getUniqueId());
			}
			
			handleGrapplingHook((Arrow) e.getDamager());
		}
	}

}
