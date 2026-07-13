package io.github.thebusybiscuit.slimefun5.storage.backend.jdbc;

/**
 * Per-database SQL for {@link JdbcBackend}: schema DDL and the upsert statements used by the
 * flush methods. SELECT/DELETE statements are dialect-neutral and stay inline in
 * {@link JdbcBackend}.
 */
interface SqlDialect {

    String[] ddl();

    String upsertBlocks();

    String upsertChunks();

    String upsertBlockInventory();

    String upsertUniversalInventory();

    String upsertMeta();
}
