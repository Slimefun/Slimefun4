package io.github.thebusybiscuit.slimefun5.storage.backend.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Pure-JDBC schema test: no MockBukkit needed since the schema only touches H2 via plain SQL.
 */
class JdbcBackendTest {

    @Test
    void schemaIsCreatedIdempotently() throws Exception {
        JdbcBackend b1 = new JdbcBackend("jdbc:h2:mem:sf_schema;DB_CLOSE_DELAY=-1");
        // Same mem db, second init must not fail.
        JdbcBackend b2 = new JdbcBackend("jdbc:h2:mem:sf_schema;DB_CLOSE_DELAY=-1");

        try (Connection c = DriverManager.getConnection("jdbc:h2:mem:sf_schema;DB_CLOSE_DELAY=-1");
                ResultSet rs = c.getMetaData().getTables(null, null, "BLOCK_DATA", null)) {
            Assertions.assertTrue(rs.next(), "block_data table must exist");
        }

        b1.close();
        b2.close();
    }
}
