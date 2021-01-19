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
    SlimefunWorldData(@Nonnull World world) {
        this.world = new WeakReference<>(world);
    }

    @Nullable
    public World getWorldIfLoaded() {
        return world.get();
    }

    @Nullable
    public SlimefunBlockData getBlockData(int x, int y, int z) {
        blocksLock.readLock().lock();

        try {
            return null;
            // return blocks.get(BlockPosition.getAsLong(x, y, z));
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

}
