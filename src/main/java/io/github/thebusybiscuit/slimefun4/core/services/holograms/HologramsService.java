package io.github.thebusybiscuit.slimefun4.core.services.holograms;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Consumer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.Server;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import io.github.thebusybiscuit.cscorelib2.blocks.BlockPosition;
import io.github.thebusybiscuit.slimefun4.core.attributes.HologramOwner;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;

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
     * The radius in which we scan for holograms
     */
    private static final double RADIUS = 0.45;

    /**
     * The frequency at which to purge.
     * Every 45 seconds.
     */
    private static final long PURGE_RATE = 45L * 20L;

    /**
     * Our {@link Plugin} instance
     */
    private final Plugin plugin;

    /**
     * The {@link NamespacedKey} used to store data on a hologram
     */
    private final NamespacedKey persistentDataKey;

    /**
     * Our cache to save {@link Entity} lookups
     */
    private final Map<BlockPosition, CachedArmorStand> cache = new HashMap<>();

    /**
     * This constructs a new {@link HologramsService}.
     * 
     * @param plugin
     *            Our {@link Plugin} instance
     */
    public HologramsService(@Nonnull Plugin plugin) {
        this.plugin = plugin;

        // Null-Validation is performed in the NamespacedKey constructor
        persistentDataKey = new NamespacedKey(plugin, "hologram_id");
    }

    /**
     * This will start the {@link HologramsService} and schedule a repeating
     * purge-task.
     */
    public void start() {
        plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, this::purge, PURGE_RATE, PURGE_RATE);
    }

    /**
     * This purges all expired {@link CachedArmorStand CachedArmorStands}.
     */
    private void purge() {
        Iterator<CachedArmorStand> iterator = cache.values().iterator();

        while (iterator.hasNext()) {
            if (iterator.next().isExpired()) {
                iterator.remove();
            }
        }
    }

    /**
     * This returns the hologram associated with the given {@link Location}.
     * If createIfNoneExists is set to true a new {@link ArmorStand} will be spawned
     * if no existing one could be found.
     * 
     * @param loc
     *            The {@link Location}
     * @param createIfNoneExists
     *            Whether to create a new {@link ArmorStand} if none was found
     * 
     * @return The existing (or newly created) hologram
     */
    @Nullable
    public ArmorStand getHologram(@Nonnull Location loc, boolean createIfNoneExists) {
        Validate.notNull(loc, "Location cannot be null");

        if (!Bukkit.isPrimaryThread()) {
            throw new UnsupportedOperationException("A hologram cannot be accessed asynchronously.");
        }

        BlockPosition position = new BlockPosition(loc);
        CachedArmorStand cachedEntity = cache.get(position);

        // Check if the ArmorStand was cached
        if (cachedEntity != null) {
            ArmorStand armorstand = cachedEntity.getArmorStand();

            // If the Entity still exists, return it
            if (armorstand != null) {
                return armorstand;
            }
        }

        // Scan all nearby entities which could be possible holograms
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

    /**
     * This removes the hologram at that given {@link Location}.
     * <p>
     * <strong>This method must be executed on the main {@link Server} {@link Thread}.</strong>
     * 
     * @param loc
     *            The {@link Location}
     * 
     * @return Whether the hologram could be removed, false if the hologram does not exist or was already removed
     */
    public boolean removeHologram(@Nonnull Location loc) {
        Validate.notNull(loc, "Location cannot be null");

        if (Bukkit.isPrimaryThread()) {
            ArmorStand hologram = getHologram(loc, false);

            if (hologram != null) {
                hologram.remove();
                return true;
            } else {
                return false;
            }
        } else {
            throw new UnsupportedOperationException("You cannot remove a hologram asynchronously.");
        }
    }

    /**
     * This updates the hologram.
     * You can use it to set the nametag or other properties.
     * <p>
     * <strong>This method must be executed on the main {@link Server} {@link Thread}.</strong>
     * 
     * @param loc
     *            The {@link Location}
     * @param consumer
     *            The callback to run
     */
    public void updateHologram(@Nonnull Location loc, @Nonnull Consumer<ArmorStand> consumer) {
        Validate.notNull(loc, "Location must not be null");
        Validate.notNull(consumer, "Callbacks must not be null");

        if (Bukkit.isPrimaryThread()) {
            consumer.accept(getHologram(loc, true));
        } else {
            SlimefunPlugin.runSync(() -> consumer.accept(getHologram(loc, true)));
        }
    }

    /**
     * This checks if a given {@link Entity} is an {@link ArmorStand}
     * and whether it has the correct attributes to be considered a hologram.
     * 
     * @param n
     *            The {@link Entity} to check
     * 
     * @return Whether this could be a hologram
     */
    private boolean isHologram(@Nonnull Entity n) {
        if (n instanceof ArmorStand) {
            ArmorStand armorstand = (ArmorStand) n;
            return armorstand.isValid() && armorstand.isSilent() && armorstand.isMarker() && !armorstand.hasAI() && !armorstand.hasGravity();
        } else {
            return false;
        }
    }

}
