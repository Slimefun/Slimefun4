package io.github.thebusybiscuit.slimefun4.core.handlers;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.implementation.items.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.ItemHandler;

/**
 * This is triggered when a {@link Player} interacts with an {@link Entity}.
 *
 * @author Linox
 *
 * @see ItemHandler
 * @see SimpleSlimefunItem
 * @see PlayerInteractAtEntityEvent
 *
 */
@FunctionalInterface
public interface EntityInteractHandler extends ItemHandler {

    /**
     * This function is triggered when a {@link Player} right clicks with the assigned {@link SlimefunItem}
     * in his hand.
     *
     * @param p
     *            The {@link Player} that right clicked
     * @param entity
     *            The {@link Entity} that was right clicked on
     * @param item
     *            The {@link ItemStack} that was held and used while triggering
     * @param offHand
     *            <code>true</code> if the {@link EquipmentSlot} is off hand
     */
    void onInteract(Player p, Entity entity, ItemStack item, boolean offHand);

    @Override
    default Class<? extends ItemHandler> getIdentifier() {
        return EntityInteractHandler.class;
    }
}