package io.github.thebusybiscuit.slimefun4.implementation.tasks;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import io.github.thebusybiscuit.cscorelib2.blocks.BlockPosition;
import org.bukkit.Location;
import org.bukkit.World;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

/**
 * Memory efficient, synchronized collection of {@link BlockPosition BlockPositions} used by the
 * {@link TickerTask}. All methods are thread-safe unless explicitly stated otherwise.
 *
 * @author md5sha256
 */
@ThreadSafe
final class TickerCollection {

    private final Cache<World, Set<Long>> blocks = CacheBuilder.newBuilder().weakKeys().build();

    /**
     * Get the compressed {@link BlockPosition BlockPositions} mapped to a given {@link World}.
     * The returned {@link Set} is thread-safe. Changes to set will be reflected in this instance.
     *
     * @param world The world
     * @return Returns a {@link Set} of {@link Long Longs} which are compressed {@link BlockPosition BlockPositions}
     *
     */
    public @Nonnull Set<Long> getBlocks(@Nonnull World world) {
        return blocks.asMap().computeIfAbsent(world, unused -> Collections.synchronizedSet(new HashSet<>()));
    }

    /**
     * Get a copy of all the blocks stored in the form of {@link BlockPosition BlockPositions}
     * The returned {@link Set} is not guaranteed to be thread-safe.
     *
     * @return Returns a {@link Set} of {@link BlockPosition BlockPositions}
     */
    public @Nonnull Set<Location> getBlocksAsLocations() {
        Set<Location> locations = new HashSet<>();
        for (Map.Entry<World, Set<Long>> entry : blocks.asMap().entrySet()) {
            World world = entry.getKey();
            Set<Location> converted = convertToLocation(world, entry.getValue());
            locations.addAll(converted);
        }
        return locations;
    }

    /**
     * Add a location to this ticker collection, duplicates will be ignored.
     *
     * @param block The block to add
     * @see #addBlock(Location)
     */
    public void addBlock(@Nonnull BlockPosition block) {
        getBlocks(block.getWorld()).add(block.getPosition());
    }

    /**
     * Add a location to this ticker collection, duplicates will be ignored.
     *
     * @param location The location of the block to add
     * @see #addBlock(BlockPosition)
     */
    public void addBlock(@Nonnull Location location) {
        addBlock(new BlockPosition(location));
    }

    /**
     * Remove a block from this ticker collection
     *
     * @param block The block to remove
     * @see #removeBlock(Location)
     */
    public void removeBlock(@Nonnull BlockPosition block) {
        World world = block.getWorld();
        Collection<Long> positions = blocks.getIfPresent(world);
        if (positions == null) {
            return;
        }
        positions.remove(block.getPosition());
        if (positions.isEmpty()) {
            blocks.invalidate(world);
        }
    }

    /**
     * Remove a location from this ticker collection.
     *
     * @param location The location of the block to remove
     * @see #removeBlock(BlockPosition)
     */
    public void removeBlock(@Nonnull Location location) {
        removeBlock(new BlockPosition(location));
    }

    /**
     * Clear all the blocks for a given {@link World}
     * @param key The world to clear the key for
     */
    public void clear(@Nonnull World key) {
        blocks.invalidate(key);
    }

    /**
     * Clear all entries from this instance
     */
    public void clear() {
        blocks.invalidateAll();
    }

    private static @Nonnull Set<Location> convertToLocation(@Nonnull World world, @Nonnull Collection<Long> raw) {
        Set<Location> locations = new HashSet<>(raw.size());
        for (long compressed : raw) {
            int x = (int) (compressed >> 38);
            int y = (int) (compressed & 0XFFF);
            int z = (int) (compressed << 26 >> 38);
            locations.add(new Location(world, x, y, z));
        }
        return locations;
    }

}
