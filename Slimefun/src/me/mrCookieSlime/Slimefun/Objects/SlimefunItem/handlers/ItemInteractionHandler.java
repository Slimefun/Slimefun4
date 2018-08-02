package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.handlers;

import me.mrCookieSlime.CSCoreLibPlugin.events.ItemUseEvent;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public abstract class ItemInteractionHandler extends ItemHandler {
	
	public abstract boolean onRightClick(ItemUseEvent e, Player p, ItemStack item);
	
	@Override
	public String toCodename() {
		return "ItemInteractionHandler";
	}

}
