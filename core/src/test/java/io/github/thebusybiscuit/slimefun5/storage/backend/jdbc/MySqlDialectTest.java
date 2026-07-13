package io.github.thebusybiscuit.slimefun5.storage.backend.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Exercises {@link MySqlDialect}'s DDL and upsert statements against H2 running in
 * {@code MODE=MySQL}, which parses MySQL syntax (backtick-free identifiers, {@code ON DUPLICATE
 * KEY UPDATE}, etc.) closely enough to catch dialect regressions without a real MySQL server.
 */
class MySqlDialectTest {

    private static final String URL = "jdbc:h2:mem:sp4mysql;MODE=MySQL;DB_CLOSE_DELAY=-1";

    private Connection connection;

    private Connection open() throws SQLException {
        connection = DriverManager.getConnection(URL);

        try (Statement st = connection.createStatement()) {
            for (String stmt : new MySqlDialect().ddl()) {
                st.execute(stmt);
            }
        }

        return connection;
    }

    @AfterEach
    void tearDown() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            // Wipe every table so the same shared in-memory DB is clean for the next test method.
            try (Statement st = connection.createStatement()) {
                st.execute("DROP ALL OBJECTS");
            }

            connection.close();
        }
    }

    @Test
    void testUpsertBlocksUpdatesInsteadOfDuplicating() throws SQLException {
        Connection c = open();

        try (PreparedStatement upsert = c.prepareStatement(new MySqlDialect().upsertBlocks())) {
            upsert.setString(1, "world");
            upsert.setInt(2, 1);
            upsert.setInt(3, 2);
            upsert.setInt(4, 3);
            upsert.setString(5, "OLD_ID");
            upsert.setString(6, "{\"id\":\"OLD_ID\"}");
            upsert.executeUpdate();

            upsert.setString(1, "world");
            upsert.setInt(2, 1);
            upsert.setInt(3, 2);
            upsert.setInt(4, 3);
            upsert.setString(5, "NEW_ID");
            upsert.setString(6, "{\"id\":\"NEW_ID\"}");
            upsert.executeUpdate();
        }

        try (Statement st = c.createStatement(); ResultSet rs = st.executeQuery("SELECT sf_id, data FROM block_data")) {
            Assertions.assertTrue(rs.next(), "row must exist");
            Assertions.assertEquals("NEW_ID", rs.getString("sf_id"));
            Assertions.assertEquals("{\"id\":\"NEW_ID\"}", rs.getString("data"));
            Assertions.assertFalse(rs.next(), "the upsert must update the existing row, not duplicate it");
        }
    }

    @Test
    void testUpsertChunksUpdatesInsteadOfDuplicating() throws SQLException {
        Connection c = open();

        try (PreparedStatement upsert = c.prepareStatement(new MySqlDialect().upsertChunks())) {
            upsert.setString(1, "world");
            upsert.setInt(2, 0);
            upsert.setInt(3, 0);
            upsert.setString(4, "old-data");
            upsert.executeUpdate();

            upsert.setString(1, "world");
            upsert.setInt(2, 0);
            upsert.setInt(3, 0);
            upsert.setString(4, "new-data");
            upsert.executeUpdate();
        }

        try (Statement st = c.createStatement(); ResultSet rs = st.executeQuery("SELECT data FROM chunk_data")) {
            Assertions.assertTrue(rs.next());
            Assertions.assertEquals("new-data", rs.getString("data"));
            Assertions.assertFalse(rs.next());
        }
    }

    @Test
    void testUpsertBlockInventoryUpdatesInsteadOfDuplicating() throws SQLException {
        Connection c = open();

        try (PreparedStatement upsert = c.prepareStatement(new MySqlDialect().upsertBlockInventory())) {
            upsert.setString(1, "world");
            upsert.setInt(2, 4);
            upsert.setInt(3, 5);
            upsert.setInt(4, 6);
            upsert.setString(5, "old-inv");
            upsert.executeUpdate();

            upsert.setString(1, "world");
            upsert.setInt(2, 4);
            upsert.setInt(3, 5);
            upsert.setInt(4, 6);
            upsert.setString(5, "new-inv");
            upsert.executeUpdate();
        }

        try (Statement st = c.createStatement(); ResultSet rs = st.executeQuery("SELECT inv FROM block_inventory")) {
            Assertions.assertTrue(rs.next());
            Assertions.assertEquals("new-inv", rs.getString("inv"));
            Assertions.assertFalse(rs.next());
        }
    }

    @Test
    void testUpsertUniversalInventoryUpdatesInsteadOfDuplicating() throws SQLException {
        Connection c = open();

        try (PreparedStatement upsert = c.prepareStatement(new MySqlDialect().upsertUniversalInventory())) {
            upsert.setString(1, "PRESET_ID");
            upsert.setString(2, "old-inv");
            upsert.executeUpdate();

            upsert.setString(1, "PRESET_ID");
            upsert.setString(2, "new-inv");
            upsert.executeUpdate();
        }

        try (Statement st = c.createStatement(); ResultSet rs = st.executeQuery("SELECT inv FROM universal_inventory")) {
            Assertions.assertTrue(rs.next());
            Assertions.assertEquals("new-inv", rs.getString("inv"));
            Assertions.assertFalse(rs.next());
        }
    }

    @Test
    void testUpsertMetaUpdatesInsteadOfDuplicating() throws SQLException {
        Connection c = open();

        try (PreparedStatement upsert = c.prepareStatement(new MySqlDialect().upsertMeta())) {
            upsert.setString(1, "k");
            upsert.setString(2, "v1");
            upsert.executeUpdate();

            upsert.setString(1, "k");
            upsert.setString(2, "v2");
            upsert.executeUpdate();
        }

        try (Statement st = c.createStatement(); ResultSet rs = st.executeQuery("SELECT v FROM storage_meta")) {
            Assertions.assertTrue(rs.next());
            Assertions.assertEquals("v2", rs.getString("v"));
            Assertions.assertFalse(rs.next());
        }
    }

    @Test
    void testDdlIndexCreationCanBeRerunWhenGuardedLikeCreateSchema() throws SQLException {
        // MySQL's CREATE INDEX has no IF NOT EXISTS, so re-running the DDL as-is throws on the
        // second pass. JdbcBackend.createSchema is the piece that guards each statement
        // individually (Step 2); here we just confirm the raw statement re-execution throws,
        // which is exactly the case that guard must swallow.
        Connection c = open();

        try (Statement st = c.createStatement()) {
            Assertions.assertThrows(SQLException.class, () -> {
                for (String stmt : new MySqlDialect().ddl()) {
                    st.execute(stmt);
                }
            });
        }
    }
}
