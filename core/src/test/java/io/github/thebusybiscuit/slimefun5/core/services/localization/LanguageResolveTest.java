package io.github.thebusybiscuit.slimefun5.core.services.localization;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class LanguageResolveTest {

    private static Predicate<String> loadedSetOf(String... ids) {
        Set<String> loaded = new HashSet<>();
        Collections.addAll(loaded, ids);
        return loaded::contains;
    }

    @Test
    void explicitLoadedWins() {
        String result = LanguageResolver.resolveLanguageId(
            "de", "en", TranslationConfig.LanguageSource.CLIENT, loadedSetOf("de", "en"), "en");
        Assertions.assertEquals("de", result);
    }

    @Test
    void explicitSetButNotLoadedFallsBackToClientInClientMode() {
        String result = LanguageResolver.resolveLanguageId(
            "fr", "en", TranslationConfig.LanguageSource.CLIENT, loadedSetOf("en"), "en");
        Assertions.assertEquals("en", result);
    }

    @Test
    void noExplicitClientModeClientLoadedUsesClient() {
        String result = LanguageResolver.resolveLanguageId(
            null, "de", TranslationConfig.LanguageSource.CLIENT, loadedSetOf("de", "en"), "en");
        Assertions.assertEquals("de", result);
    }

    @Test
    void noExplicitClientModeClientNotLoadedUsesServerDefault() {
        String result = LanguageResolver.resolveLanguageId(
            null, "de", TranslationConfig.LanguageSource.CLIENT, loadedSetOf("en"), "en");
        Assertions.assertEquals("en", result);
    }

    @Test
    void noExplicitServerModeIgnoresClientEvenWhenLoaded() {
        String result = LanguageResolver.resolveLanguageId(
            null, "de", TranslationConfig.LanguageSource.SERVER, loadedSetOf("de", "en"), "en");
        Assertions.assertEquals("en", result);
    }

    @Test
    void nothingLoadedAndNoServerDefaultReturnsNull() {
        String result = LanguageResolver.resolveLanguageId(
            null, "de", TranslationConfig.LanguageSource.CLIENT, loadedSetOf(), null);
        Assertions.assertNull(result);
    }
}
