package io.github.thebusybiscuit.slimefun5.core.services.localization;

import org.bukkit.ChatColor;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
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
            .renderForPacket("NOT_A_REAL_ITEM", "en", TranslationConfig.FallbackMode.ENGLISH, true));
    }

    @Test
    void knownItemRendersAName() {
        SlimefunItem probe = SlimefunItem.getById("ELECTRIC_MOTOR");
        Assertions.assertNotNull(probe, "ELECTRIC_MOTOR must be registered");
        ItemTranslationService.RenderedDisplay d = Slimefun.getItemTranslationService()
            .renderForPacket("ELECTRIC_MOTOR", "en", TranslationConfig.FallbackMode.ENGLISH, true);
        Assertions.assertNotNull(d);
        Assertions.assertNotNull(d.name);
        Assertions.assertFalse(d.name.trim().isEmpty());
    }

    @Test
    void cacheReturnsEqualResultOnSecondCall() {
        ItemTranslationService svc = Slimefun.getItemTranslationService();
        // Give the probe a real (non-raw-id) English name: only genuine renders are cached - a raw-id
        // fallback render is deliberately never memoized (see rawIdRenderIsNotCached...), and the
        // MockBukkit harness has no English baseline for core items, so without this the render degrades
        // to the raw id.
        svc.loadTranslationsForTest("en", new java.io.ByteArrayInputStream(
            "ELECTRIC_MOTOR:\n  name: '&aCached Motor'\n".getBytes(java.nio.charset.StandardCharsets.UTF_8)));
        svc.clearRenderCache();

        ItemTranslationService.RenderedDisplay a = svc.renderForPacket("ELECTRIC_MOTOR", "en", TranslationConfig.FallbackMode.ENGLISH, true);
        ItemTranslationService.RenderedDisplay b = svc.renderForPacket("ELECTRIC_MOTOR", "en", TranslationConfig.FallbackMode.ENGLISH, true);
        Assertions.assertSame(a, b, "cache must return the same instance");
    }

    @Test
    void clearRenderCacheForcesANewInstance() {
        ItemTranslationService svc = Slimefun.getItemTranslationService();
        ItemTranslationService.RenderedDisplay a = svc.renderForPacket("ELECTRIC_MOTOR", "en", TranslationConfig.FallbackMode.ENGLISH, true);
        svc.clearRenderCache();
        ItemTranslationService.RenderedDisplay b = svc.renderForPacket("ELECTRIC_MOTOR", "en", TranslationConfig.FallbackMode.ENGLISH, true);
        Assertions.assertNotSame(a, b, "clearRenderCache() must force a repeat call to recompute a fresh instance");
    }

    @Test
    void idFallbackRendersRawIdForALanguageWithNoLabel() {
        ItemTranslationService svc = Slimefun.getItemTranslationService();
        // "zz" is not a shipped language, so it has no translation - the name must fall back to the raw id.
        ItemTranslationService.RenderedDisplay d = svc.renderForPacket("ELECTRIC_MOTOR", "zz", TranslationConfig.FallbackMode.ID, true);
        Assertions.assertNotNull(d);
        Assertions.assertEquals("ELECTRIC_MOTOR", d.name);
    }

    @Test
    void englishFallbackRendersRealEnglishNotRawId() {
        ItemTranslationService svc = Slimefun.getItemTranslationService();
        // Load an "en" name for a probe id but never load "zz" (not a shipped language) - the "zz"
        // viewer must fall back to the real English name (via the "en" map), not degrade to the raw id.
        String yaml = "ELECTRIC_MOTOR:\n  name: '&aTest Electric Motor'\n";
        Slimefun.getItemTranslationService().loadTranslationsForTest("en",
            new java.io.ByteArrayInputStream(yaml.getBytes(java.nio.charset.StandardCharsets.UTF_8)));
        svc.clearRenderCache();

        ItemTranslationService.RenderedDisplay d = svc.renderForPacket("ELECTRIC_MOTOR", "zz", TranslationConfig.FallbackMode.ENGLISH, true);

        Assertions.assertNotNull(d);
        Assertions.assertEquals("Test Electric Motor", ChatColor.stripColor(d.name));
        Assertions.assertNotEquals("ELECTRIC_MOTOR", d.name);
    }

    @Test
    void nameAndLoreFallBackToTheSameLanguage() {
        ItemTranslationService svc = Slimefun.getItemTranslationService();
        // Only "en" carries a name + type block for this probe; "zz" is unshipped. Before the I-1 fix,
        // the name fell back to English (via an explicit "en" lookup) while the lore blocks fell back to
        // the SERVER DEFAULT language instead - which in this MockBukkit harness is null (see
        // Slimefun#onUnitTestStart(), which constructs LocalizationService with no default language), so
        // the old code's blockForLanguage() had nothing to fall back to and produced an EMPTY type block.
        // The fix makes the lore blocks fall back through the exact same chain as the name (English), so
        // the type block must render here too, built from the "en" content, not be empty.
        String yaml = "ELECTRIC_MOTOR:\n"
            + "  name: '&aConsistent Fallback Motor'\n"
            + "  type:\n"
            + "  - '&7Type: Consistent Fallback Motor'\n";
        svc.loadTranslationsForTest("en",
            new java.io.ByteArrayInputStream(yaml.getBytes(java.nio.charset.StandardCharsets.UTF_8)));
        svc.clearRenderCache();

        ItemTranslationService.RenderedDisplay d = svc.renderForPacket("ELECTRIC_MOTOR", "zz", TranslationConfig.FallbackMode.ENGLISH, true);

        Assertions.assertNotNull(d);
        Assertions.assertEquals("Consistent Fallback Motor", ChatColor.stripColor(d.name));

        boolean hasTypeLine = false;
        for (String line : d.lore) {
            if ("Type: Consistent Fallback Motor".equals(ChatColor.stripColor(line))) {
                hasTypeLine = true;
                break;
            }
        }
        Assertions.assertTrue(hasTypeLine, "lore must fall back to the SAME (english) block as the name, not be empty: " + d.lore);
    }

    @Test
    void differentCacheKeysDoNotCollide() {
        ItemTranslationService svc = Slimefun.getItemTranslationService();
        ItemTranslationService.RenderedDisplay byLanguage = svc.renderForPacket("ELECTRIC_MOTOR", "en", TranslationConfig.FallbackMode.ENGLISH, true);
        ItemTranslationService.RenderedDisplay byOtherLanguage = svc.renderForPacket("ELECTRIC_MOTOR", "zz", TranslationConfig.FallbackMode.ENGLISH, true);
        ItemTranslationService.RenderedDisplay byFallback = svc.renderForPacket("ELECTRIC_MOTOR", "en", TranslationConfig.FallbackMode.ID, true);

        Assertions.assertNotSame(byLanguage, byOtherLanguage, "different languageId must not share a cache entry");
        Assertions.assertNotSame(byLanguage, byFallback, "different fallback mode must not share a cache entry");
        Assertions.assertNotSame(byOtherLanguage, byFallback);
    }

    @Test
    @DisplayName("a raw-id fallback render is never cached, so it heals once a translation loads")
    void rawIdRenderIsNotCachedSoItHealsWhenTranslationsLoad() {
        ItemTranslationService svc = Slimefun.getItemTranslationService();
        // "q1" is an unshipped language with no translation yet, so the ID fallback renders the raw id -
        // exactly the "item shows ENDER_HELMET" symptom seen when a render happens before translations are
        // ready. That degraded result must NOT be memoized.
        ItemTranslationService.RenderedDisplay stale = svc.renderForPacket("ELECTRIC_MOTOR", "q1", TranslationConfig.FallbackMode.ID, true);
        Assertions.assertEquals("ELECTRIC_MOTOR", stale.name);

        // The translation now arrives (as when an addon calls registerTranslations post-boot). Without any
        // explicit clearRenderCache, the very next render must reflect it - a raw-id render must self-heal.
        String yaml = "ELECTRIC_MOTOR:\n  name: '&aQ1 Motor'\n";
        svc.loadTranslationsForTest("q1", new java.io.ByteArrayInputStream(yaml.getBytes(java.nio.charset.StandardCharsets.UTF_8)));

        ItemTranslationService.RenderedDisplay healed = svc.renderForPacket("ELECTRIC_MOTOR", "q1", TranslationConfig.FallbackMode.ID, true);
        Assertions.assertEquals("Q1 Motor", ChatColor.stripColor(healed.name),
            "a raw-id render must not be cached; once the translation loads the next render must use it");
    }

    @Test
    @DisplayName("registerTranslations invalidates the render cache so late translations take effect")
    void registerTranslationsClearsRenderCache() {
        ItemTranslationService svc = Slimefun.getItemTranslationService();
        // A genuine (non-raw-id) render IS cached. Load a real English name so the render doesn't degrade
        // to the raw id (which is never cached) in the baseline-less unit harness.
        svc.loadTranslationsForTest("en", new java.io.ByteArrayInputStream(
            "ELECTRIC_MOTOR:\n  name: '&aRegistered Motor'\n".getBytes(java.nio.charset.StandardCharsets.UTF_8)));
        svc.clearRenderCache();

        ItemTranslationService.RenderedDisplay a = svc.renderForPacket("ELECTRIC_MOTOR", "en", TranslationConfig.FallbackMode.ENGLISH, true);
        ItemTranslationService.RenderedDisplay a2 = svc.renderForPacket("ELECTRIC_MOTOR", "en", TranslationConfig.FallbackMode.ENGLISH, true);
        Assertions.assertSame(a, a2, "precondition: a non-raw-id render must be cached");

        // An addon registering its translations post-boot must drop the stale renders.
        svc.registerTranslations(plugin);

        ItemTranslationService.RenderedDisplay b = svc.renderForPacket("ELECTRIC_MOTOR", "en", TranslationConfig.FallbackMode.ENGLISH, true);
        Assertions.assertNotSame(a, b, "registerTranslations must clear the render cache");
    }

    @Test
    @DisplayName("the guide-source marker round-trips through PDC (the packet source-append relies on it)")
    void guideSourceMarkerRoundTrips() {
        io.github.thebusybiscuit.slimefun5.libraries.keys.NamespacedKey key =
            new io.github.thebusybiscuit.slimefun5.libraries.keys.NamespacedKey(Slimefun.instance(), io.github.thebusybiscuit.slimefun5.utils.ChestMenuUtils.GUIDE_SOURCE_MARKER);

        org.bukkit.inventory.ItemStack marked = io.github.thebusybiscuit.slimefun5.utils.ChestMenuUtils.markGuideSource(new org.bukkit.inventory.ItemStack(org.bukkit.Material.PAPER));
        Assertions.assertTrue(io.github.thebusybiscuit.slimefun5.utils.compatibility.PdcCompat.has(marked.getItemMeta(), key, "STRING"),
            "a marked guide display copy must carry the source marker");

        Assertions.assertFalse(io.github.thebusybiscuit.slimefun5.utils.compatibility.PdcCompat.has(new org.bukkit.inventory.ItemStack(org.bukkit.Material.PAPER).getItemMeta(), key, "STRING"),
            "an unmarked (real) item must not carry the marker, so its source is never shown");
    }

    @Test
    @DisplayName("renderForPacket honours includeDescription: false drops the description block")
    void includeDescriptionToggleControlsDescriptionBlock() {
        ItemTranslationService svc = Slimefun.getItemTranslationService();
        String id = "ELECTRIC_MOTOR";
        // MockBukkit's unit-test LocalizationService has no default language, so loadBundled() (called in
        // load()) never actually loads languages/en/items.yml here - load a type + description block for
        // "en" explicitly, the same way the other tests in this file inject translations.
        String yaml = "ELECTRIC_MOTOR:\n"
            + "  name: '&aDescribed Motor'\n"
            + "  type:\n"
            + "  - '&7&oComponent'\n"
            + "  description:\n"
            + "  - '&7A crafting component.'\n";
        svc.loadTranslationsForTest("en", new java.io.ByteArrayInputStream(yaml.getBytes(java.nio.charset.StandardCharsets.UTF_8)));
        svc.clearRenderCache();

        ItemTranslationService.RenderedDisplay withDesc = svc.renderForPacket(id, "en", TranslationConfig.FallbackMode.ENGLISH, true);
        ItemTranslationService.RenderedDisplay noDesc = svc.renderForPacket(id, "en", TranslationConfig.FallbackMode.ENGLISH, false);
        Assertions.assertNotEquals(withDesc.lore, noDesc.lore,
            "includeDescription=false must produce different lore than includeDescription=true when a description exists");
        Assertions.assertTrue(withDesc.lore.size() >= noDesc.lore.size(),
            "dropping the description can only remove lines");
    }
}
