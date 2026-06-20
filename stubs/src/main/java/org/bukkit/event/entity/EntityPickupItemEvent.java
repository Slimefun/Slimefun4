package org.bukkit.event.entity;

import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * Compile-only stub for {@code org.bukkit.event.entity.EntityPickupItemEvent} (Minecraft 1.12+). Not
 * shaded; on modern servers the real concrete class is used at runtime. Stubbed as a class (not an
 * interface) so that virtual method calls resolve to {@code invokevirtual}, matching the real type.
 */
public class EntityPickupItemEvent extends EntityEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    public EntityPickupItemEvent(LivingEntity entity, Item item, int remaining) {
        super(entity);
    }

    @Override
    public LivingEntity getEntity() {
        return (LivingEntity) super.getEntity();
    }

    public Item getItem() {
        return null;
    }

    public int getRemaining() {
        return 0;
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
