package me.mrCookieSlime.Slimefun.Objects.handlers;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import io.github.thebusybiscuit.slimefun4.implementation.items.weapons.SlimefunBow;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;

/**
 * This {@link ItemHandler} is triggered when the {@link SlimefunItem} it was assigned to
 * is a {@link SlimefunBow} and an Arrow fired from this bow hit a {@link LivingEntity}.
 * 
 * @author TheBusyBiscuit
 *
 * @see ItemHandler
 * @see SlimefunBow
 * 
 */
@FunctionalInterface
public interface BowShootHandler extends ItemHandler {

    void onHit(EntityDamageByEntityEvent e, LivingEntity n);

    @Override
    default Class<? extends ItemHandler> getIdentifier() {
        return BowShootHandler.class;
    }
}
