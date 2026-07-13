package io.github.thebusybiscuit.slimefun5.storage.backend.jdbc;

import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;

/**
 * Reads the {@code storage.backend} config setting to decide which {@link
 * io.github.thebusybiscuit.slimefun5.storage.backend.BlockStorageBackend} to construct at boot.
 *
 * <p>
 * Absent or any value other than {@code legacy}/{@code mysql} selects the H2 database backend;
 * only an explicit {@code legacy} keeps flat files.
 */
public final class StorageBackendConfig {

    private StorageBackendConfig() {}

    public enum Backend {
        LEGACY,
        H2,
        MYSQL
    }

    public static Backend backend() {
        String value = Slimefun.getCfg().getString("storage.backend");

        if ("legacy".equalsIgnoreCase(value)) {
            return Backend.LEGACY;
        } else if ("mysql".equalsIgnoreCase(value)) {
            return Backend.MYSQL;
        } else {
            return Backend.H2;
        }
    }

    public static boolean isJdbcBackend() {
        return backend() != Backend.LEGACY;
    }

    public static String h2Url() {
        return "jdbc:h2:file:./data-storage/Slimefun/slimefun;AUTO_SERVER=FALSE";
    }

    public static String mysqlUrl() {
        String host = Slimefun.getCfg().getOrSetDefault("storage.mysql.host", "localhost");
        int port = Slimefun.getCfg().getOrSetDefault("storage.mysql.port", 3306);
        String database = Slimefun.getCfg().getOrSetDefault("storage.mysql.database", "slimefun");
        String extraParams = Slimefun.getCfg().getOrSetDefault("storage.mysql.extra-params", "useSSL=false&allowPublicKeyRetrieval=true&characterEncoding=utf8");
        return "jdbc:mysql://" + host + ':' + port + '/' + database + '?' + extraParams;
    }

    public static String mysqlUser() {
        return Slimefun.getCfg().getOrSetDefault("storage.mysql.username", "root");
    }

    public static String mysqlPassword() {
        return Slimefun.getCfg().getOrSetDefault("storage.mysql.password", "");
    }
}
