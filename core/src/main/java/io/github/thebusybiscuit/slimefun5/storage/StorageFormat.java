package io.github.thebusybiscuit.slimefun5.storage;

import java.util.logging.Level;

import io.github.bakedlibs.dough.config.Config;
import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;

/**
 * Records the on-disk block-data layout version in a side-car marker
 * ({@code data-storage/Slimefun/storage-format.yml}).
 *
 * <p>The per-block {@code .sfb} format itself is intentionally <em>not</em> touched — it must stay
 * byte-identical to upstream Slimefun so existing and upstream worlds keep loading unchanged. This
 * marker simply lets future block-data migrations detect the format and run safely. Data written
 * without the marker (older fork builds, or upstream) is treated as v1.
 */
public final class StorageFormat {

    /** Bump when the block-data layout changes and add a migration keyed off the previous value. */
    public static final int CURRENT_BLOCK_DATA_VERSION = 1;

    private StorageFormat() {}

    /** Reads the marker, warns if it was written by a newer build, and stamps it when absent. */
    public static void checkAndStamp() {
        try {
            Config marker = new Config("data-storage/Slimefun/storage-format.yml");
            boolean present = marker.contains("block-data-version");
            int version = present ? marker.getInt("block-data-version") : 1;

            if (version > CURRENT_BLOCK_DATA_VERSION) {
                Slimefun.logger().log(Level.WARNING, "Block data was written by a newer Slimefun (format v{0} > v{1}); loading best-effort.", new Object[] { version, CURRENT_BLOCK_DATA_VERSION });
            }

            if (!present) {
                marker.setValue("block-data-version", CURRENT_BLOCK_DATA_VERSION);
                marker.save();
            }
        } catch (Exception e) {
            Slimefun.logger().log(Level.WARNING, "Could not read/write the storage-format marker: {0}", e.getMessage());
        }
    }
}
