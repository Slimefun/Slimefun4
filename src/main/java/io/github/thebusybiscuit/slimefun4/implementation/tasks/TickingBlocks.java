package io.github.thebusybiscuit.slimefun4.implementation.tasks;

import io.github.thebusybiscuit.cscorelib2.blocks.BlockPosition;
import org.bukkit.Location;
import org.bukkit.World;

import javax.annotation.Nonnull;
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
final class TickingBlocks {

    private final Map<World, Set<Long>> blocks = Collections.synchronizedMap(new WeakHashMap<>());

    /**
     * Get the compressed {@link BlockPosition BlockPositions} mapped to a given {@link World}.
     * The returned {@link Set} is thread-safe. Changes to set will be reflected in this instance.
     *
     * @param world The world
     * @return Returns a {@link Set} of {@link Long Longs} which are compressed {@link BlockPosition BlockPositions}
     *
     * @see #getBlocksCopy()
     */
    public @Nonnull Set<Long> getBlocks(@Nonnull World world) {
        return blocks.computeIfAbsent(world, (unused) -> Collections.synchronizedSet(new HashSet<>()));
    }

    /**
     * Get a copy of the blocks mapped to a given {@link World} in the form of {@link BlockPosition BlockPositions}
     * The returned {@link Set} is guaranteed to be a copy; however, it is not guaranteed to be thread-safe.
     *
     * @param world The world
     * @return Returns a {@link Set} of {@link BlockPosition BlockPositions}
     * @see #getBlocksCopy()
     */
    public @Nonnull Set<BlockPosition> getBlocksCopy(@Nonnull World world) {
        Collection<Long> compressed = blocks.get(world);
        if (compressed == null) {
            return Collections.emptySet();
        }
        return convertToBlockPosition(world, compressed);
    }

    /**
     * Get a copy of all the blocks stored in the form of {@link BlockPosition BlockPositions}
     * The returned {@link Set} is not guaranteed to be thread-safe.
     *
     * @return Returns a {@link Set} of {@link BlockPosition BlockPositions}
     * @see #getBlocksCopy(World)
     */
    public @Nonnull Set<BlockPosition> getBlocksCopy() {
        Set<BlockPosition> positions = new HashSet<>();
        for (Map.Entry<World, Set<Long>> entry : blocks.entrySet()) {
            World world = entry.getKey();
            Set<BlockPosition> converted = convertToBlockPosition(world, entry.getValue());
            positions.addAll(converted);
        }
        return positions;
    }

    /**
     * Get a copy of all the blocks in a given {@link World} in the form of {@link BlockPosition BlockPositions}
     * The returned {@link Set} is not guaranteed to be thread-safe.
     *
     * @return Returns a {@link Set} of {@link BlockPosition BlockPositions}
     * @see #getBlocksAsLocations()
     */
    public @Nonnull Set<Location> getBlocksAsLocations(@Nonnull World world) {
        Collection<Long> compressed = blocks.get(world);
        if (compressed == null) {
            return Collections.emptySet();
        }
        return convertToLocation(world, compressed);
    }

    /**
     * Get a copy of all the blocks stored in the form of {@link BlockPosition BlockPositions}
     * The returned {@link Set} is not guaranteed to be thread-safe.
     *
     * @return Returns a {@link Set} of {@link BlockPosition BlockPositions}
     * @see #getBlocksAsLocations(World)
     */
    public @Nonnull Set<Location> getBlocksAsLocations() {
        Set<Location> locations = new HashSet<>();
        for (Map.Entry<World, Set<Long>> entry : blocks.entrySet()) {
            World world = entry.getKey();
            Set<Location> converted = convertToLocation(world, entry.getValue());
            locations.addAll(converted);
        }
        return locations;
    }

    public void addBlock(@Nonnull BlockPosition block) {
        getBlocks(block.getWorld()).add(block.getPosition());
    }

    public void addBlock(@Nonnull Location location) {
        addBlock(new BlockPosition(location));
    }

    public void removeBlock(@Nonnull BlockPosition block) {
        World world = block.getWorld();
        Collection<Long> positions = blocks.get(world);
        if (positions == null) {
            return;
        }
        positions.remove(block.getPosition());
        if (positions.isEmpty()) {
            blocks.remove(world);
        }
    }

    public void removeBlock(@Nonnull Location location) {
        removeBlock(new BlockPosition(location));
    }

    /**
     * Clear all the blocks for a given {@link World}
     * @param key The world to clear the key for
     */
    public void clear(@Nonnull World key) {
        blocks.remove(key);
    }

    /**
     * Clear all entries from this instance
     */
    public void clear() {
        blocks.clear();
    }

    private static @Nonnull Set<BlockPosition> convertToBlockPosition(@Nonnull World world, @Nonnull Collection<Long> raw) {
        if (raw.isEmpty()) {
            return Collections.emptySet();
        }
        Set<BlockPosition> positions = new HashSet<>(raw.size());
        for (long compressed : raw) {
            positions.add(new BlockPosition(world, compressed));
        }
        return positions;
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
