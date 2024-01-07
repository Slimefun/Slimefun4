package io.github.thebusybiscuit.slimefun4.api.profiles;

import java.util.Optional;

import org.bukkit.entity.Player;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.github.thebusybiscuit.slimefun4.api.player.PlayerBackpack;
import io.github.thebusybiscuit.slimefun4.api.player.PlayerProfile;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.test.TestUtilities;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;

class TestPlayerBackpacks {

    private static ServerMock server;

    @BeforeAll
    public static void load() {
        server = MockBukkit.mock();
        MockBukkit.load(Slimefun.class);
    }

    @AfterAll
    public static void unload() {
        MockBukkit.unmock();
    }

    @Test
    @DisplayName("Test creating a new Player Backpack")
    void testCreateBackpack() throws InterruptedException {
        Player player = server.addPlayer();
        PlayerProfile profile = TestUtilities.awaitProfile(player);

        PlayerBackpack backpack = profile.createBackpack(18);

        Assertions.assertNotNull(backpack);

        Assertions.assertEquals(player.getUniqueId(), backpack.getOwnerId());
        Assertions.assertEquals(18, backpack.getSize());
        Assertions.assertEquals(18, backpack.getInventory().getSize());
    }

    @Test
    @DisplayName("Test creating a new backpack will increment the id")
    void testCreateBackpackIncrementsId() throws InterruptedException {
        Player player = server.addPlayer();
        PlayerProfile profile = TestUtilities.awaitProfile(player);

        PlayerBackpack backpackOne = profile.createBackpack(18);
        PlayerBackpack backpackTwo = profile.createBackpack(18);
        PlayerBackpack backpackThree = profile.createBackpack(18);

        Assertions.assertEquals(0, backpackOne.getId());
        Assertions.assertEquals(1, backpackTwo.getId());
        Assertions.assertEquals(2, backpackThree.getId());
    }

    @Test
    @DisplayName("Test upgrading the backpack size")
    void testChangeSize() throws InterruptedException {
        Player player = server.addPlayer();
        PlayerProfile profile = TestUtilities.awaitProfile(player);
        PlayerBackpack backpack = profile.createBackpack(9);

        Assertions.assertThrows(IllegalArgumentException.class, () -> profile.createBackpack(-9));
        Assertions.assertThrows(IllegalArgumentException.class, () -> profile.createBackpack(12));
        Assertions.assertThrows(IllegalArgumentException.class, () -> profile.createBackpack(3000));

        Assertions.assertThrows(IllegalArgumentException.class, () -> backpack.setSize(-9));
        Assertions.assertThrows(IllegalArgumentException.class, () -> backpack.setSize(12));
        Assertions.assertThrows(IllegalArgumentException.class, () -> backpack.setSize(3000));

        backpack.setSize(27);

        Assertions.assertEquals(27, backpack.getSize());
    }

    @Test
    @DisplayName("Test getting a backpack by its id")
    void testGetBackpackById() throws InterruptedException {
        Player player = server.addPlayer();
        PlayerProfile profile = TestUtilities.awaitProfile(player);
        PlayerBackpack backpack = profile.createBackpack(9);
        int id = backpack.getId();

        Assertions.assertThrows(IllegalArgumentException.class, () -> profile.getBackpack(-20));

        Optional<PlayerBackpack> optional = profile.getBackpack(id);
        Assertions.assertTrue(optional.isPresent());
        Assertions.assertEquals(backpack, optional.get());

        Assertions.assertFalse(profile.getBackpack(500).isPresent());
    }
}
