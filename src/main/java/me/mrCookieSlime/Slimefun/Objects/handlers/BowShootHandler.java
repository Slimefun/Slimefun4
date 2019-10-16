package me.mrCookieSlime.Slimefun.Objects.handlers;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

@FunctionalInterface
public interface BowShootHandler extends ItemHandler {
	
	boolean onHit(EntityDamageByEntityEvent e, LivingEntity n);
	
	default String toCodename() {
		return "BowShootHandler";
	}
}
