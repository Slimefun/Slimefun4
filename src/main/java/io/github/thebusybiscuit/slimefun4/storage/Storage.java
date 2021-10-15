package io.github.thebusybiscuit.slimefun4.storage;

import io.github.bakedlibs.dough.blocks.BlockPosition;
import io.github.bakedlibs.dough.blocks.ChunkPosition;
import io.github.thebusybiscuit.slimefun4.storage.implementation.binary.RegionFile;
import org.bukkit.Chunk;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Storage contains the getters and setters for chunk and block data. Through this, loading and writing to the
 * {@link RegionFile} is achieved.
 *
 *
 */
public abstract class Storage {

    private final Map<ChunkPosition, DataObject> chunkData = new ConcurrentHashMap<>();
    ////////////////////////////////////////
    // Getters
    ////////////////////////////////////////
    /**
     * Get the {@link DataObject} linked to this {@link Chunk}, if the chunk doesn't have one it returns null.
     *
     * @param chunk The chunk of the data to retrieve
     * @return The {@link DataObject} associated with this {@link Chunk} or null if it doesn't exist
     */
    @Nullable
    public DataObject getChunk(Chunk chunk) {
        return chunkData.get(new ChunkPosition(chunk));
    }

    /**
     * Get the {@link DataObject} linked to this {@link ChunkPosition}, if the chunk doesn't have one it returns null.
     *
     * @param chunkPosition The chunk of the data to retrieve
     * @return The {@link DataObject} associated with this {@link ChunkPosition} or null if it doesn't exist
     */
    @Nullable
    public DataObject getChunk(ChunkPosition chunkPosition) {
        return chunkData.get(chunkPosition);
    }

    /**
     * Get the {@link DataObject} linked to this {@link BlockPosition}, if the chunk doesn't have one it returns null.
     *
     * @param blockPosition The chunk of the data to retrieve
     * @return The {@link DataObject} associated with this {@link BlockPosition} or null if it doesn't exist
     */
    @Nullable
    public DataObject getBlock(BlockPosition blockPosition) {
//        final ChunkPosition pos = new ChunkPosition(blockPosition.getChunk());
        final ChunkPosition fake = new ChunkPosition(null, 1, 1);
        final DataObject chunk = this.chunkData.get(fake);

        if (chunk != null) {
            // TODO: Return the actual data
//            return chunk.getObject("Blocks");
//            return new DataObject();
            return chunk;
        } else {
            return new DataObject();
        }
    }

    ////////////////////////////////////////
    // Loading/Saving
    ////////////////////////////////////////
    /**
     * Load the {@link DataObject} linked to this {@link ChunkPosition}, if the chunk doesn't have one it returns null.
     *
     * @param chunkPosition The chunk of the data to retrieve
     * @return The {@link DataObject} associated with this {@link ChunkPosition} or null if it doesn't exist
     */
    @Nullable
    public abstract DataObject loadChunk(@Nonnull ChunkPosition chunkPosition);

    /**
     * Save the {@link DataObject} linked to this {@link ChunkPosition}.
     *
     * @param chunkPosition The chunk of the data to retrieve
     * @param object The {@link DataObject}
     */
    public abstract void saveChunk(@Nonnull ChunkPosition chunkPosition, @Nonnull DataObject object);
}
