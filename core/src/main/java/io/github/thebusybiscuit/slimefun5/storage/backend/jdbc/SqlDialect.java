package io.github.thebusybiscuit.slimefun5.storage.backend.jdbc;

/**
 * Per-database SQL for {@link JdbcBackend}: schema DDL and the upsert statements used by the
 * flush methods. SELECT/DELETE statements are dialect-neutral and stay inline in
 * {@link JdbcBackend}.
 */
interface SqlDialect {

    String[] ddl();

    /**
     * Schema for the player-data store ({@link JdbcStorage}): {@code storage_meta} plus the
     * normalized {@code player_research}/{@code player_backpack}/{@code player_waypoint} tables.
     * Kept separate from {@link #ddl()} so the player store can run in its own database without
     * also creating the block tables.
     */
    String[] playerDdl();

    String upsertBlocks();

    String upsertChunks();

    String upsertBlockInventory();

    String upsertUniversalInventory();

    String upsertMeta();
}
