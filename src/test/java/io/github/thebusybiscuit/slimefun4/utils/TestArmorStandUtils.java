package io.github.thebusybiscuit.slimefun4.utils;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.WorldMock;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TestArmorStandUtils {

    private static World world;

    @BeforeAll
    public static void load() {
        MockBukkit.mock();
        world = new WorldMock();
    }

    @AfterAll
    public static void unload() {
        MockBukkit.unmock();
    }

    @Test
    @DisplayName("Test Spawn Armor Stand")
    void TestSummonFakeHologram() {
        // Make sure it's in the right place
        Location testLocation = new Location(world, 100, 100, 100);
        ArmorStand output = ArmorStandUtils.spawnFakeHologram(testLocation);
        Assertions.assertEquals(testLocation, output.getLocation());

        // Make sure all values are set Properly
        Assertions.assertFalse(output.isVisible());
        Assertions.assertTrue(output.isSilent());
        Assertions.assertTrue(output.isMarker());
        Assertions.assertFalse(output.hasGravity());
        Assertions.assertFalse(output.hasBasePlate());
        Assertions.assertTrue(output.isCustomNameVisible());
        Assertions.assertFalse(output.getRemoveWhenFarAway());

        // Test Exceptions
        Assertions.assertThrows(IllegalArgumentException.class, () -> ArmorStandUtils.spawnFakeHologram(null));
    }
}
