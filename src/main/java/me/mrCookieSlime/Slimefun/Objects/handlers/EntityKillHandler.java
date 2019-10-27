package me.mrCookieSlime.Slimefun.Objects.handlers;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

@FunctionalInterface
public interface EntityKillHandler extends ItemHandler {
	
	boolean onKill(EntityDeathEvent e, Entity entity, Player killer, ItemStack item);
	
	default String toCodename() {
		return "EntityKillHandler";
	}
}
