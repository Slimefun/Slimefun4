package io.github.thebusybiscuit.slimefun5.core.services.localization;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockbukkit.mockbukkit.MockBukkit;

import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun5.implementation.setup.SlimefunItemSetup;

/**
 * Verifies the honest weighted coverage metric (Task D): a language that only translates item names
 * (no lore blocks, no messages/menus/enchants) must score well below 100%, and English must always be
 * exactly 100%. Uses the {@code loadTranslationsForTest} seam to inject synthetic en/zz item entries for
 * a real registered item id (ELECTRIC_MOTOR - see PacketRenderTest for the same pattern), since
 * getItemUnitCoverage only counts registered Slimefun items.
 */
class CoverageTest {

    private static final String TEST_ID = "ELECTRIC_MOTOR";

    private static Slimefun plugin;

    @BeforeAll
    public static void load() {
        MockBukkit.mock();
        plugin = MockBukkit.load(Slimefun.class);
        SlimefunItemSetup.setup(plugin);

        // English defines name + type + stats for TEST_ID (3 units).
        String en = TEST_ID + ":\n"
            + "  name: '&aTest Electric Motor'\n"
            + "  type:\n"
            + "  - '&7Type: Component'\n"
            + "  stats:\n"
            + "  - '&7Speed: 1'\n";
        Slimefun.getItemTranslationService().loadTranslationsForTest("en", new ByteArrayInputStream(en.getBytes(StandardCharsets.UTF_8)));

        // "zz" only has the name (1 unit).
        String zz = TEST_ID + ":\n"
            + "  name: '&aZZ Motor'\n";
        Slimefun.getItemTranslationService().loadTranslationsForTest("zz", new ByteArrayInputStream(zz.getBytes(StandardCharsets.UTF_8)));
    }

    @AfterAll
    public static void unload() {
        MockBukkit.unmock();
    }

    @Test
    void namesOnlyLanguageScoresWellBelow100() {
        ItemTranslationService svc = Slimefun.getItemTranslationService();
        Map<String, int[]> zz = svc.getItemUnitCoverage("zz");
        int cov = 0;
        int tot = 0;

        for (int[] c : zz.values()) {
            cov += c[0];
            tot += c[1];
        }

        Assertions.assertTrue(tot > cov, "a names-only language must not be counted 100%");
    }

    @Test
    void englishIsOneHundredPercent() {
        Assertions.assertEquals(100, Slimefun.getTranslationCoverageService().getOverallPercent("en"));
    }

    @Test
    void zzOverallPercentIsBelow100() {
        int pct = Slimefun.getTranslationCoverageService().getOverallPercent("zz");
        Assertions.assertTrue(pct < 100, "an unshipped language must never read 100%");
    }
}
