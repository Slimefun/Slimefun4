package io.github.thebusybiscuit.slimefun4.tests.researches;

import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import io.github.thebusybiscuit.slimefun4.api.events.ResearchUnlockEvent;
import io.github.thebusybiscuit.slimefun4.api.player.PlayerProfile;
import io.github.thebusybiscuit.slimefun4.core.researching.Research;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;

public class TestResearchUnlocking {

    private static ServerMock server;
    private static SlimefunPlugin plugin;

    @BeforeAll
    public static void load() throws InterruptedException {
        server = MockBukkit.mock();
        plugin = MockBukkit.load(SlimefunPlugin.class);
    }

    @AfterAll
    public static void unload() {
        MockBukkit.unmock();
    }

    private Player awaitUnlock(Player player, Research research, boolean instant) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        AtomicReference<Player> ref = new AtomicReference<>();

        // This loads the profile asynchronously
        research.unlock(player, instant, p -> {
            ref.set(p);
            latch.countDown();
        });

        latch.await(10, TimeUnit.SECONDS);
        return ref.get();
    }

    @ParameterizedTest
    @ValueSource(booleans = { true, false })
    public void testUnlock(boolean instant) throws InterruptedException {
        SlimefunPlugin.getRegistry().setResearchingEnabled(true);
        Player player = server.addPlayer();
        Research research = new Research(new NamespacedKey(plugin, "instant_unlock"), 1842, "Instant", 500);

        Player p = awaitUnlock(player, research, instant);
        Optional<PlayerProfile> profile = PlayerProfile.find(p);

        server.getPluginManager().assertEventFired(ResearchUnlockEvent.class);
        Assertions.assertEquals(player, p);
        Assertions.assertTrue(profile.isPresent());
        Assertions.assertTrue(profile.get().hasUnlocked(research));
    }

}
