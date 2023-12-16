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

        setupResearches();
    }

    @AfterAll
    public static void unload() throws IOException {
        MockBukkit.unmock();
        FileUtils.deleteDirectory(dataFolder);
    }

    @Test
    void testLoadingBasicPlayerData() throws IOException {
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
            Assertions.assertTrue(data.getResearches().contains(Slimefun.getRegistry().getResearches().get(i).getKey()));
        }
    }

    @Test
    void testSavingBasicPlayerData() throws IOException, InterruptedException, ExecutionException {
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
            Assertions.assertTrue(assertion.getResearches().contains(Slimefun.getRegistry().getResearches().get(i).getKey()));
        }
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
