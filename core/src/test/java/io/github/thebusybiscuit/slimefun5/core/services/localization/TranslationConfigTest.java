package io.github.thebusybiscuit.slimefun5.core.services.localization;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TranslationConfigTest {

    @Test
    void fallbackModeParsesLenientlyDefaultingToEnglish() {
        Assertions.assertEquals(TranslationConfig.FallbackMode.ID, TranslationConfig.FallbackMode.of("id"));
        Assertions.assertEquals(TranslationConfig.FallbackMode.ID, TranslationConfig.FallbackMode.of("ID"));
        Assertions.assertEquals(TranslationConfig.FallbackMode.ENGLISH, TranslationConfig.FallbackMode.of("english"));
        Assertions.assertEquals(TranslationConfig.FallbackMode.ENGLISH, TranslationConfig.FallbackMode.of(null));
        Assertions.assertEquals(TranslationConfig.FallbackMode.ENGLISH, TranslationConfig.FallbackMode.of("garbage"));
    }
}
