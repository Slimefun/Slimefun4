package io.github.thebusybiscuit.slimefun4.testing.tests.gps;

import org.bukkit.entity.Player;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import io.github.thebusybiscuit.slimefun4.api.events.WaypointCreateEvent;
import io.github.thebusybiscuit.slimefun4.api.gps.GPSNetwork;
import io.github.thebusybiscuit.slimefun4.api.gps.Waypoint;
import io.github.thebusybiscuit.slimefun4.api.player.PlayerProfile;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.testing.TestUtilities;

public class TestWaypoints {

    private static ServerMock server;

    @BeforeAll
    public static void load() {
        server = MockBukkit.mock();
        MockBukkit.load(SlimefunPlugin.class);
    }

    @AfterAll
    public static void unload() {
        MockBukkit.unmock();
    }

    @Test
    public void testAddWaypointToProfile() throws InterruptedException {
        Player player = server.addPlayer();
        PlayerProfile profile = TestUtilities.awaitProfile(player);

        Assertions.assertTrue(profile.getWaypoints().isEmpty());
        Waypoint waypoint = new Waypoint(profile, "hello", player.getLocation(), "HELLO");
        profile.addWaypoint(waypoint);
        Assertions.assertTrue(profile.isDirty());

        Assertions.assertThrows(IllegalArgumentException.class, () -> profile.addWaypoint(null));

        Assertions.assertFalse(profile.getWaypoints().isEmpty());
        Assertions.assertEquals(1, profile.getWaypoints().size());
        Assertions.assertEquals(waypoint, profile.getWaypoints().get(0));
    }

    @Test
    public void testRemoveWaypointFromProfile() throws InterruptedException {
        Player player = server.addPlayer();
        PlayerProfile profile = TestUtilities.awaitProfile(player);

        Waypoint waypoint = new Waypoint(profile, "hello", player.getLocation(), "HELLO");
        profile.addWaypoint(waypoint);
        Assertions.assertEquals(1, profile.getWaypoints().size());

        profile.removeWaypoint(waypoint);
        Assertions.assertThrows(IllegalArgumentException.class, () -> profile.removeWaypoint(null));

        Assertions.assertTrue(profile.getWaypoints().isEmpty());
        Assertions.assertEquals(0, profile.getWaypoints().size());

        // This will not remove it but neither should it throw an error
        profile.removeWaypoint(waypoint);
        Assertions.assertTrue(profile.getWaypoints().isEmpty());
    }

    @Test
    public void testWaypointAlreadyExisting() throws InterruptedException {
        Player player = server.addPlayer();
        PlayerProfile profile = TestUtilities.awaitProfile(player);

        Waypoint waypoint = new Waypoint(profile, "test", player.getLocation(), "Testing");
        profile.addWaypoint(waypoint);

        Assertions.assertEquals(1, profile.getWaypoints().size());
        Assertions.assertThrows(IllegalArgumentException.class, () -> profile.addWaypoint(waypoint));
        Assertions.assertEquals(1, profile.getWaypoints().size());
    }

    @Test
    public void testTooManyWaypoints() throws InterruptedException {
        Player player = server.addPlayer();
        PlayerProfile profile = TestUtilities.awaitProfile(player);

        for (int i = 0; i < 99; i++) {
            Waypoint waypoint = new Waypoint(profile, String.valueOf(i), player.getLocation(), "Test");
            profile.addWaypoint(waypoint);
        }

        // Size should be capped at 21.
        Assertions.assertEquals(21, profile.getWaypoints().size());
    }

    @Test
    public void testWaypointEvent() throws InterruptedException {
        GPSNetwork network = new GPSNetwork();
        Player player = server.addPlayer();
        TestUtilities.awaitProfile(player);

        network.addWaypoint(player, "Hello world", player.getLocation());
        server.getPluginManager().assertEventFired(WaypointCreateEvent.class, event -> event.getPlayer() == player);
    }

    @Test
    public void testWaypointComparison() throws InterruptedException {
        Player player = server.addPlayer();
        PlayerProfile profile = TestUtilities.awaitProfile(player);

        Waypoint waypoint = new Waypoint(profile, "waypoint", player.getLocation(), "Test");
        Waypoint same = new Waypoint(profile, "waypoint", player.getLocation(), "Test");
        Waypoint different = new Waypoint(profile, "waypoint_nope", player.getLocation(), "Test2");

        Assertions.assertEquals(waypoint, same);
        Assertions.assertEquals(waypoint.hashCode(), same.hashCode());

        Assertions.assertNotEquals(waypoint, different);
        Assertions.assertNotEquals(waypoint.hashCode(), different.hashCode());
    }

    @Test
    public void testIsDeathpoint() throws InterruptedException {
        Player player = server.addPlayer();
        PlayerProfile profile = TestUtilities.awaitProfile(player);

        Waypoint waypoint = new Waypoint(profile, "waypoint", player.getLocation(), "Some Waypoint");
        Waypoint deathpoint = new Waypoint(profile, "deathpoint", player.getLocation(), "player:death I died");

        Assertions.assertFalse(waypoint.isDeathpoint());
        Assertions.assertTrue(deathpoint.isDeathpoint());
    }
}
