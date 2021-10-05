package io.github.thebusybiscuit.slimefun4.storage.implementation;

import io.github.bakedlibs.dough.blocks.ChunkPosition;
import io.github.thebusybiscuit.slimefun4.storage.DataObject;
import io.github.thebusybiscuit.slimefun4.storage.Storage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class LegacyStorage extends Storage {

    @Nullable
    @Override
    public DataObject loadChunk(@Nonnull ChunkPosition chunkPosition) {
        return null;
    }

    @Override
    public void saveChunk(@Nonnull ChunkPosition chunkPosition, @Nonnull DataObject object) {

    }
}
