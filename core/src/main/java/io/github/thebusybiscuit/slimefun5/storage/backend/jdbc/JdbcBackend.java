package io.github.thebusybiscuit.slimefun5.storage.backend.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.Location;
import org.bukkit.World;

import com.google.common.annotations.Beta;

import io.github.bakedlibs.dough.common.CommonPatterns;
import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun5.storage.backend.BlockStorageBackend;

import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.api.BlockInfoConfig;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.api.inventory.UniversalBlockMenu;

/**
 * Embedded-H2 implementation of {@link BlockStorageBackend}.
 *
 * <p>
 * SP-2 Task 1 scaffold: opens the connection and creates the schema idempotently.
 * The actual read/write methods are filled in by SP-2 Tasks 2-3.
 *
 * <p>
 * <b>This API is still experimental, it may change without notice.</b>
 */
@Beta
public class JdbcBackend implements BlockStorageBackend {

    private static final String STUB_MESSAGE = "SP-2 Task 2/3";

    // Single embedded connection; all access must go through `lock` to keep it thread-safe.
    private final Connection connection;
    private final Object lock = new Object();

    public JdbcBackend(@Nonnull String jdbcUrl) {
        Connection c;
        try {
            // H2 registers its driver via META-INF/services, but shadowJar's `exclude("META-INF/**")`
            // strips that file from the shaded jar, so ServiceLoader auto-registration won't fire at
            // runtime. Force registration explicitly instead. We reference the driver class directly
            // (not a "org.h2.Driver" string literal) because shadow's relocator only guarantees
            // rewriting genuine class references (CONSTANT_Class in the bytecode); a bare string isn't
            // reliably rewritten across relocator implementations. `.class.getName()` compiles to a real
            // class reference, so it is relocated together with the rest of org.h2.
            Class.forName(org.h2.Driver.class.getName());
            c = DriverManager.getConnection(jdbcUrl);
            c.setAutoCommit(true);
            createSchema(c);
        } catch (Exception e) {
            throw new IllegalStateException("Could not open H2 storage at " + jdbcUrl, e);
        }
        this.connection = c;
    }

    private void createSchema(@Nonnull Connection c) throws SQLException {
        try (Statement st = c.createStatement()) {
            st.execute("CREATE TABLE IF NOT EXISTS storage_meta (k VARCHAR(64) PRIMARY KEY, v VARCHAR(255))");
            st.execute("CREATE TABLE IF NOT EXISTS block_data (world VARCHAR(255), x INT, y INT, z INT, sf_id VARCHAR(255), data CLOB, PRIMARY KEY(world,x,y,z))");
            st.execute("CREATE TABLE IF NOT EXISTS chunk_data (world VARCHAR(255), cx INT, cz INT, data CLOB, PRIMARY KEY(world,cx,cz))");
            st.execute("CREATE TABLE IF NOT EXISTS block_inventory (world VARCHAR(255), x INT, y INT, z INT, inv CLOB, PRIMARY KEY(world,x,y,z))");
            st.execute("CREATE TABLE IF NOT EXISTS universal_inventory (id VARCHAR(255) PRIMARY KEY, inv CLOB)");
            st.execute("CREATE INDEX IF NOT EXISTS idx_block_data_world ON block_data(world)");
            st.execute("CREATE INDEX IF NOT EXISTS idx_block_inventory_world ON block_inventory(world)");
            st.execute("CREATE INDEX IF NOT EXISTS idx_chunk_data_world ON chunk_data(world)");
        }
    }

    /**
     * Package-private accessor for Tasks 2/3 + tests: every use must synchronize on {@link #lock}.
     */
    @Nonnull
    Connection connection() {
        return connection;
    }

    @Nonnull
    Object lock() {
        return lock;
    }

    @Override
    public void close() {
        synchronized (lock) {
            try {
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                Slimefun.logger().log(Level.WARNING, "Error closing H2 storage", e);
            }
        }
    }

    @Override
    @Nonnull
    public Map<String, Map<Location, Config>> loadWorldBlocks(@Nonnull World world) {
        synchronized (lock) {
            Map<String, Map<Location, Config>> result = new HashMap<>();

            try (PreparedStatement st = connection.prepareStatement("SELECT sf_id, x, y, z, data FROM block_data WHERE world = ?")) {
                st.setString(1, world.getName());

                try (ResultSet rs = st.executeQuery()) {
                    while (rs.next()) {
                        String sfId = rs.getString("sf_id");
                        Location location = new Location(world, rs.getInt("x"), rs.getInt("y"), rs.getInt("z"));
                        Config blockInfo = BlockStorage.parseBlockInfo(location, rs.getString("data"));

                        if (blockInfo != null) {
                            result.computeIfAbsent(sfId, k -> new HashMap<>()).put(location, blockInfo);
                        }
                    }
                }
            } catch (SQLException e) {
                Slimefun.logger().log(Level.SEVERE, e, () -> "Could not load block data from H2 storage for world \"" + world.getName() + '"');
            }

            return result;
        }
    }

    @Override
    @Nonnull
    public Map<String, BlockInfoConfig> loadChunksForWorld(@Nonnull World world) {
        synchronized (lock) {
            Map<String, BlockInfoConfig> result = new HashMap<>();

            try (PreparedStatement st = connection.prepareStatement("SELECT cx, cz, data FROM chunk_data WHERE world = ?")) {
                st.setString(1, world.getName());

                try (ResultSet rs = st.executeQuery()) {
                    while (rs.next()) {
                        String key = BlockStorage.serializeChunk(world, rs.getInt("cx"), rs.getInt("cz"));
                        result.put(key, new BlockInfoConfig(BlockStorage.parseJSON(rs.getString("data"))));
                    }
                }
            } catch (SQLException e) {
                Slimefun.logger().log(Level.SEVERE, e, () -> "Could not load chunk data from H2 storage for world \"" + world.getName() + '"');
            }

            return result;
        }
    }

    @Override
    @Nonnull
    public Map<Location, BlockMenu> loadWorldInventories(@Nonnull World world) {
        throw new UnsupportedOperationException(STUB_MESSAGE);
    }

    @Override
    @Nonnull
    public Map<String, UniversalBlockMenu> loadUniversalInventories() {
        throw new UnsupportedOperationException(STUB_MESSAGE);
    }

    @Override
    @Nullable
    public BlockMenu loadInventoryIfPresent(@Nonnull Location l, @Nonnull BlockMenuPreset preset) {
        throw new UnsupportedOperationException(STUB_MESSAGE);
    }

    @Override
    public void flushBlocks(@Nonnull World world, @Nonnull Map<String, Config> blocksCache) {
        synchronized (lock) {
            try {
                connection.setAutoCommit(false);

                try (PreparedStatement upsert = connection.prepareStatement(
                        "MERGE INTO block_data(world,x,y,z,sf_id,data) KEY(world,x,y,z) VALUES(?,?,?,?,?,?)");
                        PreparedStatement deleteForId = connection.prepareStatement(
                                "DELETE FROM block_data WHERE world = ? AND sf_id = ?");
                        PreparedStatement deleteLocation = connection.prepareStatement(
                                "DELETE FROM block_data WHERE world = ? AND x = ? AND y = ? AND z = ?")) {

                    for (Map.Entry<String, Config> entry : blocksCache.entrySet()) {
                        String sfId = entry.getKey();
                        Config cfg = entry.getValue();

                        if (cfg.getKeys().isEmpty()) {
                            // Mirrors LegacyFileBackend deleting the whole "<sf_id>.sfb" file: no
                            // locations remain queued for this id, so no rows for it remain either.
                            deleteForId.setString(1, world.getName());
                            deleteForId.setString(2, sfId);
                            deleteForId.executeUpdate();
                            continue;
                        }

                        for (String locationKey : cfg.getKeys()) {
                            String[] parts = CommonPatterns.SEMICOLON.split(locationKey);

                            if (parts.length != 4) {
                                continue;
                            }

                            int x = Integer.parseInt(parts[1]);
                            int y = Integer.parseInt(parts[2]);
                            int z = Integer.parseInt(parts[3]);
                            String json = cfg.getString(locationKey);

                            if (json == null) {
                                deleteLocation.setString(1, world.getName());
                                deleteLocation.setInt(2, x);
                                deleteLocation.setInt(3, y);
                                deleteLocation.setInt(4, z);
                                deleteLocation.executeUpdate();
                            } else {
                                upsert.setString(1, world.getName());
                                upsert.setInt(2, x);
                                upsert.setInt(3, y);
                                upsert.setInt(4, z);
                                upsert.setString(5, sfId);
                                upsert.setString(6, json);
                                upsert.executeUpdate();
                            }
                        }
                    }
                }

                connection.commit();
            } catch (SQLException | NumberFormatException e) {
                try {
                    connection.rollback();
                } catch (SQLException rollbackException) {
                    Slimefun.logger().log(Level.SEVERE, rollbackException, () -> "Could not roll back H2 block flush");
                }

                Slimefun.logger().log(Level.SEVERE, e, () -> "Could not flush block data to H2 storage for world \"" + world.getName() + '"');
            } finally {
                try {
                    connection.setAutoCommit(true);
                } catch (SQLException e) {
                    Slimefun.logger().log(Level.WARNING, e, () -> "Could not restore auto-commit on the H2 connection");
                }
            }
        }
    }

    @Override
    public void flushInventories(@Nonnull Map<Location, BlockMenu> dirtyInventories) {
        throw new UnsupportedOperationException(STUB_MESSAGE);
    }

    @Override
    public void flushUniversalInventories(@Nonnull Map<String, UniversalBlockMenu> universalInventories) {
        throw new UnsupportedOperationException(STUB_MESSAGE);
    }

    @Override
    public void flushChunks(@Nonnull Map<String, BlockInfoConfig> chunks) {
        synchronized (lock) {
            try {
                connection.setAutoCommit(false);

                try (PreparedStatement upsert = connection.prepareStatement(
                        "MERGE INTO chunk_data(world,cx,cz,data) KEY(world,cx,cz) VALUES(?,?,?,?)")) {

                    for (Map.Entry<String, BlockInfoConfig> entry : chunks.entrySet()) {
                        BlockInfoConfig biCfg = entry.getValue();

                        if (biCfg.getKeys().isEmpty()) {
                            // Saving empty chunk data is pointless, matches LegacyFileBackend.
                            continue;
                        }

                        String[] parts = CommonPatterns.SEMICOLON.split(entry.getKey());

                        if (parts.length != 4) {
                            continue;
                        }

                        upsert.setString(1, parts[0]);
                        upsert.setInt(2, Integer.parseInt(parts[2]));
                        upsert.setInt(3, Integer.parseInt(parts[3]));
                        upsert.setString(4, biCfg.toJSON());
                        upsert.executeUpdate();
                    }
                }

                connection.commit();
            } catch (SQLException | NumberFormatException e) {
                try {
                    connection.rollback();
                } catch (SQLException rollbackException) {
                    Slimefun.logger().log(Level.SEVERE, rollbackException, () -> "Could not roll back H2 chunk flush");
                }

                Slimefun.logger().log(Level.SEVERE, e, () -> "Could not flush chunk data to H2 storage");
            } finally {
                try {
                    connection.setAutoCommit(true);
                } catch (SQLException e) {
                    Slimefun.logger().log(Level.WARNING, e, () -> "Could not restore auto-commit on the H2 connection");
                }
            }
        }
    }

    @Override
    public void deleteInventory(@Nonnull Location l) {
        throw new UnsupportedOperationException(STUB_MESSAGE);
    }
}
