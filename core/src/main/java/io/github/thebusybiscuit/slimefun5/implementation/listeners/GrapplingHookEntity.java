package io.github.thebusybiscuit.slimefun5.implementation.listeners;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import io.github.thebusybiscuit.slimefun5.implementation.SlimefunItems;

final class GrapplingHookEntity {

    private final boolean dropItem;
    private final boolean wasConsumed;
    private final Arrow arrow;
    private final Entity leashTarget;

    @ParametersAreNonnullByDefault
    GrapplingHookEntity(Player p, Arrow arrow, Entity leashTarget, boolean dropItem, boolean wasConsumed) {
        this.arrow = arrow;
        this.wasConsumed = wasConsumed;
        this.leashTarget = leashTarget;
        this.dropItem = p.getGameMode() != GameMode.CREATIVE && dropItem;
    }

    @Nonnull
    public Arrow getArrow() {
        return arrow;
    }

    public void drop(@Nonnull Location l) {
        // If a grappling hook was consumed, drop one grappling hook on the floor
        if (dropItem && wasConsumed) {
            Item item = l.getWorld().dropItem(l, SlimefunItems.GRAPPLING_HOOK.item());
            item.setPickupDelay(16);
        }
    }

    public void remove() {
        // Detach the leash before removing either entity: removing a leashed mob (or its leash holder)
        // makes vanilla break the leash and drop a LEAD item at that spot. Unleashing first suppresses it.
        if (leashTarget instanceof LivingEntity && ((LivingEntity) leashTarget).isLeashed()) {
            ((LivingEntity) leashTarget).setLeashHolder(null);
        }

        if (arrow.isValid()) {
            arrow.remove();
        }

        if (leashTarget.isValid()) {
            leashTarget.remove();
        }
    }

}
