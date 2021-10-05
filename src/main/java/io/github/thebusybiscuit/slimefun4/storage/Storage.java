package io.github.thebusybiscuit.slimefun4.storage;

import io.github.bakedlibs.dough.blocks.BlockPosition;
import io.github.bakedlibs.dough.blocks.ChunkPosition;
import org.bukkit.Chunk;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class Storage {

    private final Map<ChunkPosition, DataObject> chunkData = new ConcurrentHashMap<>();

    ////////////////////////////////////////
    // Getters
    ////////////////////////////////////////
    @Nullable
    public DataObject getChunk(Chunk chunk) {
        return chunkData.get(new ChunkPosition(chunk));
    }

    @Nullable
    public DataObject getChunk(ChunkPosition chunkPosition) {
        return chunkData.get(chunkPosition);
    }

    @Nullable
    public DataObject getBlock(BlockPosition blockPosition) {
        final ChunkPosition pos = new ChunkPosition(blockPosition.getChunk());
        final DataObject chunk = this.chunkData.get(pos);

        if (chunk != null) {
            // TODO: Return the actual data
            return chunk.getObject("Blocks");
        } else {
            return null;
        }
    }

    ////////////////////////////////////////
    // Loading/Saving
    ////////////////////////////////////////
    @Nullable
    public abstract DataObject loadChunk(@Nonnull ChunkPosition chunkPosition);

    public abstract void saveChunk(@Nonnull ChunkPosition chunkPosition, @Nonnull DataObject object);
}
