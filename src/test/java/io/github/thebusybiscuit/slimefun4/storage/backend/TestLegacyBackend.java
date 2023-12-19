package io.github.thebusybiscuit.slimefun4.storage.backend;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import be.seeseemelk.mockbukkit.MockBukkit;
import io.github.thebusybiscuit.slimefun4.api.player.PlayerProfile;
import io.github.thebusybiscuit.slimefun4.api.researches.Research;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.storage.backend.legacy.LegacyStorage;
import io.github.thebusybiscuit.slimefun4.storage.data.PlayerData;

class TestLegacyBackend {

    private static Slimefun plugin;
    private static File dataFolder;

    @BeforeAll
    public static void load() {
        MockBukkit.mock();
        plugin = MockBukkit.load(Slimefun.class);
        dataFolder = new File("data-storage/Slimefun/Players");
        dataFolder.mkdirs();

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
    void testSavingResearches() throws IOException, InterruptedException, ExecutionException {
        // Create a player file which we can load
        UUID uuid = UUID.randomUUID();
        File playerFile = new File("data-storage/Slimefun/Players/" + uuid + ".yml");

        OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);

        CompletableFuture<PlayerProfile> future = new CompletableFuture<PlayerProfile>();
        PlayerProfile.get(player, (profile) -> future.complete(profile));
        PlayerProfile profile = future.get();

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
    void testSavingBackpacks() throws IOException, InterruptedException, ExecutionException {
        // Create a player file which we can load
        UUID uuid = UUID.randomUUID();
        File playerFile = new File("data-storage/Slimefun/Players/" + uuid + ".yml");

        OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);

        CompletableFuture<PlayerProfile> future = new CompletableFuture<PlayerProfile>();
        PlayerProfile.get(player, (profile) -> future.complete(profile));
        PlayerProfile profile = future.get();

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

    // Utils
    private static void setupResearches() {
        for (int i = 0; i < 10; i++) {
            NamespacedKey key = new NamespacedKey(plugin, "test_" + i);
            Research research = new Research(key, i, "Test " + i, 100);
            research.register();
        }
    }
}
