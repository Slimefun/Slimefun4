package io.github.thebusybiscuit.slimefun4.core.services.localization;

import java.io.IOException;
import java.util.regex.Pattern;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Your friendly neighbourhood spellcheck.
 * Brought to you by our Discord bot "@WalshBot".
 * No more incorrect spelling of "Slimefun".
 *
 * @author TheBusyBiscuit
 *
 */
class TestSlimefunSpelling extends AbstractLocaleRegexChecker {

    TestSlimefunSpelling() {
        super(Pattern.compile("[Ss]lime(?:F|( [Ff]))un"));
    }

    @ParameterizedTest
    @ParametersAreNonnullByDefault
    @MethodSource("getAllLanguageFiles")
    @DisplayName("Test correct spelling of Slimefun in language files")
    void testSpelling(LanguagePreset lang, LanguageFile file) throws IOException, InvalidConfigurationException {
        FileConfiguration config = readLanguageFile(lang, file);
        if (config == null) {
            return;
        }
        assertNoRegexMatchesForAllEntries(lang, file, config);
    }

}
