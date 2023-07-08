package io.github.thebusybiscuit.slimefun4.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.github.thebusybiscuit.slimefun4.api.MinecraftVersion;

class TestMinecraftVersion {

    @Test
    @DisplayName("Test if Minecraft versions match themselves")
    void testMatches() {
        Assertions.assertTrue(MinecraftVersion.MINECRAFT_1_16.isMinecraftVersion(16));
        Assertions.assertTrue(MinecraftVersion.MINECRAFT_1_17.isMinecraftVersion(17));

        Assertions.assertFalse(MinecraftVersion.MINECRAFT_1_17.isMinecraftVersion(16));
        Assertions.assertFalse(MinecraftVersion.MINECRAFT_1_16.isMinecraftVersion(0));
    }

    @Test
    @DisplayName("Test if Minecraft versions are ordered correctly (#atLeast)")
    void testAtLeast() {
        Assertions.assertTrue(MinecraftVersion.MINECRAFT_1_18.isAtLeast(MinecraftVersion.MINECRAFT_1_16));
        Assertions.assertTrue(MinecraftVersion.MINECRAFT_1_17.isAtLeast(MinecraftVersion.MINECRAFT_1_16));
        Assertions.assertTrue(MinecraftVersion.MINECRAFT_1_17.isAtLeast(MinecraftVersion.MINECRAFT_1_17));

        Assertions.assertFalse(MinecraftVersion.MINECRAFT_1_17.isAtLeast(MinecraftVersion.MINECRAFT_1_18));
    }

    @Test
    @DisplayName("Test correct behaviour for MinecraftVersion.UNKNOWN.isAtleast(...)")
    void testAtLeastUnknown() {
        // Unknown should always fall back to false
        Assertions.assertFalse(MinecraftVersion.UNKNOWN.isAtLeast(MinecraftVersion.MINECRAFT_1_16));
        Assertions.assertFalse(MinecraftVersion.UNKNOWN.isAtLeast(MinecraftVersion.MINECRAFT_1_17));

        Assertions.assertThrows(IllegalArgumentException.class, () -> MinecraftVersion.MINECRAFT_1_16.isAtLeast(null));
    }

    @Test
    @DisplayName("Test if Minecraft versions are ordered correctly (#isBefore)")
    void testIsBefore() {
        Assertions.assertTrue(MinecraftVersion.MINECRAFT_1_16.isBefore(MinecraftVersion.MINECRAFT_1_17));
        Assertions.assertTrue(MinecraftVersion.MINECRAFT_1_16.isBefore(MinecraftVersion.MINECRAFT_1_18));

        Assertions.assertFalse(MinecraftVersion.MINECRAFT_1_17.isBefore(MinecraftVersion.MINECRAFT_1_17));
        Assertions.assertFalse(MinecraftVersion.MINECRAFT_1_17.isBefore(MinecraftVersion.MINECRAFT_1_16));
    }

    @Test
    @DisplayName("Test correct behaviour for MinecraftVersion.UNKNOWN.isBefore(...)")
    void testIsBeforeUnknown() {
        // Unknown should always fall back to true
        Assertions.assertTrue(MinecraftVersion.UNKNOWN.isBefore(MinecraftVersion.MINECRAFT_1_16));
        Assertions.assertTrue(MinecraftVersion.UNKNOWN.isBefore(MinecraftVersion.MINECRAFT_1_17));

        Assertions.assertThrows(IllegalArgumentException.class, () -> MinecraftVersion.MINECRAFT_1_16.isBefore(null));
    }

    @Test
    @DisplayName("Test warning system for lowest supported version checks")
    void testLowestSupportedVersion() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> MinecraftVersion.UNIT_TEST.isAtLeast(MinecraftVersion.MINECRAFT_1_16));
    }

}
