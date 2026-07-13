package io.github.thebusybiscuit.slimefun5.storage.backend.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;

import javax.annotation.Nonnull;

import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;

/**
 * {@link ConnectionProvider} for MySQL: a single validated {@link Connection}, no pool. Unlike
 * {@link EmbeddedH2Provider}, a MySQL connection can legitimately drop (network blip, server
 * restart, idle timeout), so {@link #conn()} re-validates and reopens as needed instead of handing
 * out one connection for the whole plugin lifetime.
 */
public class MySqlProvider implements ConnectionProvider {

    private static boolean driverLoaded = false;

    private final String url;
    private final String user;
    private final String password;
    private Connection connection;

    public MySqlProvider(@Nonnull String url, @Nonnull String user, @Nonnull String password) {
        this.url = url;
        this.user = user;
        this.password = password;
        loadDriver();
    }

    /**
     * The production shadowJar relocates {@code com.mysql} to {@code
     * io.github.thebusybiscuit.slimefun5.libraries.mysql}, but a bare {@code Class.forName} String
     * literal is NOT rewritten by the relocator (only real class references are). So we try the
     * relocated name first (matches the shaded jar) and fall back to the original (matches an
     * unshaded classpath, e.g. a unit test with the driver on it directly).
     */
    private static synchronized void loadDriver() {
        if (driverLoaded) {
            return;
        }

        try {
            Class.forName("io.github.thebusybiscuit.slimefun5.libraries.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException relocatedNotFound) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
            } catch (ClassNotFoundException originalNotFound) {
                throw new IllegalStateException("MySQL JDBC driver not found on the classpath");
            }
        }

        driverLoaded = true;
    }

    @Override
    @Nonnull
    public Connection conn() throws SQLException {
        if (connection == null || connection.isClosed() || !connection.isValid(2)) {
            close();
            connection = DriverManager.getConnection(url, user, password);
            connection.setAutoCommit(true);
        }

        return connection;
    }

    @Override
    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            Slimefun.logger().log(Level.WARNING, "Error closing MySQL storage connection", e);
        }
    }
}
