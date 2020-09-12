package io.github.thebusybiscuit.slimefun4.testing.tests.listeners;

import org.bukkit.entity.Player;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import io.github.thebusybiscuit.slimefun4.api.events.WaypointCreateEvent;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.implementation.listeners.DeathpointListener;
import io.github.thebusybiscuit.slimefun4.testing.TestUtilities;

class TestDeathpointListener {

    private static SlimefunPlugin plugin;
    private static ServerMock server;

    @BeforeAll
    public static void load() {
        server = MockBukkit.mock();
        plugin = MockBukkit.load(SlimefunPlugin.class);
        new DeathpointListener(plugin);
    }

    @AfterAll
    public static void unload() {
        MockBukkit.unmock();
    }

    @Test
    @DisplayName("Test Deathpoint not triggering when no Emergency Transmitter is found")
    void testNoTransmitter() throws InterruptedException {
        Player player = server.addPlayer();
        TestUtilities.awaitProfile(player);

        player.setHealth(0);
        Assertions.assertThrows(AssertionError.class, () -> server.getPluginManager().assertEventFired(WaypointCreateEvent.class, event -> event.getPlayer() == player && event.isDeathpoint()));
    }

    @Test
    @DisplayName("Test Emergency Transmitter creating a new Waypoint")
    void testTransmitter() throws InterruptedException {
        Player player = server.addPlayer();
        TestUtilities.awaitProfile(player);
        player.getInventory().setItem(8, SlimefunItems.GPS_EMERGENCY_TRANSMITTER.clone());

        player.setHealth(0);
        server.getPluginManager().assertEventFired(WaypointCreateEvent.class, event -> event.getPlayer() == player && event.isDeathpoint());
    }

}
