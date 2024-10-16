package io.github.thebusybiscuit.slimefun4.core.services.localization;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;
import java.util.regex.Pattern;

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
    void testSpelling(LanguagePreset lang, LanguageFile file) throws IOException, InvalidConfigurationException {
        FileConfiguration config = readLanguageFile(lang, file);
        if (config == null) {
            return;
        }
        assertNoRegexMatchesForAllEntries(lang, file, config);
    }

}
