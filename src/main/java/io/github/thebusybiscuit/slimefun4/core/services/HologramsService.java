package io.github.thebusybiscuit.slimefun4.core.services;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import io.github.thebusybiscuit.cscorelib2.blocks.BlockPosition;
import io.github.thebusybiscuit.slimefun4.core.attributes.HologramOwner;

/**
 * This service is responsible for handling holograms.
 * This includes error management when something goes wrong.
 * 
 * @author TheBusyBiscuit
 *
 * @see HologramOwner
 */
public class HologramsService {

    /**
     * The radius in which we scan for holograms.
     */
    private static final double RADIUS = 0.45;

    /**
     * The {@link NamespacedKey} used to store data on a hologram
     */
    private final NamespacedKey persistentDataKey;

    /**
     * Our cache to save {@link Entity} lookups
     */
    private final Map<BlockPosition, UUID> entityCache = new HashMap<>();

    public HologramsService(@Nonnull Plugin plugin) {
        // Null-Validation is performed in the NamespacedKey constructor
        persistentDataKey = new NamespacedKey(plugin, "hologram_id");
    }

    @Nullable
    public ArmorStand getHologram(@Nonnull Location loc, boolean createIfNoneExists) {
        Validate.notNull(loc, "Location cannot be null");

        BlockPosition position = new BlockPosition(loc);
        UUID uuid = entityCache.get(position);

        if (uuid != null) {
            Entity entity = Bukkit.getEntity(uuid);

            if (entity instanceof ArmorStand) {
                return (ArmorStand) entity;
            }
        }

        Collection<Entity> holograms = loc.getWorld().getNearbyEntities(loc, RADIUS, RADIUS, RADIUS, this::isHologram);

        for (Entity n : holograms) {
            if (n instanceof ArmorStand) {
                PersistentDataContainer container = n.getPersistentDataContainer();

                if (container.has(persistentDataKey, PersistentDataType.LONG)) {
                    // Check if it is ours or a different one.
                    if (container.get(persistentDataKey, PersistentDataType.LONG).equals(position.getPosition())) {
                        return (ArmorStand) n;
                    }
                } else {
                    // Set a persistent tag to re-identify the correct hologram later
                    container.set(persistentDataKey, PersistentDataType.LONG, position.getPosition());
                    return (ArmorStand) n;
                }
            }
        }

        if (createIfNoneExists) {
            // Spawn a new ArmorStand
            ArmorStand hologram = (ArmorStand) loc.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);

            // Set a persistent tag to re-identify the correct hologram later
            PersistentDataContainer container = hologram.getPersistentDataContainer();
            container.set(persistentDataKey, PersistentDataType.LONG, position.getPosition());

            hologram.setSilent(true);
            hologram.setMarker(true);
            hologram.setAI(false);
            hologram.setGravity(false);

            return hologram;
        } else {
            return null;
        }
    }

    public boolean removeHologram(@Nonnull Location loc) {
        Validate.notNull(loc, "Location cannot be null");

        ArmorStand hologram = getHologram(loc, false);

        if (hologram != null) {
            hologram.remove();
            return true;
        } else {
            return false;
        }
    }

    private boolean isHologram(@Nonnull Entity n) {
        if (n instanceof ArmorStand) {
            ArmorStand armorstand = (ArmorStand) n;
            return armorstand.isValid() && armorstand.isSilent() && armorstand.isMarker() && !armorstand.hasAI() && !armorstand.hasGravity();
        } else {
            return false;
        }
    }

    public void updateHologram(@Nonnull Location loc, @Nonnull Consumer<ArmorStand> consumer) {
        Validate.notNull(loc, "Location cannot be null");
        Validate.notNull(consumer, "Callbacks must not be null");

        consumer.accept(getHologram(loc, true));
    }

}
