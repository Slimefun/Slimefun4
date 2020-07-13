package io.github.thebusybiscuit.slimefun4.implementation.listeners;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;

import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;

final class GrapplingHookEntity {

    private final boolean dropItem;

    private final Arrow arrow;
    private final Entity leashTarget;

    GrapplingHookEntity(Player p, Arrow arrow, Entity leashTarget, boolean dropItem) {
        this.arrow = arrow;
        this.leashTarget = leashTarget;
        this.dropItem = p.getGameMode() != GameMode.CREATIVE && dropItem;
    }

    public Arrow getArrow() {
        return arrow;
    }

    public void drop(Location l) {
        if (dropItem) {
            Item item = l.getWorld().dropItem(l, SlimefunItems.GRAPPLING_HOOK.clone());
            item.setPickupDelay(16);
        }
    }

    public void remove() {
        if (arrow.isValid()) {
            arrow.remove();
        }

        if (leashTarget.isValid()) {
            leashTarget.remove();
        }
    }

}