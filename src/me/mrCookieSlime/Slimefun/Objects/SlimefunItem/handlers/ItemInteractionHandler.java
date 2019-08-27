package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.handlers;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.CSCoreLibPlugin.events.ItemUseEvent;

@Deprecated
public abstract class ItemInteractionHandler extends ItemHandler {
	
	public abstract boolean onRightClick(ItemUseEvent e, Player p, ItemStack item);
	
	@Override
	public String toCodename() {
		return "ItemInteractionHandler";
	}

}