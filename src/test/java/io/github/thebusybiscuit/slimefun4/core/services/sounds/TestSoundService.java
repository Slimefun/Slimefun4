package io.github.thebusybiscuit.slimefun4.core.services.sounds;

import be.seeseemelk.mockbukkit.MockBukkit;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

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

    @DisplayName("Testing each SoundConfiguration")
    @ParameterizedTest
    @EnumSource(SoundEffect.class)
    void testSounds(SoundEffect effect) {
        SoundService service = Slimefun.getSoundService();
        SoundConfiguration element = service.getConfiguration(effect);
        Assertions.assertNotNull(element, "The sound effect must not be null!");
    }
}
