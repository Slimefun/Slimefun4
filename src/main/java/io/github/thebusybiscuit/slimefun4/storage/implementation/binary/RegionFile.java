package io.github.thebusybiscuit.slimefun4.storage.implementation.binary;

/**
 * This class is responsible for writing region data files.
 * <p>
 * <b>Introducing: Piston Format</b><br>
 * The format for this format is as follows:
 * <pre>{@code
 * | version (1 byte) | offsets | chunks... |
 *
 * Offset:
 *   x - byte
 *   z - byte
 *   sectorStart: int
 *   numSectors: byte (8KiB sectors, max of 256 - max chunk size: 2MiB)
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

    @Override
    public void close() throws Exception {

    }
}
