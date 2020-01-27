package me.mrCookieSlime.Slimefun.Objects.handlers;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.CSCoreLibPlugin.events.ItemUseEvent;

/**
 * 
 * @deprecated Please use {@link ItemUseHandler} and {@link BlockUseHandler} now.
 *
 */
@Deprecated
@FunctionalInterface
public interface ItemInteractionHandler extends ItemHandler {
	
	boolean onRightClick(ItemUseEvent e, Player p, ItemStack item);

	@Override
	default Class<? extends ItemHandler> getIdentifier() {
		return ItemInteractionHandler.class;
	}

}
