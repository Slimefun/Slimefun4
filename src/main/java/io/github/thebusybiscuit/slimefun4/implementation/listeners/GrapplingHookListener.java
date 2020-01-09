package io.github.thebusybiscuit.slimefun4.implementation.listeners;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.util.Vector;

import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.api.Slimefun;

public class GrapplingHookListener implements Listener {
	
	public GrapplingHookListener(SlimefunPlugin plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler
	public void onArrowSuccessfulHit(EntityDamageByEntityEvent e) {
		if (e.getDamager() instanceof Arrow) {
			handleGrapplingHook((Arrow) e.getDamager());
		}
	}
	
	@EventHandler
	public void onArrowHit(ProjectileHitEvent e) {
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
			Player p = (Player) arrow.getShooter();
			
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
}
