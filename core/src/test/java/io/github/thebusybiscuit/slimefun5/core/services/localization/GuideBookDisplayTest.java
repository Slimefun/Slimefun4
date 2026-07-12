package io.github.thebusybiscuit.slimefun5.core.services.localization;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
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
 * here and {@link GuideBookDisplay#build()} would produce an empty cache. These tests instead go through
 * the package-private {@link GuideBookDisplay#buildFrom} seam with a real "en" {@link Language} built
 * directly from the bundled {@code languages/en/messages.yml} resource, the same way {@code loadBundled()}
 * would at real boot.
 */
class GuideBookDisplayTest {

    private static Slimefun plugin;
    private static GuideBookDisplay display;

    @BeforeAll
    public static void load() {
        MockBukkit.mock();
        plugin = MockBukkit.load(Slimefun.class);
        SlimefunItemSetup.setup(plugin);

        Language english = new Language("en", "11b3188fd44902f72602bd7c2141f5a70673a411adb3d81862c69e536166b");
        try (InputStream stream = Slimefun.class.getResourceAsStream("/languages/en/messages.yml")) {
            FileConfiguration config = YamlConfiguration.loadConfiguration(new InputStreamReader(stream, StandardCharsets.UTF_8));
            english.setFile(LanguageFile.MESSAGES, config);
        } catch (Exception e) {
            throw new IllegalStateException("Could not load languages/en/messages.yml for the test", e);
        }

        display = Slimefun.getGuideBookDisplay();
        display.buildFrom(Collections.singletonList(english));
    }

    @AfterAll
    public static void unload() {
        MockBukkit.unmock();
    }

    @Test
    void englishSurvivalAndCheatBooksRenderWithANonEmptyName() {
        ItemTranslationService.RenderedDisplay survival = display.rendered("en", false);
        ItemTranslationService.RenderedDisplay cheat = display.rendered("en", true);

        Assertions.assertNotNull(survival);
        Assertions.assertNotNull(survival.name);
        Assertions.assertFalse(survival.name.trim().isEmpty());

        Assertions.assertNotNull(cheat);
        Assertions.assertNotNull(cheat.name);
        Assertions.assertFalse(cheat.name.trim().isEmpty());

        Assertions.assertNotEquals(survival.name, cheat.name, "the cheat book must render a distinct name from the survival book");
    }

    @Test
    void unshippedLanguageFallsBackToEnglish() {
        // "zz" was never built - rendered() must fall back to the English rendering.
        ItemTranslationService.RenderedDisplay fallback = display.rendered("zz", false);
        ItemTranslationService.RenderedDisplay english = display.rendered("en", false);

        Assertions.assertNotNull(fallback);
        Assertions.assertEquals(english.name, fallback.name);
        Assertions.assertEquals(english.lore, fallback.lore);
    }
}
