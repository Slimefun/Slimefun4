package io.github.thebusybiscuit.slimefun4.storage.implementation;

import io.github.bakedlibs.dough.blocks.ChunkPosition;
import io.github.thebusybiscuit.slimefun4.storage.DataObject;
import io.github.thebusybiscuit.slimefun4.storage.Storage;
import org.bukkit.Chunk;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;

/**
 * Binary storage is simply storing data in binary format.
 * Instead of the legacy way of shoving a ton of JSONs into a single file this will instead split the data up
 * into regions.
 *
 *
 */
public class BinaryStorage extends Storage {

    // /data-storage/regions/<x>.<z>.sf
    private static final File regionFolder = new File("data-storage", "regions");

    private final short VERSION = 1;

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public DataObject loadChunk(@Nonnull ChunkPosition chunkPosition) {
        final File regionFile = getRegionFile(chunkPosition);

        if (regionFile.exists()) {
            return readRegionData(regionFile);
        } else {
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveChunk(@Nonnull ChunkPosition chunkPosition, @Nonnull DataObject object) {
        writeRegionData(getRegionFile(chunkPosition), object);
    }

    ////////////////////////////////////////
    // Reading/Writing binary data
    ////////////////////////////////////////
    @Nonnull
    private File getRegionFile(@Nonnull ChunkPosition position) {
        final Chunk chunk = position.getChunk();

        final int regionX = chunk.getX() >> 5;
        final int regionZ = chunk.getZ() >> 5;

        return new File(regionFolder, "r." + regionX + '.' + regionZ + ".sf");
    }

    @Nonnull
    private DataObject readRegionData(@Nonnull File f) {
        // TODO: Write

        return new DataObject();
    }

    private void writeRegionData(@Nonnull File regionFile, @Nonnull DataObject object) {
        // TODO: Figure out how I want to actually write a region specific one

        // Data structure:
        // Index table
        //   - 3000,6000 - 1,2
        // Version: 1s
        // Dictionary: [
        //   'CARGO_INPUT',
        //   'CARGO_OUTPUT'
        // ]
        // relX,relZ (Chunk)

        // Chunk Data:
        // relX,relZ:
        //   <Data>
        //   'Blocks':
        //     <relX&relY&relZ>:
        //       'Id': 0
        //       'slimefun:energy_charge': 1000d
    }
}
