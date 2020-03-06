package me.mrCookieSlime.Slimefun.Objects.handlers;

import io.github.thebusybiscuit.slimefun4.api.events.PlayerRightClickEvent;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;

/**
 * This {@link ItemHandler} is triggered when the {@link SlimefunItem} it was assigned to
 * is right-clicked.
 * 
 * @author TheBusyBiscuit
 *
 * @see ItemHandler
 * @see SimpleSlimefunItem
 * 
 */
@FunctionalInterface
public interface ItemUseHandler extends ItemHandler {

    void onRightClick(PlayerRightClickEvent e);

    @Override
    default Class<? extends ItemHandler> getIdentifier() {
        return ItemUseHandler.class;
    }

}
