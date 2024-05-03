package io.github.thebusybiscuit.slimefun4.core.services.sounds;

import be.seeseemelk.mockbukkit.MockBukkit;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import org.junit.jupiter.api.*;

public class TestSoundService {

    private static Slimefun plugin;

    @BeforeAll
    public static void load() {
        MockBukkit.mock();
        plugin = MockBukkit.load(Slimefun.class);
    }

    @AfterAll
    public static void unload() {
        MockBukkit.unmock();
    }

    @Test
    @DisplayName("Testing each SoundConfiguration")
    void testSounds() {
        SoundService service = Slimefun.getSoundService();
        SoundEffect[] effects = SoundEffect.values();

        for (SoundEffect i : effects) {
            SoundConfiguration element = service.getConfiguration(i);
            Assertions.assertNotNull(element, "The sound effect must not be null!");
        }

    }
}
