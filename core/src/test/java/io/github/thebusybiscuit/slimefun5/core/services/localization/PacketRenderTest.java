package io.github.thebusybiscuit.slimefun5.core.services.localization;

import org.bukkit.Material;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockbukkit.mockbukkit.MockBukkit;

import io.github.thebusybiscuit.slimefun5.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun5.implementation.setup.SlimefunItemSetup;

class PacketRenderTest {

    private static Slimefun plugin;

    @BeforeAll
    public static void load() {
        MockBukkit.mock();
        plugin = MockBukkit.load(Slimefun.class);
        SlimefunItemSetup.setup(plugin);
        Slimefun.getItemTranslationService().loadBundled();
    }

    @AfterAll
    public static void unload() {
        MockBukkit.unmock();
    }

    @Test
    void unknownIdRendersNull() {
        Assertions.assertNull(Slimefun.getItemTranslationService()
            .renderForPacket("NOT_A_REAL_ITEM", "en", TranslationConfig.FallbackMode.ENGLISH));
    }

    @Test
    void knownItemRendersAName() {
        SlimefunItem probe = SlimefunItem.getById("ELECTRIC_MOTOR");
        Assertions.assertNotNull(probe, "ELECTRIC_MOTOR must be registered");
        ItemTranslationService.RenderedDisplay d = Slimefun.getItemTranslationService()
            .renderForPacket("ELECTRIC_MOTOR", "en", TranslationConfig.FallbackMode.ENGLISH);
        Assertions.assertNotNull(d);
        Assertions.assertNotNull(d.name);
        Assertions.assertFalse(d.name.trim().isEmpty());
    }

    @Test
    void cacheReturnsEqualResultOnSecondCall() {
        ItemTranslationService svc = Slimefun.getItemTranslationService();
        ItemTranslationService.RenderedDisplay a = svc.renderForPacket("ELECTRIC_MOTOR", "en", TranslationConfig.FallbackMode.ENGLISH);
        ItemTranslationService.RenderedDisplay b = svc.renderForPacket("ELECTRIC_MOTOR", "en", TranslationConfig.FallbackMode.ENGLISH);
        Assertions.assertSame(a, b, "cache must return the same instance");
    }
}
