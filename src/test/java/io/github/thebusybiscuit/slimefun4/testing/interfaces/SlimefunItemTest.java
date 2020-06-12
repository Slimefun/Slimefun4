package io.github.thebusybiscuit.slimefun4.testing.interfaces;

import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import io.github.thebusybiscuit.slimefun4.api.events.PlayerRightClickEvent;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.ItemUseHandler;

public interface SlimefunItemTest<T extends SlimefunItem> {

    T registerSlimefunItem(SlimefunPlugin plugin, String id);

    default void simulateNormalRightClick(Player player, T item) {
        PlayerInteractEvent e = new PlayerInteractEvent(player, Action.RIGHT_CLICK_AIR, item.getItem().clone(), null, null, EquipmentSlot.HAND);
        PlayerRightClickEvent event = new PlayerRightClickEvent(e);
        item.callItemHandler(ItemUseHandler.class, handler -> handler.onRightClick(event));
    }

}
