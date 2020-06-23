package me.mrCookieSlime.Slimefun.Objects.handlers;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;

/**
 * This is triggered when a {@link Player} interacts with an {@link Entity}.
 *
 * @author Linox
 *
 * @see ItemHandler
 * @see SimpleSlimefunItem
 *
 */
@FunctionalInterface
public interface EntityInteractHandler extends ItemHandler {

    /**
     * This function is triggered when a {@link Player} right clicks with the assigned {@link SlimefunItem}
     * in his hand.
     *
     * @param e
     *            The {@link PlayerInteractAtEntityEvent} that was triggered
     */
    void onInteract(PlayerInteractAtEntityEvent e, ItemStack item);

    @Override
    default Class<? extends ItemHandler> getIdentifier() {
        return EntityInteractHandler.class;
    }
}