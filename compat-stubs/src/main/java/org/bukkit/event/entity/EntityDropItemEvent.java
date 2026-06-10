package org.bukkit.event.entity;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * Compile-only stub for {@code org.bukkit.event.entity.EntityDropItemEvent}. Not shaded; on modern
 * servers the real concrete class is used at runtime. Stubbed as a class (not an interface) so virtual
 * calls resolve via invokevirtual, matching the real type.
 */
public class EntityDropItemEvent extends EntityEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    public EntityDropItemEvent(Entity entity, Item drop) {
        super(entity);
    }

    public Item getItemDrop() {
        return null;
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
