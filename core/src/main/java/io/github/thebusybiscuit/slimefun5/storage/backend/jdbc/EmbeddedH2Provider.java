package io.github.thebusybiscuit.slimefun5.storage.backend.jdbc;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Properties;
import java.util.logging.Level;

import javax.annotation.Nonnull;

import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;

/**
 * {@link ConnectionProvider} for embedded H2: opens a single {@link Connection} up front and
 * hands out that same instance for the lifetime of the backend (embedded H2 never drops).
 */
class EmbeddedH2Provider implements ConnectionProvider {

    private final Connection connection;

    EmbeddedH2Provider(@Nonnull String jdbcUrl) {
        try {
            // H2 is not shaded into the jar; the driver is loaded from the classpath if present
            // (unit tests) or downloaded once into plugins/Slimefun/libraries otherwise. We connect
            // through the Driver instance directly rather than DriverManager, which can't see a driver
            // loaded by a child classloader.
            Driver driver = StorageDriverLoader.loadDriver("org.h2.Driver", Collections.singletonList("com.h2database:h2:2.1.214"));
            connection = driver.connect(jdbcUrl, new Properties());

            if (connection == null) {
                throw new IllegalStateException("H2 driver did not accept the URL " + jdbcUrl);
            }

            connection.setAutoCommit(true);
        } catch (Exception e) {
            throw new IllegalStateException("Could not open H2 storage at " + jdbcUrl, e);
        }
    }

    @Override
    @Nonnull
    public Connection conn() {
        return connection;
    }

    @Override
    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            Slimefun.logger().log(Level.WARNING, "Error closing H2 storage", e);
        }
    }
}
