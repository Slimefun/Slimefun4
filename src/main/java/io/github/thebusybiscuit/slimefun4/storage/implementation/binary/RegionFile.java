package io.github.thebusybiscuit.slimefun4.storage.implementation.binary;

import io.github.thebusybiscuit.slimefun4.core.debug.Debug;
import io.github.thebusybiscuit.slimefun4.core.debug.TestCase;
import io.github.thebusybiscuit.slimefun4.storage.DataObject;
import io.github.thebusybiscuit.slimefun4.utils.IntPair;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Map;

/**
 * This class is responsible for writing region data files.
 * <p>
 * <b>Introducing: Piston Format</b><br>
 * The format for this format is as follows:
 * <pre>{@code
 * | version (1 byte) | offsets | chunks... |
 *
 * Offset:
 *   count - short
 *   array:
 *     x - byte
 *     z - byte
 *     numSectors: byte (8KiB sectors, max of 256 - max chunk size: 2MiB)
 *     sectorStart: int
 * Chunk:
 *   // TODO
 * }</pre>
 *
 * <table>
 *   <tr>
 *     <td>Key</td>
 *     <td>Description</td>
 *   </tr>
 *   <tr>
 *     <td>Version</td>
 *     <td>This a 1 byte value that describes the version of this format</td>
 *   </tr>
 *   <tr>
 *       <td>Offset</td>
 *       <td>Where in the file the needed data is. This is indexed with chunk x, z, sector start and num of sectors</td>
 *   </tr>
 *   <tr>
 *       <td>Sector</td>
 *       <td>Sectors are columns of data. Each sector is 8KiB</td>
 *   </tr>
 * </table>
 */
public final class RegionFile implements AutoCloseable {

    private static final short VERSION = 1;

    // 32x32 chunks
    private static final int CHUNKS = 1024;
    // 8KiB
    private static final int SECTOR_SIZE = 8192;
    private static final int MAX_SECTORS_PER_CHUNK = 256;
    private static final int MAX_REGION_SECTORS = MAX_SECTORS_PER_CHUNK * CHUNKS;

    private final RandomAccessFile file;
    private final Map<IntPair, IntPair> offsets = new HashMap<>();
    // TODO: Figure out a better way to store this - maybe some bit set?
    private final boolean[] sectorAvailability = new boolean[MAX_REGION_SECTORS];

    public RegionFile(@Nonnull File regionFile) throws IOException {
        // Steps:
        // 1. Read the file header
        // 2. Read the offsets
        // 3. Find the chunk
        // 4. Load into BinaryReader (which decompresses and reads)
        // 5. Store in memory

        Debug.log(TestCase.REGION_IO, "Caching {}", regionFile.getName());

        file = new RandomAccessFile(regionFile, "rw");

        // We don't want to try and read any data from here. We do however still want the access file so we can write
        if (!regionFile.exists()) return;

        long regionFileSize = file.length();

        final byte version = file.readByte();
        Debug.log(TestCase.REGION_IO, "Region file version: {} (expected: {})", version, VERSION);

        final short offsetCount = file.readShort();
        for (short idx = 0; idx < offsetCount; idx++) {
            final byte relativeChunkX = file.readByte();
            final byte relativeChunkZ = file.readByte();
            final byte numSectors = file.readByte();
            final int sectorStart = file.readInt();

            offsets.put(new IntPair(relativeChunkX, relativeChunkZ), new IntPair(numSectors, sectorStart));
        }
    }

    public void write(int x, int z, @Nonnull DataObject object) {

    }

    @Override
    public void close() throws Exception {
        this.offsets.clear();
        this.file.getChannel().close();
        this.file.close();
    }
}
