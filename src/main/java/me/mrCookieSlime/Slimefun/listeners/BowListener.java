package me.mrCookieSlime.Slimefun.listeners;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;

import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.BowShootHandler;
import me.mrCookieSlime.Slimefun.Objects.handlers.ItemHandler;

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
	public void onArrowSuccessfulHit(EntityDamageByEntityEvent e) {
		if (e.getDamager() instanceof Arrow && e.getEntity() instanceof LivingEntity && SlimefunPlugin.getUtilities().arrows.containsKey(e.getDamager().getUniqueId())) {
			for (ItemHandler handler : SlimefunItem.getHandlers("BowShootHandler")) {
				 if (((BowShootHandler) handler).onHit(e, (LivingEntity) e.getEntity())) {
					 break;
				 }
			 }
			 
			 SlimefunPlugin.getUtilities().arrows.remove(e.getDamager().getUniqueId());
		}
	}

}
