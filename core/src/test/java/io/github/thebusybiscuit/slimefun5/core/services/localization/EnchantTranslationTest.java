package io.github.thebusybiscuit.slimefun5.core.services.localization;

import java.io.InputStream;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockbukkit.mockbukkit.MockBukkit;

import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun5.implementation.setup.SlimefunItemSetup;

/**
 * MockBukkit's unit-test {@code LocalizationService} is constructed with no default language (see
 * {@code Slimefun#onUnitTestStart}), so {@code Slimefun.getLocalization().getLanguages()} is always empty
 * here and {@link EnchantTranslationService#loadBundled()} would load nothing. This test instead goes
 * through the package-private {@link EnchantTranslationService#loadTranslationsForTest} seam with the real
 * bundled {@code languages/en/enchantments.yml} resource, the same way {@code loadBundled()} would at real
 * boot.
 */
class EnchantTranslationTest {

    private static Slimefun plugin;

    @BeforeAll
    public static void load() {
        MockBukkit.mock();
        plugin = MockBukkit.load(Slimefun.class);
        SlimefunItemSetup.setup(plugin);

        EnchantTranslationService svc = Slimefun.getEnchantTranslationService();
        try (InputStream stream = Slimefun.class.getResourceAsStream("/languages/en/enchantments.yml")) {
            svc.loadTranslationsForTest("en", stream);
        } catch (Exception e) {
            throw new IllegalStateException("Could not load languages/en/enchantments.yml for the test", e);
        }
    }

    @AfterAll
    public static void unload() {
        MockBukkit.unmock();
    }

    @Test
    void resolvesEnglishThenFallsBack() {
        EnchantTranslationService svc = Slimefun.getEnchantTranslationService();
        Assertions.assertEquals("Silk Touch", svc.name("en", "silk_touch"));
        Assertions.assertEquals("Silk Touch", svc.name("zz", "silk_touch")); // unshipped lang → English
        Assertions.assertNull(svc.name("en", "made_up_enchant"));            // unknown key → null
    }
}
