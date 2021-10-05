package io.github.thebusybiscuit.slimefun4.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

class TestWikiResource {

    @Test
    @DisplayName("Test wiki.json file format")
    void testWikiJson() throws IOException {
        JsonParser parser = new JsonParser();
        Pattern pattern = Pattern.compile("[A-Z_0-9]+");

        // Here we test for any Syntax errors in our wiki.json file
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/wiki.json"), StandardCharsets.UTF_8))) {
            JsonElement json = parser.parse(reader);
            Assertions.assertTrue(json.isJsonObject());

            for (Map.Entry<String, JsonElement> entry : json.getAsJsonObject().entrySet()) {
                Assertions.assertTrue(pattern.matcher(entry.getKey()).matches());

                JsonElement element = entry.getValue();
                Assertions.assertTrue(element.isJsonPrimitive());
                Assertions.assertTrue(element.getAsJsonPrimitive().isString());
            }
        }
    }

}
