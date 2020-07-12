package io.github.thebusybiscuit.slimefun4.core.handlers;

import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerDropItemEvent;

import me.mrCookieSlime.Slimefun.Objects.handlers.ItemHandler;

@FunctionalInterface
public interface ItemDropHandler extends ItemHandler {

    boolean onItemDrop(PlayerDropItemEvent e, Player p, Item item);

    @Override
    default Class<? extends ItemHandler> getIdentifier() {
        return ItemDropHandler.class;
    }

    @Override
    default boolean isPrivate() {
        return false;
    }
}
