package io.github.thebusybiscuit.slimefun4.core.services.localization;

import java.io.BufferedReader;
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
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import org.apache.commons.lang.Validate;
import org.bukkit.configuration.file.FileConfiguration;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.provider.Arguments;

import be.seeseemelk.mockbukkit.MockBukkit;

class AbstractLocaleRegexChecker {

    private final Pattern pattern;

    AbstractLocaleRegexChecker(@Nonnull Pattern pattern) {
        Validate.notNull(pattern, "The pattern cannot be null.");

        this.pattern = pattern;
    }

    @BeforeAll
    public static void load() {
        MockBukkit.mock();
    }

    @AfterAll
    public static void unload() {
        MockBukkit.unmock();
    }

    public @Nonnull Pattern getPattern() {
        return this.pattern;
    }

    @ParametersAreNonnullByDefault
    @Nullable
    BufferedReader readLanguageFile(LanguagePreset lang, LanguageFile file) {
        String path = file.getFilePath(lang.getLanguageCode());
        InputStream inputStream = getClass().getResourceAsStream(path);

        if (inputStream == null) {
            // This file does not exist, we consider it "passed".
            return null;
        }

        return new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
    }

    @ParametersAreNonnullByDefault
    void assertNoRegexMatchesForAllEntries(LanguagePreset lang, LanguageFile file, FileConfiguration config) {
        for (Map.Entry<String, Object> entry : config.getValues(true).entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            if (value instanceof String) {
                String location = lang.getLanguageCode() + '/' + file.name() + ": " + key;
                assertNoRegexMatch(location, (String) value);
            } else if (value instanceof List) {
                int index = 0;
                for (Object element : (List<?>) value) {
                    if (element instanceof String) {
                        String location = lang.getLanguageCode() + '/' + file.name() + ": " + key + "[" + index + "]";
                        assertNoRegexMatch(location, (String) element);
                    }

                    index++;
                }
            }
        }
    }

    @ParametersAreNonnullByDefault
    void assertNoRegexMatch(String location, String value) {
        Matcher matcher = getPattern().matcher(value);
        boolean hasMatch = matcher.find();

        if (hasMatch) {
            Assertions.fail("Mistake found @ \"" + location + "\" - \"" + matcher.group() + "\"!");
        }
    }

    static @Nonnull Stream<Arguments> getAllLanguageFiles() {
        Stream<LanguagePreset> stream = Arrays.stream(LanguagePreset.values());
        return stream.flatMap(a -> Arrays.stream(LanguageFile.values()).map(b -> Arguments.of(a, b)));
    }

}
