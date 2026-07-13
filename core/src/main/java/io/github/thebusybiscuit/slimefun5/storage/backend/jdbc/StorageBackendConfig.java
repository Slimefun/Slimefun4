package io.github.thebusybiscuit.slimefun5.storage.backend.jdbc;

import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;

/**
 * Reads the {@code storage.backend} config setting to decide which {@link
 * io.github.thebusybiscuit.slimefun5.storage.backend.BlockStorageBackend} to construct at boot.
 *
 * <p>
 * Absent or any value other than {@code legacy} selects the H2 database backend; only an explicit
 * {@code legacy} keeps flat files.
 */
public final class StorageBackendConfig {

    private StorageBackendConfig() {}

    public static boolean isJdbcBackend() {
        return !"legacy".equalsIgnoreCase(Slimefun.getCfg().getString("storage.backend"));
    }

    public static String h2Url() {
        return "jdbc:h2:file:./data-storage/Slimefun/slimefun;AUTO_SERVER=FALSE";
    }
}
