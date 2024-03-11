package io.github.thebusybiscuit.slimefun4.storage.backend;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.World.Environment;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import io.github.thebusybiscuit.slimefun4.api.gps.Waypoint;
import io.github.thebusybiscuit.slimefun4.api.player.PlayerProfile;
import io.github.thebusybiscuit.slimefun4.api.researches.Research;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.storage.backend.legacy.LegacyStorage;
import io.github.thebusybiscuit.slimefun4.storage.data.PlayerData;
import io.github.thebusybiscuit.slimefun4.test.TestUtilities;
import io.github.thebusybiscuit.slimefun4.utils.FileUtils;

class TestLegacyBackend {

    private static ServerMock server;
    private static Slimefun plugin;

    @BeforeAll
    public static void load() {
        server = MockBukkit.mock();
        plugin = MockBukkit.load(Slimefun.class);

        File playerFolder = new File("data-storage/Slimefun/Players");
        playerFolder.mkdirs();
        File waypointFolder = new File("data-storage/Slimefun/waypoints");
        waypointFolder.mkdirs();

        // Not too sure why this is needed, we don't use it elsewhere, it should just use the ItemStack serialization
        // My guess is MockBukkit isn't loading the ConfigurationSerialization class therefore the static block
        // within the class isn't being fired (where ItemStack and other classes are registered)
        ConfigurationSerialization.registerClass(ItemStack.class);
        ConfigurationSerialization.registerClass(ItemMeta.class);
    }

    @AfterAll
    public static void unload() throws IOException {
        MockBukkit.unmock();
        FileUtils.deleteDirectory(new File("data-storage"));
    }

    @AfterEach
    public void cleanup() {
        Slimefun.getRegistry().getResearches().clear();
    }

    // Test simple loading and saving of player data
    @Test
    void testLoadingResearches() throws IOException {
        setupResearches();

        // Create a player file which we can load
        UUID uuid = UUID.randomUUID();
        File playerFile = new File("data-storage/Slimefun/Players/" + uuid + ".yml");
        Files.writeString(playerFile.toPath(), """
        researches:
          '0': true
          '1': true
          '2': true
          '3': true
          '4': true
          '5': true
          '6': true
          '7': true
          '8': true
          '9': true
        """);

        // Load the player data
        LegacyStorage storage = new LegacyStorage();
        PlayerData data = storage.loadPlayerData(uuid);

        // Check if the data is correct
        Assertions.assertEquals(10, data.getResearches().size());
        for (int i = 0; i < 10; i++) {
            Assertions.assertTrue(data.getResearches().contains(Slimefun.getRegistry().getResearches().get(i)));
        }
    }

    // There's some issues with deserializing items in tests, I spent quite a while debugging this
    // and didn't really get anywhere. So commenting this out for now.
    /*
    @Test
    void testLoadingBackpacks() throws IOException {
        // Create a player file which we can load
        UUID uuid = UUID.randomUUID();
        File playerFile = new File("data-storage/Slimefun/Players/" + uuid + ".yml");
        Files.writeString(playerFile.toPath(), """
        backpacks:
          '0':
            size: 9
            contents:
              '0':
              ==: org.bukkit.inventory.ItemStack
              v: 1
              type: IRON_BLOCK
              meta:
              ==: org.bukkit.inventory.meta.ItemMeta
              enchants: {}
              damage: 0
              persistentDataContainer:
                slimefun:slimefun_item: TEST
              displayName: §6Test block
              itemFlags: !!set {}
              unbreakable: false
              repairCost: 0
        """);

        // Load the player data
        LegacyStorage storage = new LegacyStorage();
        PlayerData data = storage.loadPlayerData(uuid);

        // Check if the data is correct
        Assertions.assertEquals(1, data.getBackpacks().size());
        Assertions.assertEquals(9, data.getBackpacks().get(0).getSize());

        // Validate item deserialization
        System.out.println(
            Arrays.stream(data.getBackpack(0).getInventory().getContents())
                .map((item) -> item == null ? "null" : item.getType().name())
                .collect(Collectors.joining(", "))
        );
        ItemStack stack = data.getBackpack(0).getInventory().getItem(0);
        Assertions.assertNotNull(stack);
        Assertions.assertEquals("IRON_BLOCK", stack.getType().name());
        Assertions.assertEquals(1, stack.getAmount());
        Assertions.assertEquals(ChatColor.GREEN + "Test block", stack.getItemMeta().getDisplayName());
    }
     */

    @Test
    void testLoadingWaypoints() throws IOException {
        // Create mock world
        server.createWorld(WorldCreator.name("world").environment(Environment.NORMAL));

        // Create a player file which we can load
        UUID uuid = UUID.randomUUID();
        File waypointFile = new File("data-storage/Slimefun/waypoints/" + uuid + ".yml");
        Files.writeString(waypointFile.toPath(), """
        TEST:
          x: -173.0
          y: 75.0
          z: -11.0
          pitch: 0.0
          yaw: 178.0
          world: world
          name: test
        """);

        // Load the player data
        LegacyStorage storage = new LegacyStorage();
        PlayerData data = storage.loadPlayerData(uuid);

        // Check if the data is correct
        Assertions.assertEquals(1, data.getWaypoints().size());

        // Validate waypoint deserialization
        Waypoint waypoint = data.getWaypoints().iterator().next();

        Assertions.assertEquals("test", waypoint.getName());
        Assertions.assertEquals(-173.0, waypoint.getLocation().getX());
        Assertions.assertEquals(75.0, waypoint.getLocation().getY());
        Assertions.assertEquals(-11.0, waypoint.getLocation().getZ());
        Assertions.assertEquals(178.0, waypoint.getLocation().getYaw());
        Assertions.assertEquals(0.0, waypoint.getLocation().getPitch());
        Assertions.assertEquals("world", waypoint.getLocation().getWorld().getName());
    }

    @Test
    void testSavingResearches() throws InterruptedException {
        setupResearches();

        // Create a player file which we can load
        UUID uuid = UUID.randomUUID();
        File playerFile = new File("data-storage/Slimefun/Players/" + uuid + ".yml");

        OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);

        PlayerProfile profile = TestUtilities.awaitProfile(player);

        for (Research research : Slimefun.getRegistry().getResearches()) {
            profile.setResearched(research, true);
        }

        // Save the player data
        LegacyStorage storage = new LegacyStorage();
        storage.savePlayerData(uuid, profile.getPlayerData());

        // Assert the file exists and data is correct
        Assertions.assertTrue(playerFile.exists());
        PlayerData assertion = storage.loadPlayerData(uuid);
        Assertions.assertEquals(10, assertion.getResearches().size());
        for (int i = 0; i < 10; i++) {
            Assertions.assertTrue(assertion.getResearches().contains(Slimefun.getRegistry().getResearches().get(i)));
        }
    }

    // There's some issues with deserializing items in tests, I spent quite a while debugging this
    // and didn't really get anywhere. So commenting this out for now.
    /*
    @Test
    void testSavingBackpacks() throws InterruptedException {
        // Create a player file which we can load
        UUID uuid = UUID.randomUUID();
        File playerFile = new File("data-storage/Slimefun/Players/" + uuid + ".yml");

        OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);

       PlayerProfile profile = TestUtilities.awaitProfile(player);

        PlayerBackpack backpack = profile.createBackpack(9);
        backpack.getInventory().addItem(SlimefunItems.AIR_RUNE);

        // Save the player data
        LegacyStorage storage = new LegacyStorage();
        storage.savePlayerData(uuid, profile.getPlayerData());

        // Assert the file exists and data is correct
        Assertions.assertTrue(playerFile.exists());
        PlayerData assertion = storage.loadPlayerData(uuid);
        Assertions.assertEquals(1, assertion.getBackpacks().size());
    }
    */

    @Test
    void testSavingWaypoints() throws InterruptedException {
        // Create mock world
        World world = server.createWorld(WorldCreator.name("world").environment(Environment.NORMAL));

        // Create a player file which we can load
        UUID uuid = UUID.randomUUID();
        File playerFile = new File("data-storage/Slimefun/Players/" + uuid + ".yml");

        OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
        PlayerProfile profile = TestUtilities.awaitProfile(player);

        profile.addWaypoint(new Waypoint(
            player.getUniqueId(),
            "test",
            new Location(world, 1, 2, 3, 4, 5),
            ChatColor.GREEN + "Test waypoint")
        );

        // Save the player data
        LegacyStorage storage = new LegacyStorage();
        storage.savePlayerData(uuid, profile.getPlayerData());

        // Assert the file exists and data is correct
        Assertions.assertTrue(playerFile.exists());
        PlayerData assertion = storage.loadPlayerData(uuid);
        Assertions.assertEquals(1, assertion.getWaypoints().size());
    
        // Validate waypoint deserialization
        Waypoint waypoint = assertion.getWaypoints().iterator().next();

        Assertions.assertEquals(ChatColor.GREEN + "Test waypoint", waypoint.getName());
        Assertions.assertEquals(1, waypoint.getLocation().getX());
        Assertions.assertEquals(2, waypoint.getLocation().getY());
        Assertions.assertEquals(3, waypoint.getLocation().getZ());
        Assertions.assertEquals(4, waypoint.getLocation().getYaw());
        Assertions.assertEquals(5, waypoint.getLocation().getPitch());
        Assertions.assertEquals("world", waypoint.getLocation().getWorld().getName());
    }

    // Test realistic situations
    @Test
    void testResearchChanges() throws InterruptedException {
        setupResearches();

        UUID uuid = UUID.randomUUID();
        File playerFile = new File("data-storage/Slimefun/Players/" + uuid + ".yml");

        OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
        PlayerProfile profile = TestUtilities.awaitProfile(player);

        // Unlock all researches
        for (Research research : Slimefun.getRegistry().getResearches()) {
            profile.setResearched(research, true);
        }

        // Save the player data
        LegacyStorage storage = new LegacyStorage();
        storage.savePlayerData(uuid, profile.getPlayerData());

        // Assert the file exists and data is correct
        Assertions.assertTrue(playerFile.exists());
        PlayerData assertion = storage.loadPlayerData(uuid);
        Assertions.assertEquals(10, assertion.getResearches().size());
        for (int i = 0; i < 10; i++) {
            Assertions.assertTrue(assertion.getResearches().contains(Slimefun.getRegistry().getResearches().get(i)));
        }

        // Now let's change the data and save it again
        profile.setResearched(Slimefun.getRegistry().getResearches().get(3), false);

        // Save the player data
        storage.savePlayerData(uuid, profile.getPlayerData());

        // Assert the file exists and data is correct
        Assertions.assertTrue(playerFile.exists());
        System.out.println("update assertion");
        assertion = storage.loadPlayerData(uuid);
        Assertions.assertEquals(9, assertion.getResearches().size());
        for (int i = 0; i < 10; i++) {
            if (i != 3) {
                Assertions.assertTrue(assertion.getResearches().contains(Slimefun.getRegistry().getResearches().get(i)));
            }
        }
    }

    // Test realistic situations - when we fix the serialization issue
    // @Test
    // void testBackpackChanges() throws InterruptedException {}

    @Test
    void testWaypointChanges() throws InterruptedException {
        // Create mock world
        World world = server.createWorld(WorldCreator.name("world").environment(Environment.NORMAL));

        // Create a player file which we can load
        UUID uuid = UUID.randomUUID();
        File playerFile = new File("data-storage/Slimefun/Players/" + uuid + ".yml");

        OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
        PlayerProfile profile = TestUtilities.awaitProfile(player);

        profile.addWaypoint(new Waypoint(
            player.getUniqueId(),
            "test",
            new Location(world, 1, 2, 3, 4, 5),
            ChatColor.GREEN + "Test waypoint"
        ));

        Waypoint test2 = new Waypoint(
            player.getUniqueId(),
            "test2",
            new Location(world, 10, 20, 30, 40, 50),
            ChatColor.GREEN + "Test 2 waypoint"
        );
        profile.addWaypoint(test2);

        // Save the player data
        LegacyStorage storage = new LegacyStorage();
        storage.savePlayerData(uuid, profile.getPlayerData());

        // Assert the file exists and data is correct
        Assertions.assertTrue(playerFile.exists());
        PlayerData assertion = storage.loadPlayerData(uuid);
        Assertions.assertEquals(2, assertion.getWaypoints().size());

        // Remove one
        profile.removeWaypoint(test2);

        // Save the player data
        storage.savePlayerData(uuid, profile.getPlayerData());

        // Assert the file exists and data is correct
        Assertions.assertTrue(playerFile.exists());
        assertion = storage.loadPlayerData(uuid);
        Assertions.assertEquals(1, assertion.getWaypoints().size());
    }

    @Test
    void testDuplicateResearchesDontGetUnResearched() throws InterruptedException {
        // Create a player file which we can load
        UUID uuid = UUID.randomUUID();
        File playerFile = new File("data-storage/Slimefun/Players/" + uuid + ".yml");

        OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
        PlayerProfile profile = TestUtilities.awaitProfile(player);

        // Setup initial research
        NamespacedKey initialKey = new NamespacedKey(plugin, "test_1");
        Research initialResearch = new Research(initialKey, 1, "Test 1", 100);
        initialResearch.register();

        // Setup duplicate research
        // Keep the ID as 1 but change name and key
        NamespacedKey duplicateKey = new NamespacedKey(plugin, "test_2");
        Research duplicateResearch = new Research(duplicateKey, 1, "Test 2", 100);
        duplicateResearch.register();

        profile.setResearched(initialResearch, true);

        // Save the player data
        LegacyStorage storage = new LegacyStorage();
        storage.savePlayerData(uuid, profile.getPlayerData());

        // Assert the file exists and data is correct
        Assertions.assertTrue(playerFile.exists());
        PlayerData assertion = storage.loadPlayerData(uuid);
        // Will have both the initial and duplicate research
        Assertions.assertEquals(2, assertion.getResearches().size());
        Assertions.assertTrue(assertion.getResearches().contains(initialResearch));
        Assertions.assertTrue(assertion.getResearches().contains(duplicateResearch));
    }
    
    // Utils
    private static void setupResearches() {
        for (int i = 0; i < 10; i++) {
            NamespacedKey key = new NamespacedKey(plugin, "test_" + i);
            Research research = new Research(key, i, "Test " + i, 100);
            research.register();
        }
    }
}
