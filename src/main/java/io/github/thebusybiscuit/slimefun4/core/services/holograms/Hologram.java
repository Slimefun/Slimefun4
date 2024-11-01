package io.github.thebusybiscuit.slimefun4.core.services.holograms;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.TextDisplay;

/**
 * A {@link TextDisplay} or {@link ArmorStand} (if pre1.19.4) used
 * for displaying text "holograms".
 * 
 * @author TheBusyBiscuit, JustAHuman
 */
public class Hologram {
    private static final long EXPIRES_AFTER = TimeUnit.MINUTES.toMillis(10);

    private final UUID uniqueId;
    private long lastAccess;
    private String text;

    /**
     * This creates a new {@link Hologram} for the given {@link UUID}.
     * 
     * @param uniqueId
     *            The {@link UUID} of the corresponding {@link TextDisplay} or {@link ArmorStand}
     */
    Hologram(@Nonnull UUID uniqueId) {
        this.uniqueId = uniqueId;
        this.lastAccess = System.currentTimeMillis();
    }

    /**
     * This returns the corresponding {@link TextDisplay} or {@link ArmorStand}
     * and also updates the "lastAccess" timestamp.
     * <p>
     * If the entity was removed, it will return null.
     * 
     * @return The {@link Entity} or null.
     */
    @Nullable
    public Entity getEntity() {
        Entity entity = Bukkit.getEntity(uniqueId);
        if ((entity instanceof TextDisplay || entity instanceof ArmorStand) && entity.isValid()) {
            this.lastAccess = System.currentTimeMillis();
            return entity;
        } else {
            this.lastAccess = 0;
            return null;
        }
    }

    /**
     * @return Whether the associated {@link Entity} despawned.
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
     * This method sets the label of this {@link Hologram}.
     * 
     * @param text
     *            The label to set
     */
    void setText(@Nullable String text) {
        if (Objects.equals(this.text, text)) {
            /*
             * Label is already set, no need to cause an entity
             * update. But we can update the lastAccess flag.
             */
            this.lastAccess = System.currentTimeMillis();
            return;
        }

        this.text = text;
        Entity entity = getEntity();
        if (entity instanceof ArmorStand armorStand) {
            armorStand.setCustomNameVisible(text != null);
            armorStand.setCustomName(text);
        } else if (entity instanceof TextDisplay textDisplay) {
            textDisplay.setText(text);
        }
    }

    /**
     * This will remove the {@link Entity} and expire this {@link Hologram}.
     */
    void remove() {
        Entity entity = getEntity();
        if (entity != null) {
            lastAccess = 0;
            entity.remove();
        }
    }

}
