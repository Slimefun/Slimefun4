package io.github.thebusybiscuit.slimefun5.storage.backend.jdbc;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.google.common.annotations.Beta;

import io.github.bakedlibs.dough.config.Config;
import io.github.thebusybiscuit.slimefun5.api.gps.Waypoint;
import io.github.thebusybiscuit.slimefun5.api.player.PlayerBackpack;
import io.github.thebusybiscuit.slimefun5.api.researches.Research;
import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun5.storage.Storage;
import io.github.thebusybiscuit.slimefun5.storage.data.PlayerData;

/**
 * JDBC (H2 / MySQL) implementation of {@link Storage} for player data - researches, backpacks and
 * waypoints - normalized into the {@code player_research}/{@code player_backpack}/{@code
 * player_waypoint} tables. Mirrors the shape of {@link JdbcBackend} (single serialized connection
 * behind a lock) but is deliberately self-contained: it owns its own {@link ConnectionProvider}
 * rather than sharing the block store's, because embedded H2 permits only one connection per
 * database file per JVM.
 *
 * <p>
 * Each {@link #savePlayerData(UUID, PlayerData)} fully replaces that player's rows in a single
 * transaction (delete-then-insert), so removals - a locked research, a deleted backpack or
 * waypoint - are handled without a separate delete path.
 *
 * <p>
 * <b>This API is still experimental, it may change without notice.</b>
 */
@Beta
@ThreadSafe
public class JdbcStorage implements Storage {

    // Never touches the filesystem: dough Config only writes in save(), which we never call. The
    // in-memory YamlConfiguration paired with it is the actual (de)serialization holder for backpacks.
    private static final File DUMMY_FILE = new File("");

    private final String label;
    private final ConnectionProvider provider;
    private final Object lock = new Object();

    /** Embedded-H2 convenience: opens the player-data H2 file at {@code jdbcUrl}. */
    public JdbcStorage(@Nonnull String label, @Nonnull String jdbcUrl) {
        this(label, new H2Dialect(), new EmbeddedH2Provider(jdbcUrl));
    }

    public JdbcStorage(@Nonnull String label, @Nonnull SqlDialect dialect, @Nonnull ConnectionProvider provider) {
        this.label = label;
        this.provider = provider;

        synchronized (lock) {
            try {
                createSchema(provider.conn(), dialect);
            } catch (SQLException e) {
                throw new IllegalStateException("Could not initialize player-data storage schema", e);
            }
        }
    }

    private void createSchema(@Nonnull Connection c, @Nonnull SqlDialect dialect) throws SQLException {
        try (Statement st = c.createStatement()) {
            for (String stmt : dialect.playerDdl()) {
                // Every player DDL statement is CREATE TABLE IF NOT EXISTS (no bare indexes), so it is
                // idempotent on both H2 and MySQL; a failure here is a genuine misconfiguration.
                st.execute(stmt);
            }
        }
    }

    @Override
    public PlayerData loadPlayerData(@Nonnull UUID uuid) {
        long start = System.nanoTime();

        synchronized (lock) {
            Set<Research> researches = loadResearches(uuid);
            HashMap<Integer, PlayerBackpack> backpacks = loadBackpacks(uuid);
            Set<Waypoint> waypoints = loadWaypoints(uuid);

            Slimefun.getAnalyticsService().recordPlayerProfileDataTime(label, true, System.nanoTime() - start);
            return new PlayerData(researches, backpacks, waypoints);
        }
    }

    @Nonnull
    private Set<Research> loadResearches(@Nonnull UUID uuid) {
        Set<Integer> unlockedIds = new HashSet<>();

        try {
            Connection connection = provider.conn();

            try (PreparedStatement st = connection.prepareStatement("SELECT research_id FROM player_research WHERE uuid = ?")) {
                st.setString(1, uuid.toString());

                try (ResultSet rs = st.executeQuery()) {
                    while (rs.next()) {
                        unlockedIds.add(rs.getInt("research_id"));
                    }
                }
            }
        } catch (SQLException e) {
            Slimefun.logger().log(Level.SEVERE, e, () -> "Could not load researches for Player \"" + uuid + '"');
        }

        // Map ids back to the registered Research singletons, mirroring LegacyStorage (which keys by
        // Research#getID and thus also unlocks BOTH researches sharing a duplicate id).
        Set<Research> researches = new HashSet<>();

        for (Research research : Slimefun.getRegistry().getResearches()) {
            if (unlockedIds.contains(research.getID())) {
                researches.add(research);
            }
        }

        return researches;
    }

    @Nonnull
    private HashMap<Integer, PlayerBackpack> loadBackpacks(@Nonnull UUID uuid) {
        HashMap<Integer, PlayerBackpack> backpacks = new HashMap<>();

        try {
            Connection connection = provider.conn();

            try (PreparedStatement st = connection.prepareStatement("SELECT backpack_id, size, contents FROM player_backpack WHERE uuid = ?")) {
                st.setString(1, uuid.toString());

                try (ResultSet rs = st.executeQuery()) {
                    while (rs.next()) {
                        int id = rs.getInt("backpack_id");
                        int size = rs.getInt("size");

                        try {
                            HashMap<Integer, ItemStack> items = deserializeContents(rs.getString("contents"), size, uuid, id);
                            backpacks.put(id, PlayerBackpack.load(uuid, id, size, items));
                        } catch (Exception itemError) {
                            Slimefun.logger().log(Level.WARNING, itemError, () -> "Could not load Backpack \"" + id + "\" for Player \"" + uuid + '"');
                        }
                    }
                }
            }
        } catch (SQLException e) {
            Slimefun.logger().log(Level.SEVERE, e, () -> "Could not load backpacks for Player \"" + uuid + '"');
        }

        return backpacks;
    }

    @Nonnull
    private HashMap<Integer, ItemStack> deserializeContents(@Nonnull String clob, int size, @Nonnull UUID uuid, int backpackId) {
        HashMap<Integer, ItemStack> items = new HashMap<>();
        YamlConfiguration yaml = new YamlConfiguration();

        try {
            yaml.loadFromString(clob);
        } catch (org.bukkit.configuration.InvalidConfigurationException e) {
            Slimefun.logger().log(Level.WARNING, e, () -> "Could not parse contents of Backpack \"" + backpackId + "\" for Player \"" + uuid + '"');
            return items;
        }

        Config cfg = new Config(DUMMY_FILE, yaml);

        for (int slot = 0; slot < size; slot++) {
            String key = String.valueOf(slot);

            if (cfg.contains(key)) {
                try {
                    items.put(slot, cfg.getItem(key));
                } catch (Exception itemError) {
                    // A single un-deserializable item must not cost the whole backpack - drop just that slot.
                    final int lostSlot = slot;
                    Slimefun.logger().log(Level.WARNING, itemError, () -> "Skipped an unreadable item in backpack \"" + backpackId + "\" slot " + lostSlot + " for Player \"" + uuid + '"');
                }
            }
        }

        return items;
    }

    @Nonnull
    private Set<Waypoint> loadWaypoints(@Nonnull UUID uuid) {
        Set<Waypoint> waypoints = new HashSet<>();

        try {
            Connection connection = provider.conn();

            try (PreparedStatement st = connection.prepareStatement("SELECT waypoint_id, name, world, x, y, z, yaw, pitch FROM player_waypoint WHERE uuid = ?")) {
                st.setString(1, uuid.toString());

                try (ResultSet rs = st.executeQuery()) {
                    while (rs.next()) {
                        String worldName = rs.getString("world");
                        World world = Bukkit.getWorld(worldName);

                        // Match LegacyStorage: skip waypoints whose world is not loaded.
                        if (world == null) {
                            continue;
                        }

                        String id = rs.getString("waypoint_id");
                        String name = rs.getString("name");
                        Location loc = new Location(world, rs.getDouble("x"), rs.getDouble("y"), rs.getDouble("z"), rs.getFloat("yaw"), rs.getFloat("pitch"));
                        waypoints.add(new Waypoint(uuid, id, loc, name));
                    }
                }
            }
        } catch (SQLException e) {
            Slimefun.logger().log(Level.SEVERE, e, () -> "Could not load waypoints for Player \"" + uuid + '"');
        }

        return waypoints;
    }

    @Override
    public void savePlayerData(@Nonnull UUID uuid, @Nonnull PlayerData data) {
        long start = System.nanoTime();

        synchronized (lock) {
            Connection connection;

            try {
                connection = provider.conn();
            } catch (SQLException e) {
                Slimefun.logger().log(Level.SEVERE, e, () -> "Could not save player data for \"" + uuid + '"');
                return;
            }

            boolean committed = false;

            try {
                connection.setAutoCommit(false);

                deleteExisting(connection, uuid);
                saveResearches(connection, uuid, data);
                saveBackpacks(connection, uuid, data);
                saveWaypoints(connection, uuid, data);

                connection.commit();
                committed = true;
            } catch (SQLException e) {
                Slimefun.logger().log(Level.SEVERE, e, () -> "Could not save player data for \"" + uuid + '"');
            } finally {
                if (!committed) {
                    try {
                        connection.rollback();
                    } catch (SQLException rollbackException) {
                        Slimefun.logger().log(Level.SEVERE, rollbackException, () -> "Could not roll back the player-data save for \"" + uuid + '"');
                    }
                }

                try {
                    connection.setAutoCommit(true);
                } catch (SQLException e) {
                    Slimefun.logger().log(Level.WARNING, e, () -> "Could not restore auto-commit on the player-data connection");
                }
            }

            Slimefun.getAnalyticsService().recordPlayerProfileDataTime(label, false, System.nanoTime() - start);
        }
    }

    private void deleteExisting(@Nonnull Connection connection, @Nonnull UUID uuid) throws SQLException {
        for (String table : new String[] { "player_research", "player_backpack", "player_waypoint" }) {
            try (PreparedStatement st = connection.prepareStatement("DELETE FROM " + table + " WHERE uuid = ?")) {
                st.setString(1, uuid.toString());
                st.executeUpdate();
            }
        }
    }

    private void saveResearches(@Nonnull Connection connection, @Nonnull UUID uuid, @Nonnull PlayerData data) throws SQLException {
        // Distinct ids: a duplicate research id (shared by two Research objects) is stored once.
        Set<Integer> ids = new HashSet<>();

        for (Research research : data.getResearches()) {
            ids.add(research.getID());
        }

        try (PreparedStatement st = connection.prepareStatement("INSERT INTO player_research(uuid, research_id) VALUES(?, ?)")) {
            for (int id : ids) {
                st.setString(1, uuid.toString());
                st.setInt(2, id);
                st.executeUpdate();
            }
        }
    }

    private void saveBackpacks(@Nonnull Connection connection, @Nonnull UUID uuid, @Nonnull PlayerData data) throws SQLException {
        try (PreparedStatement st = connection.prepareStatement("INSERT INTO player_backpack(uuid, backpack_id, size, contents) VALUES(?, ?, ?, ?)")) {
            for (PlayerBackpack backpack : data.getBackpacks().values()) {
                st.setString(1, uuid.toString());
                st.setInt(2, backpack.getId());
                st.setInt(3, backpack.getSize());
                st.setString(4, serializeContents(backpack));
                st.executeUpdate();
            }
        }
    }

    @Nonnull
    private String serializeContents(@Nonnull PlayerBackpack backpack) {
        YamlConfiguration yaml = new YamlConfiguration();
        Config cfg = new Config(DUMMY_FILE, yaml);
        Inventory inventory = backpack.getInventory();

        for (int slot = 0; slot < backpack.getSize(); slot++) {
            ItemStack item = inventory.getItem(slot);

            if (item != null) {
                cfg.setValue(String.valueOf(slot), item);
            }
        }

        return yaml.saveToString();
    }

    private void saveWaypoints(@Nonnull Connection connection, @Nonnull UUID uuid, @Nonnull PlayerData data) throws SQLException {
        try (PreparedStatement st = connection.prepareStatement("INSERT INTO player_waypoint(uuid, waypoint_id, name, world, x, y, z, yaw, pitch) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)")) {
            for (Waypoint waypoint : data.getWaypoints()) {
                Location loc = waypoint.getLocation();

                if (loc == null || loc.getWorld() == null) {
                    continue;
                }

                st.setString(1, uuid.toString());
                st.setString(2, waypoint.getId());
                st.setString(3, waypoint.getName());
                st.setString(4, loc.getWorld().getName());
                st.setDouble(5, loc.getX());
                st.setDouble(6, loc.getY());
                st.setDouble(7, loc.getZ());
                st.setFloat(8, loc.getYaw());
                st.setFloat(9, loc.getPitch());
                st.executeUpdate();
            }
        }
    }

    @Override
    public void close() {
        synchronized (lock) {
            provider.close();
        }
    }
}
