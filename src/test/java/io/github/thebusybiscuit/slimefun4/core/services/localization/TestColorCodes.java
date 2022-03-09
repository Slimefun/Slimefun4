package io.github.thebusybiscuit.slimefun4.core.services.localization;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.regex.Pattern;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * We need to make sure that color codes are still working properly.
 * <p>
 * Sometimes, the color code can be accidentally removed as people see
 * it as part of the word on first look. This test is designed to catch
 * these cases. It is not perfect but it does catch a few instances.
 * <p>
 * The test checks for color chars (ampersands) that have no follow-up
 * chatcolor key but rather a preceeding one.
 * <p>
 * The test will catch occurences like "a& ", "b&Hello" or "7&", "5& a".
 * The test will however ignore valid color codes such as "a&a".
 * 
 * @author TheBusyBiscuit
 *
 */
class TestColorCodes extends AbstractLocaleRegexChecker {

    TestColorCodes() {
        super(Pattern.compile("[a-f0-9klmno]&[^a-f0-9klmno]"));
    }

    @ParameterizedTest
    @ParametersAreNonnullByDefault
    @MethodSource("getAllLanguageFiles")
    @DisplayName("Test for mistakes in color codes for Slimefun locale files")
    void testSpelling(LanguagePreset lang, LanguageFile file) throws IOException {
        try (BufferedReader reader = readLanguageFile(lang, file)) {
            if (reader == null) {
                return;
            }

            FileConfiguration config = YamlConfiguration.loadConfiguration(reader);
            assertNoRegexMatchesForAllEntries(lang, file, config);
        }
    }

}
