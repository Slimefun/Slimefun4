package io.github.thebusybiscuit.slimefun4.core.services.localization;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Your friendly neighbourhood spellcheck.
 * Brought to you by our Discord bot "@WalshBot".
 * No more incorrect spelling of "Slimefun".
 * 
 * @author TheBusyBiscuit
 *
 */
class TestSlimefunSpelling {

    private final Pattern incorrectSpelling = Pattern.compile("[Ss]lime(?:F|( [Ff]))un");

    @ParameterizedTest
    @ParametersAreNonnullByDefault
    @MethodSource("getAllLanguageFiles")
    @DisplayName("Test correct spelling of Slimefun in language files")
    void testSpelling(LanguagePreset lang, LanguageFile file) throws IOException {
        String path = file.getFilePath(lang.getLanguageCode());
        InputStream inputStream = getClass().getResourceAsStream(path);

        if (inputStream == null) {
            // This file does not exist, we consider it "passed".
            return;
        }

        // Read the language file as yaml
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            FileConfiguration config = YamlConfiguration.loadConfiguration(reader);

            for (Map.Entry<String, Object> entry : config.getValues(true).entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();

                if (value instanceof String) {
                    assertCorrectSpelling(lang, file, key, (String) value);
                } else if (value instanceof List) {
                    for (Object element : (List<?>) value) {
                        if (element instanceof String) {
                            assertCorrectSpelling(lang, file, key, (String) element);
                        }
                    }
                }
            }
        }
    }

    @ParametersAreNonnullByDefault
    private void assertCorrectSpelling(LanguagePreset lang, LanguageFile file, String key, String value) {
        Matcher matcher = incorrectSpelling.matcher(value);
        boolean hasIncorrectSpelling = matcher.find();

        if (hasIncorrectSpelling) {
            String location = lang.getLanguageCode() + '/' + file.name() + ": " + key;
            Assertions.fail("Mistake in file \"" + location + "\" - It's \"Slimefun\", not \"" + matcher.group() + "\"!");
        }
    }

    private static @Nonnull Stream<Arguments> getAllLanguageFiles() {
        Stream<LanguagePreset> stream = Arrays.stream(LanguagePreset.values());
        return stream.flatMap(a -> Arrays.stream(LanguageFile.values()).map(b -> Arguments.of(a, b)));
    }

}
