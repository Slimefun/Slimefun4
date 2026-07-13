package io.github.thebusybiscuit.slimefun5.storage.backend.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
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
            // H2 registers its driver via META-INF/services, but shadowJar's `exclude("META-INF/**")`
            // strips that file from the shaded jar, so ServiceLoader auto-registration won't fire at
            // runtime. Force registration explicitly instead. We reference the driver class directly
            // (not a "org.h2.Driver" string literal) because shadow's relocator only guarantees
            // rewriting genuine class references (CONSTANT_Class in the bytecode); a bare string isn't
            // reliably rewritten across relocator implementations. `.class.getName()` compiles to a real
            // class reference, so it is relocated together with the rest of org.h2.
            Class.forName(org.h2.Driver.class.getName());
            connection = DriverManager.getConnection(jdbcUrl);
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
