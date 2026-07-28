package io.github.thebusybiscuit.slimefun5.storage.backend.jdbc;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.WorldCreator;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import org.mockbukkit.mockbukkit.MockBukkit;
import org.mockbukkit.mockbukkit.ServerMock;

import io.github.thebusybiscuit.slimefun5.api.gps.Waypoint;
import io.github.thebusybiscuit.slimefun5.api.player.PlayerBackpack;
import io.github.thebusybiscuit.slimefun5.api.researches.Research;
import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun5.storage.data.PlayerData;
import io.github.thebusybiscuit.slimefun5.utils.FileUtils;

/**
 * Round-trip tests for the JDBC (H2) player-data store: backpack item serialization and waypoint
 * columns survive a save/load, and a save fully replaces the previous state.
 */
class JdbcStorageTest {

    private static ServerMock server;
    private static World world;

    @BeforeAll
    public static void load() {
        server = MockBukkit.mock();
        MockBukkit.load(Slimefun.class);
        world = server.createWorld(WorldCreator.name("world").environment(Environment.NORMAL));

        ConfigurationSerialization.registerClass(ItemStack.class);
        ConfigurationSerialization.registerClass(ItemMeta.class);
    }

    @AfterAll
    public static void unload() throws IOException {
        MockBukkit.unmock();
        FileUtils.deleteDirectory(new File("data-storage"));
    }

    @Test
    void testBackpackAndWaypointRoundTrip() {
        UUID uuid = UUID.randomUUID();
        JdbcStorage storage = new JdbcStorage("test", "jdbc:h2:mem:sf_players_rt;DB_CLOSE_DELAY=-1");

        try {
            HashMap<Integer, ItemStack> items = new HashMap<>();
            items.put(0, new ItemStack(Material.DIAMOND, 5));
            PlayerBackpack backpack = PlayerBackpack.load(uuid, 1, 9, items);

            Waypoint waypoint = new Waypoint(uuid, "wp1", new Location(world, 1, 64, 2, 30f, 15f), "Home");

            PlayerData data = new PlayerData(Collections.<Research>emptySet(),
                    Collections.singletonMap(1, backpack), Collections.singleton(waypoint));

            storage.savePlayerData(uuid, data);

            PlayerData loaded = storage.loadPlayerData(uuid);

            Assertions.assertTrue(loaded.getBackpacks().containsKey(1), "backpack must round-trip");
            PlayerBackpack loadedBackpack = loaded.getBackpack(1);
            Assertions.assertEquals(9, loadedBackpack.getSize());
            Assertions.assertEquals(new ItemStack(Material.DIAMOND, 5), loadedBackpack.getInventory().getItem(0));

            Assertions.assertEquals(1, loaded.getWaypoints().size(), "waypoint must round-trip");
            Waypoint loadedWaypoint = loaded.getWaypoints().iterator().next();
            Assertions.assertEquals("wp1", loadedWaypoint.getId());
            Assertions.assertEquals("Home", loadedWaypoint.getName());
            Assertions.assertEquals(world, loadedWaypoint.getLocation().getWorld());
            Assertions.assertEquals(1, loadedWaypoint.getLocation().getBlockX());
            Assertions.assertEquals(64, loadedWaypoint.getLocation().getBlockY());
            Assertions.assertEquals(2, loadedWaypoint.getLocation().getBlockZ());
        } finally {
            storage.close();
        }
    }

    @Test
    void testSaveFullyReplacesPreviousState() {
        UUID uuid = UUID.randomUUID();
        JdbcStorage storage = new JdbcStorage("test", "jdbc:h2:mem:sf_players_replace;DB_CLOSE_DELAY=-1");

        try {
            PlayerBackpack backpack = PlayerBackpack.load(uuid, 1, 9, new HashMap<>());
            Waypoint waypoint = new Waypoint(uuid, "wp1", new Location(world, 1, 64, 2), "Home");
            storage.savePlayerData(uuid, new PlayerData(Collections.<Research>emptySet(),
                    Collections.singletonMap(1, backpack), Collections.singleton(waypoint)));

            // A subsequent save with empty data must drop the previously-stored backpack and waypoint.
            storage.savePlayerData(uuid, new PlayerData(new HashSet<>(),
                    new HashMap<>(), new HashSet<>()));

            PlayerData loaded = storage.loadPlayerData(uuid);
            Assertions.assertTrue(loaded.getBackpacks().isEmpty(), "delete-then-insert must clear old backpacks");
            Assertions.assertTrue(loaded.getWaypoints().isEmpty(), "delete-then-insert must clear old waypoints");
        } finally {
            storage.close();
        }
    }

    @Test
    void testLoadUnknownPlayerReturnsEmptyData() {
        JdbcStorage storage = new JdbcStorage("test", "jdbc:h2:mem:sf_players_unknown;DB_CLOSE_DELAY=-1");

        try {
            PlayerData loaded = storage.loadPlayerData(UUID.randomUUID());
            Assertions.assertTrue(loaded.getResearches().isEmpty());
            Assertions.assertTrue(loaded.getBackpacks().isEmpty());
            Assertions.assertTrue(loaded.getWaypoints().isEmpty());
        } finally {
            storage.close();
        }
    }
}
