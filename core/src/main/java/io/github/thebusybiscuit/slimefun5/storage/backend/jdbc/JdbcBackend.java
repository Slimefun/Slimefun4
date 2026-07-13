package io.github.thebusybiscuit.slimefun5.storage.backend.jdbc;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import com.google.common.annotations.Beta;

import io.github.bakedlibs.dough.common.CommonPatterns;
import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun5.storage.backend.BlockStorageBackend;

import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.api.BlockInfoConfig;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.api.inventory.DirtyChestMenu;
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

    // Never read/written to - Config only touches the filesystem in save(), which we never call.
    // The in-memory YamlConfiguration passed alongside it is the actual data holder.
    private static final File DUMMY_MENU_FILE = new File("");

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

    /**
     * Serializes a {@link DirtyChestMenu} (i.e. {@link BlockMenu} or {@link UniversalBlockMenu}) into the
     * exact same YAML a {@code .sfi} file holds, by mirroring {@code BlockMenu.save}/{@code UniversalBlockMenu.save}'s
     * write loop against an in-memory {@link YamlConfiguration} instead of a file-backed one.
     */
    @Nonnull
    private String menuToYaml(@Nonnull DirtyChestMenu menu) {
        YamlConfiguration yaml = new YamlConfiguration();
        io.github.bakedlibs.dough.config.Config cfg = new io.github.bakedlibs.dough.config.Config(DUMMY_MENU_FILE, yaml);

        cfg.setValue("preset", menu.getPreset().getID());

        for (int slot : menu.getPreset().getInventorySlots()) {
            cfg.setValue(String.valueOf(slot), menu.getItemInSlot(slot));
        }

        return yaml.saveToString();
    }

    /**
     * Inverse of {@link #menuToYaml(DirtyChestMenu)}: parses a {@code .sfi}-formatted CLOB back into a
     * dough {@link io.github.bakedlibs.dough.config.Config} that can be fed verbatim into the existing
     * {@code BlockMenu}/{@code UniversalBlockMenu} constructors.
     */
    @Nonnull
    private io.github.bakedlibs.dough.config.Config yamlToConfig(@Nonnull String clob) throws InvalidConfigurationException {
        YamlConfiguration yaml = new YamlConfiguration();
        yaml.loadFromString(clob);
        return new io.github.bakedlibs.dough.config.Config(DUMMY_MENU_FILE, yaml);
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

                        // Match LegacyFileBackend.loadBlock: only surface entries that carry an id.
                        if (blockInfo != null && blockInfo.contains("id")) {
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
        synchronized (lock) {
            Map<Location, BlockMenu> result = new HashMap<>();

            try (PreparedStatement st = connection.prepareStatement("SELECT x, y, z, inv FROM block_inventory WHERE world = ?")) {
                st.setString(1, world.getName());

                try (ResultSet rs = st.executeQuery()) {
                    while (rs.next()) {
                        Location location = new Location(world, rs.getInt("x"), rs.getInt("y"), rs.getInt("z"));

                        try {
                            io.github.bakedlibs.dough.config.Config cfg = yamlToConfig(rs.getString("inv"));
                            BlockMenuPreset preset = BlockMenuPreset.getPreset(cfg.getString("preset"));

                            if (preset == null) {
                                preset = BlockMenuPreset.getPreset(BlockStorage.checkID(location));
                            }

                            if (preset != null) {
                                result.put(location, new BlockMenu(preset, location, cfg));
                            }
                        } catch (InvalidConfigurationException e) {
                            Slimefun.logger().log(Level.SEVERE, e, () -> "Could not parse Block Inventory at " + location);
                        }
                    }
                }
            } catch (SQLException e) {
                Slimefun.logger().log(Level.SEVERE, e, () -> "Could not load block inventories from H2 storage for world \"" + world.getName() + '"');
            }

            return result;
        }
    }

    @Override
    @Nonnull
    public Map<String, UniversalBlockMenu> loadUniversalInventories() {
        synchronized (lock) {
            Map<String, UniversalBlockMenu> result = new HashMap<>();

            try (PreparedStatement st = connection.prepareStatement("SELECT id, inv FROM universal_inventory");
                    ResultSet rs = st.executeQuery()) {

                while (rs.next()) {
                    String id = rs.getString("id");

                    try {
                        io.github.bakedlibs.dough.config.Config cfg = yamlToConfig(rs.getString("inv"));
                        BlockMenuPreset preset = BlockMenuPreset.getPreset(cfg.getString("preset"));

                        if (preset != null) {
                            result.put(preset.getID(), new UniversalBlockMenu(preset, cfg));
                        }
                    } catch (InvalidConfigurationException e) {
                        Slimefun.logger().log(Level.SEVERE, e, () -> "Could not parse universal Inventory \"" + id + '"');
                    }
                }
            } catch (SQLException e) {
                Slimefun.logger().log(Level.SEVERE, e, () -> "Could not load universal inventories from H2 storage");
            }

            return result;
        }
    }

    @Override
    @Nullable
    public BlockMenu loadInventoryIfPresent(@Nonnull Location l, @Nonnull BlockMenuPreset preset) {
        synchronized (lock) {
            try (PreparedStatement st = connection.prepareStatement(
                    "SELECT inv FROM block_inventory WHERE world = ? AND x = ? AND y = ? AND z = ?")) {
                st.setString(1, l.getWorld().getName());
                st.setInt(2, l.getBlockX());
                st.setInt(3, l.getBlockY());
                st.setInt(4, l.getBlockZ());

                try (ResultSet rs = st.executeQuery()) {
                    if (rs.next()) {
                        return new BlockMenu(preset, l, yamlToConfig(rs.getString("inv")));
                    }
                }
            } catch (SQLException | InvalidConfigurationException e) {
                Slimefun.logger().log(Level.SEVERE, e, () -> "Could not load Block Inventory at " + l);
            }

            return null;
        }
    }

    @Override
    public void flushBlocks(@Nonnull World world, @Nonnull Map<String, Config> blocksCache) {
        // Upsert-only: deletions are no longer inferred from the (delta-only) Config here, they
        // come exclusively via deleteBlocks(). An empty Config means nothing to do.
        synchronized (lock) {
            try {
                connection.setAutoCommit(false);

                try (PreparedStatement upsert = connection.prepareStatement(
                        "MERGE INTO block_data(world,x,y,z,sf_id,data) KEY(world,x,y,z) VALUES(?,?,?,?,?,?)")) {

                    for (Map.Entry<String, Config> entry : blocksCache.entrySet()) {
                        String sfId = entry.getKey();
                        Config cfg = entry.getValue();

                        for (String locationKey : cfg.getKeys()) {
                            String[] parts = CommonPatterns.SEMICOLON.split(locationKey);

                            if (parts.length != 4) {
                                continue;
                            }

                            String json = cfg.getString(locationKey);

                            if (json == null) {
                                // Setting a value to null removes the key from the underlying
                                // Config entirely, so this should not be reachable in practice.
                                // Skip defensively rather than upsert a null payload.
                                continue;
                            }

                            upsert.setString(1, world.getName());
                            upsert.setInt(2, Integer.parseInt(parts[1]));
                            upsert.setInt(3, Integer.parseInt(parts[2]));
                            upsert.setInt(4, Integer.parseInt(parts[3]));
                            upsert.setString(5, sfId);
                            upsert.setString(6, json);
                            upsert.executeUpdate();
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
    public void deleteBlocks(@Nonnull World world, @Nonnull Collection<Location> locations) {
        if (locations.isEmpty()) {
            return;
        }

        synchronized (lock) {
            try {
                connection.setAutoCommit(false);

                try (PreparedStatement delete = connection.prepareStatement(
                        "DELETE FROM block_data WHERE world = ? AND x = ? AND y = ? AND z = ?")) {

                    for (Location location : locations) {
                        delete.setString(1, world.getName());
                        delete.setInt(2, location.getBlockX());
                        delete.setInt(3, location.getBlockY());
                        delete.setInt(4, location.getBlockZ());
                        delete.executeUpdate();
                    }
                }

                connection.commit();
            } catch (SQLException e) {
                try {
                    connection.rollback();
                } catch (SQLException rollbackException) {
                    Slimefun.logger().log(Level.SEVERE, rollbackException, () -> "Could not roll back H2 block deletion");
                }

                Slimefun.logger().log(Level.SEVERE, e, () -> "Could not delete block data from H2 storage for world \"" + world.getName() + '"');
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
        // The map passed in is the full inventories snapshot, not just the dirty ones - guard
        // per-menu with isDirty() to match legacy's write-avoidance (BlockMenu.save() no-ops when
        // !isDirty()). Menus that were never opened/modified are skipped entirely.
        synchronized (lock) {
            try {
                connection.setAutoCommit(false);

                List<BlockMenu> flushed = new ArrayList<>();

                try (PreparedStatement upsert = connection.prepareStatement(
                        "MERGE INTO block_inventory(world,x,y,z,inv) KEY(world,x,y,z) VALUES(?,?,?,?,?)")) {

                    for (Map.Entry<Location, BlockMenu> entry : dirtyInventories.entrySet()) {
                        BlockMenu menu = entry.getValue();

                        if (!menu.isDirty()) {
                            continue;
                        }

                        Location location = entry.getKey();
                        upsert.setString(1, location.getWorld().getName());
                        upsert.setInt(2, location.getBlockX());
                        upsert.setInt(3, location.getBlockY());
                        upsert.setInt(4, location.getBlockZ());
                        upsert.setString(5, menuToYaml(menu));
                        upsert.executeUpdate();
                        flushed.add(menu);
                    }
                }

                connection.commit();

                // Only after the write is committed: mark the menus clean so they aren't needlessly
                // re-serialized every autosave. On a rollback below they stay dirty and retry next cycle.
                for (BlockMenu menu : flushed) {
                    menu.resetDirty();
                }
            } catch (SQLException e) {
                try {
                    connection.rollback();
                } catch (SQLException rollbackException) {
                    Slimefun.logger().log(Level.SEVERE, rollbackException, () -> "Could not roll back H2 inventory flush");
                }

                Slimefun.logger().log(Level.SEVERE, e, () -> "Could not flush block inventories to H2 storage");
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
    public void flushUniversalInventories(@Nonnull Map<String, UniversalBlockMenu> universalInventories) {
        synchronized (lock) {
            try {
                connection.setAutoCommit(false);

                List<UniversalBlockMenu> flushed = new ArrayList<>();

                try (PreparedStatement upsert = connection.prepareStatement(
                        "MERGE INTO universal_inventory(id,inv) KEY(id) VALUES(?,?)")) {

                    for (Map.Entry<String, UniversalBlockMenu> entry : universalInventories.entrySet()) {
                        UniversalBlockMenu menu = entry.getValue();

                        if (!menu.isDirty()) {
                            continue;
                        }

                        upsert.setString(1, entry.getKey());
                        upsert.setString(2, menuToYaml(menu));
                        upsert.executeUpdate();
                        flushed.add(menu);
                    }
                }

                connection.commit();

                for (UniversalBlockMenu menu : flushed) {
                    menu.resetDirty();
                }
            } catch (SQLException e) {
                try {
                    connection.rollback();
                } catch (SQLException rollbackException) {
                    Slimefun.logger().log(Level.SEVERE, rollbackException, () -> "Could not roll back H2 universal inventory flush");
                }

                Slimefun.logger().log(Level.SEVERE, e, () -> "Could not flush universal inventories to H2 storage");
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

    /**
     * Reads a single key from {@code storage_meta} (used by storage migrations for per-world flags).
     */
    @Nullable
    public String getMeta(@Nonnull String key) {
        synchronized (lock) {
            try (PreparedStatement st = connection.prepareStatement("SELECT v FROM storage_meta WHERE k = ?")) {
                st.setString(1, key);
                try (ResultSet rs = st.executeQuery()) {
                    return rs.next() ? rs.getString(1) : null;
                }
            } catch (SQLException e) {
                Slimefun.logger().log(Level.SEVERE, e, () -> "Could not read storage_meta key " + key);
                return null;
            }
        }
    }

    /**
     * Writes (upserts) a single key into {@code storage_meta}.
     */
    public void setMeta(@Nonnull String key, @Nonnull String value) {
        synchronized (lock) {
            try (PreparedStatement st = connection.prepareStatement("MERGE INTO storage_meta(k,v) KEY(k) VALUES(?,?)")) {
                st.setString(1, key);
                st.setString(2, value);
                st.executeUpdate();
            } catch (SQLException e) {
                Slimefun.logger().log(Level.SEVERE, e, () -> "Could not write storage_meta key " + key);
            }
        }
    }

    @Override
    public void deleteInventory(@Nonnull Location l) {
        synchronized (lock) {
            try (PreparedStatement delete = connection.prepareStatement(
                    "DELETE FROM block_inventory WHERE world = ? AND x = ? AND y = ? AND z = ?")) {
                delete.setString(1, l.getWorld().getName());
                delete.setInt(2, l.getBlockX());
                delete.setInt(3, l.getBlockY());
                delete.setInt(4, l.getBlockZ());
                delete.executeUpdate();
            } catch (SQLException e) {
                Slimefun.logger().log(Level.SEVERE, e, () -> "Could not delete Block Inventory at " + l);
            }
        }
    }
}
