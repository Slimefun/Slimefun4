package io.github.thebusybiscuit.slimefun4.core.services.holograms;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;

/**
 * This represents an {@link ArmorStand} or hologram that can expire.
 * 
 * @author TheBusyBiscuit
 *
 */
class CachedArmorStand {

    /**
     * This is the minimum duration after which the {@link CachedArmorStand} will expire.
     */
    private static final long EXPIRES_AFTER = TimeUnit.MINUTES.toMillis(10);

    /**
     * The {@link UUID} of the {@link ArmorStand}.
     */
    private final UUID uniqueId;

    /**
     * The timestamp of when the {@link ArmorStand} was last accessed.
     */
    private long lastAccess;

    /**
     * This creates a new {@link CachedArmorStand} for the given {@link UUID}.
     * 
     * @param uniqueId
     *            The {@link UUID} of the corresponding {@link ArmorStand}
     */
    public CachedArmorStand(@Nonnull UUID uniqueId) {
        this.uniqueId = uniqueId;
        this.lastAccess = System.currentTimeMillis();
    }

    /**
     * This returns the corresponding {@link ArmorStand}
     * and also updates the "lastAccess" timestamp.
     * <p>
     * If the {@link ArmorStand} was removed, it will return null.
     * 
     * @return The {@link ArmorStand} or null.
     */
    @Nullable
    public ArmorStand getArmorStand() {
        Entity n = Bukkit.getEntity(uniqueId);

        if (n instanceof ArmorStand && n.isValid()) {
            this.lastAccess = System.currentTimeMillis();
            return (ArmorStand) n;
        } else {
            this.lastAccess = 0;
            return null;
        }
    }

    /**
     * This returns whether this {@link CachedArmorStand} has expired.
     * The armorstand will expire if the last access has been more than 10
     * minutes ago.
     * 
     * @return Whether this {@link CachedArmorStand} has expired
     */
    public boolean isExpired() {
        return System.currentTimeMillis() - lastAccess > EXPIRES_AFTER;
    }

}
