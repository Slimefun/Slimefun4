package org.bukkit.event.entity;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * Compile-only stub for {@code org.bukkit.event.entity.EntityToggleGlideEvent} (Minecraft 1.9+). Not
 * shaded; on modern servers the real concrete class is used at runtime. Stubbed as a class (not an
 * interface) so that virtual method calls resolve to {@code invokevirtual}, matching the real type.
 */
public class EntityToggleGlideEvent extends EntityEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    public EntityToggleGlideEvent(LivingEntity entity, boolean isGliding) {
        super(entity);
    }

    public boolean isGliding() {
        return false;
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public void setCancelled(boolean cancel) {
        // no-op stub
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
