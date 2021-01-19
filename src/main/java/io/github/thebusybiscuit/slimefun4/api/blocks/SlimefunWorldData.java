package io.github.thebusybiscuit.slimefun4.api.blocks;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.lang.Validate;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

import io.github.thebusybiscuit.cscorelib2.blocks.BlockPosition;
import io.github.thebusybiscuit.cscorelib2.blocks.ChunkPosition;
import io.github.thebusybiscuit.slimefun4.api.blocks.sources.BlockDataSource;

/**
 * 
 * @author TheBusyBiscuit
 * 
 * @see BlockDatabase
 * @see SlimefunBlockData
 *
 */
public class SlimefunWorldData {

    /**
     * This {@link ReadWriteLock} protects our {@link HashMap} from concurrent
     * modifications.
     */
    private final ReadWriteLock blocksLock = new ReentrantReadWriteLock();

    /**
     * This is our internal data structure: We store the {@link SlimefunBlockData}
     * in this {@link Map} and use the {@link BlockPosition}'s {@link Long} as the key.
     */
    private final Map<Long, SlimefunBlockData> blocks = new HashMap<>();

    /**
     * This {@link ReadWriteLock} protects our {@link HashMap} from concurrent
     * modifications.
     */
    private final ReadWriteLock chunksLock = new ReentrantReadWriteLock();

    /**
     * This is our internal data structure: We store the {@link SlimefunChunkData}
     * in this {@link Map} and use the {@link ChunkPosition}'s {@link Long} as the key.
     */
    private final Map<Long, SlimefunChunkData> chunks = new HashMap<>();

    /**
     * This is our singleton instance for {@link EmptyBlockData}.
     * It will always return null and cannot hold any values.
     */
    private final EmptyBlockData emptyBlockData = new EmptyBlockData();

    /**
     * The name of the {@link World}.
     */
    private final String name;

    /**
     * We keep a {@link WeakReference} of our {@link World} to prevent
     * memory leaks. Once the {@link World} is unloaded, the reference
     * will be nullified.
     * This signals that we are ready to abandon this {@link SlimefunWorldData}
     * instance.
     */
    private final WeakReference<World> world;

    /**
     * This creates a new {@link SlimefunWorldData} object for the given {@link World}.
     * 
     * @param world
     *            The {@link World}
     */
    SlimefunWorldData(@Nonnull World world, @Nonnull BlockDataSource source) {
        this.world = new WeakReference<>(world);
        this.name = world.getName();

        loadWorldData(source);
    }

    private void loadWorldData(@Nonnull BlockDataSource source) {
        blocksLock.writeLock().lock();

        try {
            source.loadBlocks(this, blocks);
        } finally {
            blocksLock.writeLock().unlock();
        }

        chunksLock.writeLock().lock();

        try {
            source.loadChunks(this, chunks);
        } finally {
            chunksLock.writeLock().unlock();
        }
    }

    @Nonnull
    public String getName() {
        return name;
    }

    @Nullable
    public World getWorldIfLoaded() {
        return world.get();
    }

    @Nullable
    public SlimefunBlockData getBlockData(int x, int y, int z) {
        blocksLock.readLock().lock();

        try {
            return blocks.get(BlockPosition.getAsLong(x, y, z));
        } finally {
            blocksLock.readLock().unlock();
        }
    }

    @Nullable
    public SlimefunBlockData getBlockData(@Nonnull Location loc) {
        Validate.notNull(loc, "The Location must not be null");

        return getBlockData(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
    }

    @Nullable
    public SlimefunBlockData getBlockData(@Nonnull Block b) {
        Validate.notNull(b, "The Block must not be null");

        return getBlockData(b.getX(), b.getY(), b.getZ());
    }

    @Nullable
    public SlimefunChunkData getChunkData(int x, int z) {
        chunksLock.readLock().lock();

        try {
            return chunks.get(ChunkPosition.getAsLong(x, z));
        } finally {
            chunksLock.readLock().unlock();
        }
    }

}
