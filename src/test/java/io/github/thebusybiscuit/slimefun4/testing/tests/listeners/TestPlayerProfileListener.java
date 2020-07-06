package io.github.thebusybiscuit.slimefun4.testing.tests.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import io.github.thebusybiscuit.slimefun4.api.player.PlayerProfile;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.implementation.listeners.PlayerProfileListener;
import io.github.thebusybiscuit.slimefun4.testing.TestUtilities;

public class TestPlayerProfileListener {

    private static SlimefunPlugin plugin;
    private static PlayerProfileListener listener;
    private static ServerMock server;

    @BeforeAll
    public static void load() {
        server = MockBukkit.mock();
        plugin = MockBukkit.load(SlimefunPlugin.class);
        listener = new PlayerProfileListener(plugin);
    }

    @AfterAll
    public static void unload() {
        MockBukkit.unmock();
    }

    @Test
    public void testPlayerLeave() throws InterruptedException {
        Player player = server.addPlayer();
        PlayerProfile profile = TestUtilities.awaitProfile(player);
        PlayerQuitEvent event = new PlayerQuitEvent(player, "bye");
        listener.onDisconnect(event);

        Assertions.assertTrue(profile.isMarkedForDeletion());
    }

    @Test
    public void testUnloadedPlayerLeave() {
        Player player = server.addPlayer();
        PlayerQuitEvent event = new PlayerQuitEvent(player, "bye");
        listener.onDisconnect(event);

        Assertions.assertFalse(PlayerProfile.find(player).isPresent());
    }

    @Test
    public void testPlayerKick() throws InterruptedException {
        Player player = server.addPlayer();
        PlayerProfile profile = TestUtilities.awaitProfile(player);
        PlayerKickEvent event = new PlayerKickEvent(player, "You're not welcome anymore", "bye");
        listener.onKick(event);

        Assertions.assertTrue(profile.isMarkedForDeletion());
    }

    @Test
    public void testUnloadedPlayerKick() {
        Player player = server.addPlayer();
        PlayerKickEvent event = new PlayerKickEvent(player, "You're not welcome anymore", "bye");
        listener.onKick(event);

        Assertions.assertFalse(PlayerProfile.find(player).isPresent());
    }

}
