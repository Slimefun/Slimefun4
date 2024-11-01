package io.github.thebusybiscuit.slimefun4.core.services.holograms;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;

/**
 * A {@link Hologram} is a wrapper around an {@link Entity}
 * for displaying text as "holograms".
 * 
 * @author TheBusyBiscuit, JustAHuman
 */
public abstract class Hologram<E extends Entity> {
    private static final long EXPIRES_AFTER = TimeUnit.MINUTES.toMillis(10);

    protected final UUID uniqueId;
    protected long lastAccess;
    protected String text;

    /**
     * This creates a new {@link Hologram} for the given {@link UUID}.
     * 
     * @param uniqueId
     *            The {@link UUID} of the corresponding {@link Entity}
     */
    protected Hologram(@Nonnull UUID uniqueId) {
        this.uniqueId = uniqueId;
        this.lastAccess = System.currentTimeMillis();
    }

    public abstract void setText(@Nullable String text);
    public abstract Class<E> getEntityType();

    /**
     * @return Whether the associated {@link Entity} has despawned.
     */
    public boolean hasDespawned() {
        return getEntity() == null;
    }

    /**
     * This returns whether this {@link Hologram} has expired.
     * The {@link Hologram} has expired if the last access was
     * more than 10 minutes ago.
     *
     * @return Whether this {@link Hologram} has expired
     */
    public boolean hasExpired() {
        return System.currentTimeMillis() - lastAccess > EXPIRES_AFTER;
    }

    /**
     * This returns the corresponding {@link Entity}
     * and also updates the "lastAccess" timestamp.
     * <p>
     * If the entity was removed, it will return null.
     * 
     * @return The {@link Entity} or null.
     */
    @Nullable
    public E getEntity() {
        Entity entity = Bukkit.getEntity(uniqueId);
        if (getEntityType().isInstance(entity) && entity.isValid()) {
            this.lastAccess = System.currentTimeMillis();
            return getEntityType().cast(entity);
        } else {
            this.lastAccess = 0;
            return null;
        }
    }

    /**
     * This will remove the {@link Entity} and expire this {@link Hologram}.
     */
    public void remove() {
        Entity entity = getEntity();
        if (entity != null) {
            lastAccess = 0;
            entity.remove();
        }
    }

}
