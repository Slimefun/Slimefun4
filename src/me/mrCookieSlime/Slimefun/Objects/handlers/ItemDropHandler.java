package me.mrCookieSlime.Slimefun.Objects.handlers;

import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerDropItemEvent;

@FunctionalInterface
public interface ItemDropHandler extends ItemHandler {

    boolean onItemDrop(PlayerDropItemEvent e, Player p, Item item);

    default String toCodename() {
        return "ItemDropHandler";
    }
}
