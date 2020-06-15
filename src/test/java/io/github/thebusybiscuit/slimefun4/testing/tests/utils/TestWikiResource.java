package io.github.thebusybiscuit.slimefun4.testing.tests.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class TestWikiResource {

    @Test
    public void testWikiJson() throws IOException {
        JsonParser parser = new JsonParser();
        Pattern pattern = Pattern.compile("[A-Z_0-9]+");

        // Here we test for any Syntax errors in our wiki.json file
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream("wiki.json")))) {
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
