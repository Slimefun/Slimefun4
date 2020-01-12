package me.mrCookieSlime.Slimefun.Objects.handlers;

import me.mrCookieSlime.CSCoreLibPlugin.events.ItemUseEvent;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@FunctionalInterface
public interface ItemInteractionHandler extends ItemHandler {
	
	boolean onRightClick(ItemUseEvent e, Player p, ItemStack item);

	@Override
	default Class<? extends ItemHandler> getIdentifier() {
		return ItemInteractionHandler.class;
	}

}
