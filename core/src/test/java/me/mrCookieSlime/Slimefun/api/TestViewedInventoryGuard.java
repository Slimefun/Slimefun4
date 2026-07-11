package me.mrCookieSlime.Slimefun.api;

import org.bukkit.Location;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockbukkit.mockbukkit.MockBukkit;
import org.mockbukkit.mockbukkit.ServerMock;
import org.mockbukkit.mockbukkit.world.WorldMock;

import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;

/**
 * Guards the viewed-inventory flag that {@link BlockStorage} exposes and the async {@code TickerTask}
 * reads to decide whether to run a machine's tick on the main thread. A machine currently open in front
 * of a player must be flagged so its (otherwise async) inventory writes are serialized with the player's
 * clicks - the fix for the async-tick-vs-player duplication race.
 */
class TestViewedInventoryGuard {

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

    private static Location loc() {
        WorldMock world = new WorldMock();
        server.addWorld(world);
        return new Location(world, 10, 64, 10);
    }

    @Test
    @DisplayName("A location is not viewed by default")
    void notViewedByDefault() {
        Assertions.assertFalse(BlockStorage.isInventoryViewed(loc()));
    }

    @Test
    @DisplayName("Marking a location viewed is observable, and unmarking clears it")
    void markAndUnmark() {
        Location l = loc();

        BlockStorage.setInventoryViewed(l, true);
        Assertions.assertTrue(BlockStorage.isInventoryViewed(l), "a viewed machine must be flagged so its tick runs on the main thread");

        BlockStorage.setInventoryViewed(l, false);
        Assertions.assertFalse(BlockStorage.isInventoryViewed(l), "once unviewed the machine may return to the async fast path");
    }

    @Test
    @DisplayName("The viewed flag is per-location")
    void perLocation() {
        Location a = loc();
        Location b = a.clone().add(1, 0, 0);

        BlockStorage.setInventoryViewed(a, true);
        Assertions.assertTrue(BlockStorage.isInventoryViewed(a));
        Assertions.assertFalse(BlockStorage.isInventoryViewed(b), "marking one machine must not flag its neighbour");

        BlockStorage.setInventoryViewed(a, false);
    }
}
