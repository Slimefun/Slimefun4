package me.mrCookieSlime.Slimefun.Objects.handlers;

import io.github.thebusybiscuit.slimefun4.api.events.ItemUseEvent;

@FunctionalInterface
public interface BlockUseHandler extends ItemHandler {
	
	void onRightClick(ItemUseEvent e);

	@Override
	default Class<? extends ItemHandler> getIdentifier() {
		return BlockUseHandler.class;
	}

}
