package io.github.thebusybiscuit.slimefun5.storage.backend.jdbc;

/**
 * {@link SqlDialect} for embedded H2, the only backend {@link JdbcBackend} currently supports.
 */
class H2Dialect implements SqlDialect {

    private static final String[] DDL = {
        "CREATE TABLE IF NOT EXISTS storage_meta (k VARCHAR(64) PRIMARY KEY, v VARCHAR(255))",
        "CREATE TABLE IF NOT EXISTS block_data (world VARCHAR(255), x INT, y INT, z INT, sf_id VARCHAR(255), data CLOB, PRIMARY KEY(world,x,y,z))",
        "CREATE TABLE IF NOT EXISTS chunk_data (world VARCHAR(255), cx INT, cz INT, data CLOB, PRIMARY KEY(world,cx,cz))",
        "CREATE TABLE IF NOT EXISTS block_inventory (world VARCHAR(255), x INT, y INT, z INT, inv CLOB, PRIMARY KEY(world,x,y,z))",
        "CREATE TABLE IF NOT EXISTS universal_inventory (id VARCHAR(255) PRIMARY KEY, inv CLOB)",
        "CREATE INDEX IF NOT EXISTS idx_block_data_world ON block_data(world)",
        "CREATE INDEX IF NOT EXISTS idx_block_inventory_world ON block_inventory(world)",
        "CREATE INDEX IF NOT EXISTS idx_chunk_data_world ON chunk_data(world)"
    };

    @Override
    public String[] ddl() {
        return DDL;
    }

    @Override
    public String upsertBlocks() {
        return "MERGE INTO block_data(world,x,y,z,sf_id,data) KEY(world,x,y,z) VALUES(?,?,?,?,?,?)";
    }

    @Override
    public String upsertChunks() {
        return "MERGE INTO chunk_data(world,cx,cz,data) KEY(world,cx,cz) VALUES(?,?,?,?)";
    }

    @Override
    public String upsertBlockInventory() {
        return "MERGE INTO block_inventory(world,x,y,z,inv) KEY(world,x,y,z) VALUES(?,?,?,?,?)";
    }

    @Override
    public String upsertUniversalInventory() {
        return "MERGE INTO universal_inventory(id,inv) KEY(id) VALUES(?,?)";
    }

    @Override
    public String upsertMeta() {
        return "MERGE INTO storage_meta(k,v) KEY(k) VALUES(?,?)";
    }
}
