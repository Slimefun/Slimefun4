package io.github.thebusybiscuit.slimefun4.testing.tests.profiles;

import java.util.Iterator;
import java.util.Optional;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.entity.OfflinePlayerMock;
import io.github.thebusybiscuit.slimefun4.api.items.HashedArmorpiece;
import io.github.thebusybiscuit.slimefun4.api.player.PlayerProfile;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.testing.TestUtilities;

public class TestPlayerProfile {

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
    public void testOfflinePlayer() throws InterruptedException {
        OfflinePlayer player = new OfflinePlayerMock("Offline Test Player");
        PlayerProfile profile = TestUtilities.awaitProfile(player);

        Assertions.assertNotNull(profile);
        Assertions.assertEquals(profile, SlimefunPlugin.getRegistry().getPlayerProfiles().get(player.getUniqueId()));

        // This profile should now be in memory and return true
        Assertions.assertTrue(PlayerProfile.get(player, p -> {}));
        Assertions.assertTrue(PlayerProfile.request(player));

        // The Player is offline so this should return null
        Assertions.assertNull(profile.getPlayer());

        Optional<PlayerProfile> optional = PlayerProfile.find(player);
        Assertions.assertTrue(optional.isPresent());
        Assertions.assertEquals(profile, optional.get());
    }

    @Test
    public void testOnlinePlayer() throws InterruptedException {
        Player player = server.addPlayer();
        PlayerProfile profile = TestUtilities.awaitProfile(player);

        Assertions.assertNotNull(profile);
        Assertions.assertEquals(player, profile.getPlayer());
    }

    @Test
    public void testIterator() throws InterruptedException {
        // Clear out any previous profiles
        Iterator<PlayerProfile> clear = PlayerProfile.iterator();
        while (clear.hasNext()) {
            clear.next();
            clear.remove();
        }

        Iterator<PlayerProfile> emptyIterator = PlayerProfile.iterator();
        Assertions.assertFalse(emptyIterator.hasNext());

        Player player = server.addPlayer();
        PlayerProfile profile = TestUtilities.awaitProfile(player);

        Iterator<PlayerProfile> iterator = PlayerProfile.iterator();
        Assertions.assertTrue(iterator.hasNext());
        Assertions.assertEquals(profile, iterator.next());
    }

    @Test
    public void testAttributes() throws InterruptedException {
        Player player = server.addPlayer();
        PlayerProfile profile = TestUtilities.awaitProfile(player);

        Assertions.assertNotNull(profile.getConfig());

        Assertions.assertFalse(profile.isDirty());
        profile.markDirty();
        Assertions.assertTrue(profile.isDirty());

        Assertions.assertFalse(profile.isMarkedForDeletion());
        profile.markForDeletion();
        Assertions.assertTrue(profile.isMarkedForDeletion());

        HashedArmorpiece[] armor = profile.getArmor();
        Assertions.assertEquals(4, armor.length);

        Assertions.assertNotNull(armor[0]);
        Assertions.assertNotNull(armor[1]);
        Assertions.assertNotNull(armor[2]);
        Assertions.assertNotNull(armor[3]);
    }

    @Test
    public void testNotExistentProfile() throws InterruptedException {
        OfflinePlayer player = new OfflinePlayerMock("Offline Test Player 2");

        Assertions.assertFalse(PlayerProfile.find(player).isPresent());

        // This loads the profile asynchronously
        Assertions.assertFalse(PlayerProfile.request(player));
    }

    @Test
    public void testNullPlayerGetProfile() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> PlayerProfile.get(null, p -> {}));
    }

    @Test
    public void testHashCode() throws InterruptedException {
        Player player = server.addPlayer();
        PlayerProfile profile = TestUtilities.awaitProfile(player);

        Assertions.assertEquals(player.getUniqueId().hashCode(), profile.hashCode());
    }

}
