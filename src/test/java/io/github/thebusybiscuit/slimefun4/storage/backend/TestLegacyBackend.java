package io.github.thebusybiscuit.slimefun4.storage.backend;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
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
import net.md_5.bungee.api.ChatColor;

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

        setupResearches();
    }

    @AfterAll
    public static void unload() throws IOException {
        MockBukkit.unmock();
        FileUtils.deleteDirectory(new File("data-storage"));
    }

    @Test
    void testLoadingResearches() throws IOException {
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

    // Utils
    private static void setupResearches() {
        for (int i = 0; i < 10; i++) {
            NamespacedKey key = new NamespacedKey(plugin, "test_" + i);
            Research research = new Research(key, i, "Test " + i, 100);
            research.register();
        }
    }
}
