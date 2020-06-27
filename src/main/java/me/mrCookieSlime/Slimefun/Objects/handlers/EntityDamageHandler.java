package me.mrCookieSlime.Slimefun.Objects.handlers;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;

/**
 * If this {@link ItemHandler} is added to a {@link SlimefunItem} it will listen
 * for any {@link EntityDamageByEntityEvent} that was triggered by a {@link Player} using
 * the {@link SlimefunItem} this {@link EntityDamageHandler} was linked to.
 *
 * @author Linox
 *
 * @see ItemHandler
 * @see SimpleSlimefunItem
 *
 */
@FunctionalInterface
public interface EntityDamageHandler extends ItemHandler {

    void onDamage(EntityDamageByEntityEvent e, Entity entity, ItemStack item);

    @Override
    default Class<? extends ItemHandler> getIdentifier() {
        return EntityDamageHandler.class;
    }
}
