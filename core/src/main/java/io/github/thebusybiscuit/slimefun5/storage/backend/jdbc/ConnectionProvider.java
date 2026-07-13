package io.github.thebusybiscuit.slimefun5.storage.backend.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

import javax.annotation.Nonnull;

/**
 * Supplies the {@link Connection} {@link JdbcBackend} operates on, decoupling connection
 * acquisition/lifecycle from the storage logic itself.
 */
interface ConnectionProvider {

    @Nonnull
    Connection conn() throws SQLException;

    void close();
}
