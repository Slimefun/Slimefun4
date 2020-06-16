package io.github.thebusybiscuit.slimefun4.testing.tests.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.github.thebusybiscuit.slimefun4.api.MinecraftVersion;

public class TestMinecraftVersion {

    @Test
    public void testMatches() {
        Assertions.assertTrue(MinecraftVersion.MINECRAFT_1_13.matches("v1_13_R1"));
        Assertions.assertTrue(MinecraftVersion.MINECRAFT_1_14.matches("v1_14_R2"));

        Assertions.assertFalse(MinecraftVersion.MINECRAFT_1_13.matches("v1_14_R2"));
        Assertions.assertFalse(MinecraftVersion.MINECRAFT_1_14.matches("1.14.x"));
        Assertions.assertThrows(IllegalArgumentException.class, () -> MinecraftVersion.MINECRAFT_1_14.matches(null));
    }

    @Test
    public void testAtLeast() {
        Assertions.assertTrue(MinecraftVersion.MINECRAFT_1_15.isAtLeast(MinecraftVersion.MINECRAFT_1_13));
        Assertions.assertTrue(MinecraftVersion.MINECRAFT_1_15.isAtLeast(MinecraftVersion.MINECRAFT_1_14));
        Assertions.assertTrue(MinecraftVersion.MINECRAFT_1_15.isAtLeast(MinecraftVersion.MINECRAFT_1_15));

        Assertions.assertFalse(MinecraftVersion.MINECRAFT_1_13.isAtLeast(MinecraftVersion.MINECRAFT_1_14));
    }

    @Test
    public void testAtLeastUnknown() {
        // Unknown should always fall back to false
        Assertions.assertFalse(MinecraftVersion.UNKNOWN.isAtLeast(MinecraftVersion.MINECRAFT_1_13));
        Assertions.assertFalse(MinecraftVersion.UNKNOWN.isAtLeast(MinecraftVersion.MINECRAFT_1_15));

        Assertions.assertThrows(IllegalArgumentException.class, () -> MinecraftVersion.MINECRAFT_1_14.isAtLeast(null));
    }

    @Test
    public void testIsBefore() {
        Assertions.assertTrue(MinecraftVersion.MINECRAFT_1_13.isBefore(MinecraftVersion.MINECRAFT_1_14));
        Assertions.assertTrue(MinecraftVersion.MINECRAFT_1_13.isBefore(MinecraftVersion.MINECRAFT_1_15));
        Assertions.assertTrue(MinecraftVersion.MINECRAFT_1_14.isBefore(MinecraftVersion.MINECRAFT_1_15));

        Assertions.assertFalse(MinecraftVersion.MINECRAFT_1_15.isBefore(MinecraftVersion.MINECRAFT_1_15));
        Assertions.assertFalse(MinecraftVersion.MINECRAFT_1_15.isBefore(MinecraftVersion.MINECRAFT_1_13));
    }

    @Test
    public void testIsBeforeUnknown() {
        // Unknown should always fall back to true
        Assertions.assertTrue(MinecraftVersion.UNKNOWN.isBefore(MinecraftVersion.MINECRAFT_1_13));
        Assertions.assertTrue(MinecraftVersion.UNKNOWN.isBefore(MinecraftVersion.MINECRAFT_1_15));

        Assertions.assertThrows(IllegalArgumentException.class, () -> MinecraftVersion.MINECRAFT_1_14.isBefore(null));
    }

}
