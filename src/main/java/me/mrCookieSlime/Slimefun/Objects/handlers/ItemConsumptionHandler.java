package me.mrCookieSlime.Slimefun.Objects.handlers;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;

@FunctionalInterface
public interface ItemConsumptionHandler extends ItemHandler {
	
	boolean onConsume(PlayerItemConsumeEvent e, Player p, ItemStack item);
	
	default String toCodename() {
		return "ItemConsumptionHandler";
	}

}
