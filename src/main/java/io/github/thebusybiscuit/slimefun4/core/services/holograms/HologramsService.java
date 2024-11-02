package io.github.thebusybiscuit.slimefun4.core.services.holograms;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.logging.Level;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import io.github.bakedlibs.dough.data.persistent.PersistentDataAPI;
import io.github.thebusybiscuit.slimefun4.api.MinecraftVersion;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.Server;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.TextDisplay;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

import io.github.bakedlibs.dough.blocks.BlockPosition;
import io.github.thebusybiscuit.slimefun4.core.attributes.HologramOwner;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;

/**
 * This service is responsible for handling holograms.
 * 
 * @author TheBusyBiscuit, JustAHuman
 *
 * @see Hologram
 * @see HologramOwner
 */
public class HologramsService {
    private static final double SCAN_RADIUS = 0.45;
    private static final long PURGE_RATE = 45L * 20L;
    private static final Vector DEFAULT_OFFSET = new Vector(0.5, 0.75, 0.5);

    private final Plugin plugin;
    private final NamespacedKey key;
    private final Map<BlockPosition, Hologram<? extends Entity>> cache = new HashMap<>();
    protected boolean started = false;

    public HologramsService(@Nonnull Plugin plugin) {
        // Null-Validation is performed in the NamespacedKey constructor
        this.plugin = plugin;
        this.key = new NamespacedKey(plugin, "hologram_id");
    }

    /**
     * This will start the {@link HologramsService} and schedule a repeating
     * purge-task.
     */
    public void start() {
        if (started) {
            // TODO: Log a warning/what do reviewers think?
            return;
        }

        plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, this::purge, PURGE_RATE, PURGE_RATE);
    }

    /**
     * @return The default {@link Hologram} offset
     */
    public @Nonnull Vector getDefaultOffset() {
        return DEFAULT_OFFSET.clone();
    }

    /**
     * @return The {@link NamespacedKey} marking an entity as a hologram & storing its position
     */
    public @Nonnull NamespacedKey getKey() {
        return key;
    }

    /**
     * This purges any expired {@link Hologram}.
     */
    private void purge() {
        cache.values().removeIf(Hologram::hasExpired);
    }

    /**
     * This returns the {@link Hologram} associated with the given {@link Location}.
     * If createIfNoneExists is set to true a new {@link Hologram} will be spawned
     * if no existing one could be found.
     * 
     * @param loc
     *            The {@link Location}
     * @param createIfNoneExists
     *            Whether to create a new {@link Hologram} if none was found
     * 
     * @return The existing (or newly created) hologram
     */
    @Nullable
    private Hologram<?> getHologram(@Nonnull Location loc, boolean createIfNoneExists) {
        Validate.notNull(loc, "Location cannot be null");
        Validate.notNull(loc.getWorld(), "The Location's World cannot be null");

        BlockPosition position = new BlockPosition(loc);
        Hologram<?> hologram = cache.get(position);

        // Check if the Hologram was cached and still exists
        if (hologram != null && !hologram.hasDespawned()) {
            return hologram;
        }

        // Scan all nearby entities which could be possible holograms
        Collection<Entity> holograms = loc.getWorld().getNearbyEntities(loc, SCAN_RADIUS, SCAN_RADIUS, SCAN_RADIUS, entity -> isHologram(entity, position));
        for (Entity entity : holograms) {
            if (hologram == null) {
                hologram = getAsHologram(entity, position);
            } else {
                // Fixes #2927 - Remove any duplicates we find
                entity.remove();
            }
        }

        if (hologram == null && createIfNoneExists) {
            if (Slimefun.getMinecraftVersion().isAtLeast(MinecraftVersion.MINECRAFT_1_19_4)) {
                return TextDisplayHologram.create(loc, position);
            }
            return ArmorStandHologram.create(loc, position);
        } else {
            return hologram;
        }
    }

    @ParametersAreNonnullByDefault
    private boolean hasHologramData(Entity entity, BlockPosition position) {
        return PersistentDataAPI.getLong(entity, key) == position.getPosition();
    }

    /**
     * This checks if a given {@link Entity} is an {@link TextDisplay} or {@link ArmorStand}
     * and whether it has the correct attributes to be considered a {@link Hologram}.
     * 
     * @param entity
     *            The {@link Entity} to check
     * 
     * @return Whether this could be a hologram
     */
    private boolean isHologram(@Nonnull Entity entity, BlockPosition position) {
        if (entity instanceof ArmorStand armorStand) {
            // The absolute minimum requirements to count as a hologram
            return !armorStand.isVisible()
                    && armorStand.isSilent()
                    && !armorStand.hasGravity()
                    && hasHologramData(armorStand, position);
        } else if (entity instanceof TextDisplay textDisplay) {
            return hasHologramData(textDisplay, position);
        }
        return false;
    }

    /**
     * This will cast and find the matching {@link Hologram} for the given {@link Entity}.
     *
     * @param entity
     *      *            The {@link Entity}
     * @param position
     *            The {@link BlockPosition} of this hologram
     * 
     * @return The {@link Hologram}
     */
    @ParametersAreNonnullByDefault
    private @Nullable Hologram<?> getAsHologram(Entity entity, BlockPosition position) {
        Hologram<?> hologram = TextDisplayHologram.of(entity, position);
        if (hologram == null) {
            hologram = ArmorStandHologram.of(entity, position);
        }
        return hologram;
    }

    /**
     * This updates the {@link Hologram}.
     * You can use it to set the nametag or other properties.
     * <p>
     * <strong>This method must be executed on the main {@link Server} {@link Thread}.</strong>
     * 
     * @param loc
     *            The {@link Location}
     * @param consumer
     *            The callback to run
     */
    private void updateHologram(@Nonnull Location loc, @Nonnull Consumer<Hologram<?>> consumer) {
        Validate.notNull(loc, "Location must not be null");
        Validate.notNull(consumer, "Callbacks must not be null");
        if (!Bukkit.isPrimaryThread()) {
            Slimefun.runSync(() -> updateHologram(loc, consumer));
            return;
        }

        try {
            Hologram<?> hologram = getHologram(loc, true);
            if (hologram != null) {
                consumer.accept(hologram);
            }
        } catch (Exception | LinkageError x) {
            Slimefun.logger().log(Level.SEVERE, "Hologram located at {0}", new BlockPosition(loc));
            Slimefun.logger().log(Level.SEVERE, "Something went wrong while trying to update this hologram", x);
        }
    }

    /**
     * This removes the {@link Hologram} at that given {@link Location}.
     * <p>
     * <strong>This method must be executed on the main {@link Server} {@link Thread}.</strong>
     * 
     * @param location
     *            The {@link Location}
     * 
     * @return Whether the {@link Hologram} could be removed, false if the {@link Hologram} does not
     *         exist or was already removed
     */
    public boolean removeHologram(@Nonnull Location location) {
        Validate.notNull(location, "Location cannot be null");
        if (!Bukkit.isPrimaryThread()) {
            throw new UnsupportedOperationException("You cannot remove a hologram asynchronously.");
        }

        try {
            Hologram<?> hologram = getHologram(location, false);
            if (hologram == null) {
                return false;
            }

            cache.remove(new BlockPosition(location));
            hologram.remove();
            return true;
        } catch (Exception | LinkageError x) {
            Slimefun.logger().log(Level.SEVERE, "Hologram located at {0}", new BlockPosition(location));
            Slimefun.logger().log(Level.SEVERE, "Something went wrong while trying to remove this hologram", x);
            return false;
        }
    }

    /**
     * This will update the text of the {@link Hologram}.
     * 
     * @param location
     *            The {@link Location} of this {@link Hologram}
     * @param text
     *            The text to set, can be null
     */
    public void setHologramLabel(@Nonnull Location location, @Nullable String text) {
        Validate.notNull(location, "Location must not be null");

        updateHologram(location, hologram -> hologram.setText(text));
    }

    public void teleportHologram(@Nonnull Location location, @Nonnull Location to) {
        Validate.notNull(location, "Location must not be null");

        updateHologram(location, hologram -> hologram.teleport(to));
    }

}
