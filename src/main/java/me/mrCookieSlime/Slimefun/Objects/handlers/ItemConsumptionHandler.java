package me.mrCookieSlime.Slimefun.Objects.handlers;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;

/**
 * This {@link ItemHandler} is triggered when the {@link SlimefunItem} it was assigned to
 * has been consumed.
 * 
 * This {@link ItemHandler} only works for food or potions.
 * 
 * @author TheBusyBiscuit
 *
 * @see ItemHandler
 * @see SimpleSlimefunItem
 * 
 */
@FunctionalInterface
public interface ItemConsumptionHandler extends ItemHandler {

    void onConsume(PlayerItemConsumeEvent e, Player p, ItemStack item);

    @Override
    default Class<? extends ItemHandler> getIdentifier() {
        return ItemConsumptionHandler.class;
    }

}
