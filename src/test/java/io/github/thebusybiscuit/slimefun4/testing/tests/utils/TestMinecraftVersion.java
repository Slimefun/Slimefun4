package io.github.thebusybiscuit.slimefun4.testing.tests.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.github.thebusybiscuit.slimefun4.api.MinecraftVersion;

class TestMinecraftVersion {

    @Test
    @DisplayName("Test if Minecraft versions match themselves")
    void testMatches() {
        Assertions.assertTrue(MinecraftVersion.MINECRAFT_1_14.matches("v1_14_R2"));
        Assertions.assertTrue(MinecraftVersion.MINECRAFT_1_15.matches("v1_15_R1"));

        Assertions.assertFalse(MinecraftVersion.MINECRAFT_1_15.matches("v1_14_R2"));
        Assertions.assertFalse(MinecraftVersion.MINECRAFT_1_14.matches("1.14.x"));
        Assertions.assertThrows(IllegalArgumentException.class, () -> MinecraftVersion.MINECRAFT_1_14.matches(null));
    }

    @Test
    @DisplayName("Test if Minecraft versions are ordered correctly (#atLeast)")
    void testAtLeast() {
        Assertions.assertTrue(MinecraftVersion.MINECRAFT_1_16.isAtLeast(MinecraftVersion.MINECRAFT_1_14));
        Assertions.assertTrue(MinecraftVersion.MINECRAFT_1_15.isAtLeast(MinecraftVersion.MINECRAFT_1_14));
        Assertions.assertTrue(MinecraftVersion.MINECRAFT_1_15.isAtLeast(MinecraftVersion.MINECRAFT_1_15));

        Assertions.assertFalse(MinecraftVersion.MINECRAFT_1_15.isAtLeast(MinecraftVersion.MINECRAFT_1_16));
    }

    @Test
    @DisplayName("Test correct behaviour for MinecraftVersion.UNKNOWN.isAtleast(...)")
    void testAtLeastUnknown() {
        // Unknown should always fall back to false
        Assertions.assertFalse(MinecraftVersion.UNKNOWN.isAtLeast(MinecraftVersion.MINECRAFT_1_14));
        Assertions.assertFalse(MinecraftVersion.UNKNOWN.isAtLeast(MinecraftVersion.MINECRAFT_1_15));

        Assertions.assertThrows(IllegalArgumentException.class, () -> MinecraftVersion.MINECRAFT_1_14.isAtLeast(null));
    }

    @Test
    @DisplayName("Test if Minecraft versions are ordered correctly (#isBefore)")
    void testIsBefore() {
        Assertions.assertTrue(MinecraftVersion.MINECRAFT_1_14.isBefore(MinecraftVersion.MINECRAFT_1_15));
        Assertions.assertTrue(MinecraftVersion.MINECRAFT_1_14.isBefore(MinecraftVersion.MINECRAFT_1_16));

        Assertions.assertFalse(MinecraftVersion.MINECRAFT_1_15.isBefore(MinecraftVersion.MINECRAFT_1_15));
        Assertions.assertFalse(MinecraftVersion.MINECRAFT_1_15.isBefore(MinecraftVersion.MINECRAFT_1_14));
    }

    @Test
    @DisplayName("Test correct behaviour for MinecraftVersion.UNKNOWN.isBefore(...)")
    void testIsBeforeUnknown() {
        // Unknown should always fall back to true
        Assertions.assertTrue(MinecraftVersion.UNKNOWN.isBefore(MinecraftVersion.MINECRAFT_1_14));
        Assertions.assertTrue(MinecraftVersion.UNKNOWN.isBefore(MinecraftVersion.MINECRAFT_1_15));

        Assertions.assertThrows(IllegalArgumentException.class, () -> MinecraftVersion.MINECRAFT_1_14.isBefore(null));
    }

}
