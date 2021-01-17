package io.github.thebusybiscuit.slimefun4.core.services.holograms;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;

/**
 * This represents an {@link ArmorStand} that can expire and be renamed.
 * 
 * @author TheBusyBiscuit
 *
 */
class Hologram {

    /**
     * This is the minimum duration after which the {@link Hologram} will expire.
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
     * The label of this {@link Hologram}.
     */
    private String label;

    /**
     * This creates a new {@link Hologram} for the given {@link UUID}.
     * 
     * @param uniqueId
     *            The {@link UUID} of the corresponding {@link ArmorStand}
     */
    Hologram(@Nonnull UUID uniqueId) {
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
    ArmorStand getArmorStand() {
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
     * This checks if the associated {@link ArmorStand} has despawned.
     * 
     * @return Whether the {@link ArmorStand} despawned
     */
    boolean hasDespawned() {
        return getArmorStand() == null;
    }

    /**
     * This returns whether this {@link Hologram} has expired.
     * The armorstand will expire if the last access has been more than 10
     * minutes ago.
     * 
     * @return Whether this {@link Hologram} has expired
     */
    boolean hasExpired() {
        return System.currentTimeMillis() - lastAccess > EXPIRES_AFTER;
    }

    /**
     * This method sets the label of this {@link Hologram}.
     * 
     * @param label
     *            The label to set
     */
    void setLabel(@Nullable String label) {
        if (Objects.equals(this.label, label)) {
            /*
             * Label is already set, no need to cause an entity
             * update. But we can update the lastAccess flag.
             */
            this.lastAccess = System.currentTimeMillis();
        } else {
            this.label = label;
            ArmorStand entity = getArmorStand();

            if (entity != null) {
                if (label != null) {
                    entity.setCustomNameVisible(true);
                    entity.setCustomName(label);
                } else {
                    entity.setCustomNameVisible(false);
                    entity.setCustomName(null);
                }
            }
        }
    }

    /**
     * This will remove the {@link ArmorStand} and expire this {@link Hologram}.
     */
    void remove() {
        ArmorStand armorstand = getArmorStand();

        if (armorstand != null) {
            lastAccess = 0;
            armorstand.remove();
        }
    }

}
