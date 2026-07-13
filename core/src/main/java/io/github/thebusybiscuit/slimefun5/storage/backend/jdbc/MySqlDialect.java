package io.github.thebusybiscuit.slimefun5.storage.backend.jdbc;

/**
 * {@link SqlDialect} for MySQL/MariaDB. Column list/order and primary keys mirror {@link
 * H2Dialect} exactly so parameter binding in {@link JdbcBackend} is unchanged; only the CLOB type
 * and upsert syntax differ.
 *
 * <p>
 * MySQL has no {@code CREATE INDEX IF NOT EXISTS}, so the 3 index statements are plain {@code
 * CREATE INDEX} and will throw "Duplicate key name" on a re-run; {@link JdbcBackend#createSchema}
 * guards each DDL statement individually and ignores that failure.
 */
public class MySqlDialect implements SqlDialect {

    private static final String[] DDL = {
        "CREATE TABLE IF NOT EXISTS storage_meta (k VARCHAR(64) PRIMARY KEY, v VARCHAR(255))",
        "CREATE TABLE IF NOT EXISTS block_data (world VARCHAR(255), x INT, y INT, z INT, sf_id VARCHAR(255), data LONGTEXT, PRIMARY KEY(world,x,y,z))",
        "CREATE TABLE IF NOT EXISTS chunk_data (world VARCHAR(255), cx INT, cz INT, data LONGTEXT, PRIMARY KEY(world,cx,cz))",
        "CREATE TABLE IF NOT EXISTS block_inventory (world VARCHAR(255), x INT, y INT, z INT, inv LONGTEXT, PRIMARY KEY(world,x,y,z))",
        "CREATE TABLE IF NOT EXISTS universal_inventory (id VARCHAR(255) PRIMARY KEY, inv LONGTEXT)",
        "CREATE INDEX idx_block_data_world ON block_data(world)",
        "CREATE INDEX idx_block_inventory_world ON block_inventory(world)",
        "CREATE INDEX idx_chunk_data_world ON chunk_data(world)"
    };

    @Override
    public String[] ddl() {
        return DDL;
    }

    @Override
    public String upsertBlocks() {
        return "INSERT INTO block_data(world,x,y,z,sf_id,data) VALUES(?,?,?,?,?,?) ON DUPLICATE KEY UPDATE sf_id=VALUES(sf_id), data=VALUES(data)";
    }

    @Override
    public String upsertChunks() {
        return "INSERT INTO chunk_data(world,cx,cz,data) VALUES(?,?,?,?) ON DUPLICATE KEY UPDATE data=VALUES(data)";
    }

    @Override
    public String upsertBlockInventory() {
        return "INSERT INTO block_inventory(world,x,y,z,inv) VALUES(?,?,?,?,?) ON DUPLICATE KEY UPDATE inv=VALUES(inv)";
    }

    @Override
    public String upsertUniversalInventory() {
        return "INSERT INTO universal_inventory(id,inv) VALUES(?,?) ON DUPLICATE KEY UPDATE inv=VALUES(inv)";
    }

    @Override
    public String upsertMeta() {
        return "INSERT INTO storage_meta(k,v) VALUES(?,?) ON DUPLICATE KEY UPDATE v=VALUES(v)";
    }
}
