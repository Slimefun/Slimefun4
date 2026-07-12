package io.github.thebusybiscuit.slimefun5.core.guide.wiki;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockbukkit.mockbukkit.MockBukkit;

import io.github.thebusybiscuit.slimefun5.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun5.implementation.setup.SlimefunItemSetup;

/**
 * MockBukkit's unit-test {@code LocalizationService} is constructed with no default language (see
 * {@code Slimefun#onUnitTestStart}), so {@code Slimefun.getLocalization().getLanguages()} is always empty
 * here and {@link WikiText#loadBundled()} would load no per-language override. This test instead
 * exercises a standalone {@link WikiText} instance (not the shared {@code Slimefun.getWikiText()}) through
 * its package-private {@code loadItemLanguageOverrideForTest}/{@code loadFallbackMessagesForTest} seams,
 * the same way {@code EnchantTranslationTest}/{@code GuideBookDisplayTest} bypass the empty registry
 * elsewhere in this codebase.
 */
class WikiTextLanguageTest {

    private static Slimefun plugin;
    private static WikiText wikiText;

    /** Has an authored English entry and a synthetic German override -> German should win. */
    private static SlimefunItem translatedItem;
    /** Has an authored English entry only -> a German lookup must fall back to English (per-key). */
    private static SlimefunItem englishOnlyItem;
    /** Has no authored entry in any language -> exercises {@code buildFallback}. */
    private static SlimefunItem fallbackItem;

    @BeforeAll
    public static void load() {
        MockBukkit.mock();
        plugin = MockBukkit.load(Slimefun.class);
        SlimefunItemSetup.setup(plugin);

        List<SlimefunItem> items = Slimefun.getRegistry().getEnabledSlimefunItems();
        Assertions.assertTrue(items.size() >= 3, "Need at least 3 registered items for this test");

        translatedItem = items.get(0);
        englishOnlyItem = items.get(1);
        fallbackItem = items.get(2);

        wikiText = new WikiText();
        wikiText.set(translatedItem.getId(), Arrays.asList("&7English body."));
        wikiText.set(englishOnlyItem.getId(), Arrays.asList("&7English-only body."));
        wikiText.setMechanic("getting_started", Arrays.asList("&7English mechanic body."));

        String syntheticDe = translatedItem.getId() + ":\n  - '&7German body.'\n";
        try (InputStream stream = new ByteArrayInputStream(syntheticDe.getBytes(StandardCharsets.UTF_8))) {
            wikiText.loadItemLanguageOverrideForTest("de", stream);
        } catch (Exception e) {
            throw new IllegalStateException("Could not load synthetic German wiki override", e);
        }

        String syntheticDeMechanic = "getting_started:\n  - '&7German mechanic body.'\n";
        try (InputStream stream = new ByteArrayInputStream(syntheticDeMechanic.getBytes(StandardCharsets.UTF_8))) {
            wikiText.loadMechanicLanguageOverrideForTest("de", stream);
        } catch (Exception e) {
            throw new IllegalStateException("Could not load synthetic German mechanic override", e);
        }

        try (InputStream stream = Slimefun.class.getResourceAsStream("/languages/en/messages.yml")) {
            wikiText.loadFallbackMessagesForTest("en", stream);
        } catch (Exception e) {
            throw new IllegalStateException("Could not load languages/en/messages.yml for the test", e);
        }
    }

    @AfterAll
    public static void unload() {
        MockBukkit.unmock();
    }

    @Test
    void germanOverrideWinsForTranslatedItem() {
        List<String> lines = wikiText.get(translatedItem, "de");
        Assertions.assertEquals(Collections.singletonList("&7German body."), lines);
    }

    @Test
    void missingGermanEntryFallsBackToEnglishPerKey() {
        List<String> lines = wikiText.get(englishOnlyItem, "de");
        Assertions.assertEquals(Collections.singletonList("&7English-only body."), lines);
    }

    @Test
    void unknownLanguageFallsBackToEnglish() {
        List<String> lines = wikiText.get(translatedItem, "zz");
        Assertions.assertEquals(Collections.singletonList("&7English body."), lines);
    }

    @Test
    void mechanicGermanOverrideWinsAndFallsBackPerKey() {
        Assertions.assertEquals(Collections.singletonList("&7German mechanic body."), wikiText.getMechanic("getting_started", "de"));
        Assertions.assertEquals(Collections.singletonList("&7English mechanic body."), wikiText.getMechanic("getting_started", "zz"));
        Assertions.assertEquals(Collections.emptyList(), wikiText.getMechanic("no_such_topic", "de"));
    }

    @Test
    void buildFallbackProducesLocalizedText() {
        List<String> lines = wikiText.get(fallbackItem, "en");

        Assertions.assertFalse(lines.isEmpty());

        String groupKey = fallbackItem.getItemGroup().getKey() != null
            ? fallbackItem.getItemGroup().getKey().getKey()
            : "Slimefun";
        Assertions.assertEquals("&7A " + groupKey + " item.", lines.get(0));

        if (fallbackItem.getRecipeType() != null && fallbackItem.getRecipeType().getKey() != null) {
            Assertions.assertEquals(2, lines.size());
            Assertions.assertEquals("&7Crafted via: &b" + fallbackItem.getRecipeType().getKey().getKey(), lines.get(1));
        }
    }

    @Test
    void buildFallbackUnknownLanguageStillResolvesToEnglish() {
        List<String> lines = wikiText.get(fallbackItem, "zz");
        Assertions.assertFalse(lines.isEmpty());
        Assertions.assertFalse(lines.get(0).startsWith("! Missing"));
    }
}
