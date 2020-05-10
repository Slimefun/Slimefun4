package io.github.thebusybiscuit.slimefun4.tests.profiles;

import java.util.Optional;

import org.bukkit.entity.Player;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import io.github.thebusybiscuit.slimefun4.api.player.PlayerBackpack;
import io.github.thebusybiscuit.slimefun4.api.player.PlayerProfile;
import io.github.thebusybiscuit.slimefun4.mocks.TestUtilities;

public class TestPlayerBackpacks {

    private static ServerMock server;

    @BeforeAll
    public static void load() {
        server = MockBukkit.mock();
    }

    @AfterAll
    public static void unload() {
        MockBukkit.unmock();
    }

    @Test
    public void testCreateBackpack() throws InterruptedException {
        Player player = server.addPlayer();
        PlayerProfile profile = TestUtilities.awaitProfile(player);
        Assertions.assertFalse(profile.isDirty());

        PlayerBackpack backpack = profile.createBackpack(18);

        Assertions.assertNotNull(backpack);

        // Creating a backpack should mark profiles as dirty
        Assertions.assertTrue(profile.isDirty());

        Assertions.assertEquals(profile, backpack.getOwner());
        Assertions.assertEquals(18, backpack.getSize());
        Assertions.assertEquals(18, backpack.getInventory().getSize());
    }

    @Test
    public void testChangeSize() throws InterruptedException {
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
        Assertions.assertTrue(profile.isDirty());
    }

    @Test
    public void testGetBackpackById() throws InterruptedException {
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
