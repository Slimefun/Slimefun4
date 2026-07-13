package io.github.thebusybiscuit.slimefun5.storage.backend.jdbc;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Properties;
import java.util.logging.Level;

import javax.annotation.Nonnull;

import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;

/**
 * {@link ConnectionProvider} for MySQL: a single validated {@link Connection}, no pool. Unlike
 * {@link EmbeddedH2Provider}, a MySQL connection can legitimately drop (network blip, server
 * restart, idle timeout), so {@link #conn()} re-validates and reopens as needed instead of handing
 * out one connection for the whole plugin lifetime.
 *
 * <p>
 * The MySQL driver is not shaded into the jar; it (and its protobuf dependency) is loaded from the
 * classpath if present, else downloaded once into {@code plugins/Slimefun/libraries} by
 * {@link StorageDriverLoader}. We connect through the {@link Driver} instance directly, since
 * {@link java.sql.DriverManager} cannot see a driver loaded by a child classloader.
 */
public class MySqlProvider implements ConnectionProvider {

    private final String url;
    private final Properties properties;
    private final Driver driver;
    private Connection connection;

    public MySqlProvider(@Nonnull String url, @Nonnull String user, @Nonnull String password) {
        this.url = url;
        this.properties = new Properties();
        this.properties.setProperty("user", user);
        this.properties.setProperty("password", password);
        this.driver = StorageDriverLoader.loadDriver("com.mysql.cj.jdbc.Driver",
                Arrays.asList("com.mysql:mysql-connector-j:8.0.33", "com.google.protobuf:protobuf-java:3.21.9"));
    }

    @Override
    @Nonnull
    public Connection conn() throws SQLException {
        if (connection == null || connection.isClosed() || !connection.isValid(2)) {
            close();
            connection = driver.connect(url, properties);

            if (connection == null) {
                throw new SQLException("MySQL driver did not accept the URL " + url);
            }

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
        } finally {
            connection = null;
        }
    }
}
