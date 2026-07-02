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
    private boolean removed = false;

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
        // Idempotent: this is scheduled from both the landing path and the despawn timer, and a second
        // run must not touch the entities again (re-unleashing/re-removing is what leaves stray leads).
        if (removed) {
            return;
        }

        removed = true;

        // Detach the leash first, then remove both entities unconditionally, so no path leaves behind a
        // leashed bat (whose leash later breaks and drops a lead) or a pickuppable arrow.
        if (leashTarget instanceof LivingEntity && ((LivingEntity) leashTarget).isLeashed()) {
            ((LivingEntity) leashTarget).setLeashHolder(null);
        }

        arrow.remove();
        leashTarget.remove();
    }

}
