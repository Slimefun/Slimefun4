package io.github.thebusybiscuit.slimefun4.core.handlers;

import javax.annotation.Nonnull;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.Slimefun.Objects.handlers.ItemHandler;

/**
 * This is triggered when a {@link Player} attacks an {@link Entity}.
 *
 * @author Mooy1
 *
 */
@FunctionalInterface
public interface EntityHitHandler extends ItemHandler {

    void onHit(@Nonnull EntityDamageByEntityEvent e, @Nonnull Player player, @Nonnull ItemStack item);

    @Override
    default Class<? extends ItemHandler> getIdentifier() {
        return EntityHitHandler.class;
    }

}
