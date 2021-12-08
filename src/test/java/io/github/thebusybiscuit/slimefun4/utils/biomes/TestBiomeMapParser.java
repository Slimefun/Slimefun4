package io.github.thebusybiscuit.slimefun4.utils.biomes;

import java.util.function.Function;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.NamespacedKey;
import org.bukkit.block.Biome;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.google.gson.JsonElement;

import io.github.thebusybiscuit.slimefun4.api.exceptions.BiomeMapException;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;

import be.seeseemelk.mockbukkit.MockBukkit;

class TestBiomeMapParser {

    private static final Function<JsonElement, String> AS_STRING = JsonElement::getAsString;
    private static final Function<JsonElement, Integer> AS_INT = JsonElement::getAsInt;

    private static Slimefun plugin;
    private static NamespacedKey key;

    @BeforeAll
    public static void load() {
        MockBukkit.mock();
        plugin = MockBukkit.load(Slimefun.class);
        key = new NamespacedKey(plugin, "test");
    }

    @AfterAll
    public static void unload() {
        MockBukkit.unmock();
    }

    @Test
    @DisplayName("Test JSON Parsing Error handling")
    void testInvalidJson() {
        assertMisconfiguration(AS_STRING, "");
        assertMisconfiguration(AS_STRING, "1234");
        assertMisconfiguration(AS_STRING, "hello world");
        assertMisconfiguration(AS_STRING, "{}");
    }

    @Test
    @DisplayName("Test Array not having proper children")
    void testInvalidArrayChildren() {
        assertMisconfiguration(AS_STRING, "[1, 2, 3]");
        assertMisconfiguration(AS_STRING, "[[1], [2]]");
        assertMisconfiguration(AS_STRING, "[\"foo\", \"bar\"]");
    }

    @Test
    @DisplayName("Test Array entries being misconfigured")
    void testInvalidEntries() {
        assertMisconfiguration(AS_STRING, "[{}]");
        assertMisconfiguration(AS_STRING, "[{\"id\": \"one\"}]");
    }

    @Test
    @DisplayName("Test Array entries being incomplete")
    void testIncompleteEntries() {
        assertMisconfiguration(AS_STRING, "[{\"value\": \"cool\"}]");
        assertMisconfiguration(AS_STRING, "[{\"biomes\": []}]");
    }

    @Test
    @DisplayName("Test Biome entry being misconfigured")
    void testInvalidBiomeEntry() {
        assertMisconfiguration(AS_STRING, "[{\"value\": \"hello\", \"biomes\": {}}]");
        assertMisconfiguration(AS_STRING, "[{\"value\": \"hello\", \"biomes\": 1234}]");
        assertMisconfiguration(AS_STRING, "[{\"value\": \"hello\", \"biomes\": \"I am Groot.\"}]");
    }

    @Test
    @DisplayName("Test unknown Biomes")
    void testUnknownBiomes() {
        assertMisconfiguration(AS_INT, "[{\"value\": 1, \"biomes\": [1, 2, 3]}]");
        assertMisconfiguration(AS_INT, "[{\"value\": 1, \"biomes\": [\"hello world\"]}]");
        assertMisconfiguration(AS_INT, "[{\"value\": 1, \"biomes\": [\"slimefun:slime_forest\"]}]");
        assertMisconfiguration(AS_INT, "[{\"value\": 1, \"biomes\": [\"minecraft:walshy_desert\"]}]");
    }

    @Test
    @DisplayName("Test double Biome registration")
    void testDoubleBiomes() {
        Biome biome = Biome.OCEAN;

        assertMisconfiguration(AS_INT, "[{\"value\": 1, \"biomes\": [\"" + biome.getKey() + "\"]}, {\"value\": 2, \"biomes\": [\"" + biome.getKey() + "\"]}]");
    }

    @Test
    @DisplayName("Test different value converters")
    void testValueConversion() {
        Biome biome = Biome.OCEAN;

        String stringValue = "hello world";
        BiomeMap<String> stringMap = createBiomeMap(AS_STRING, "[{\"value\": \"" + stringValue + "\", \"biomes\": [\"" + biome.getKey() + "\"]}]");
        Assertions.assertEquals(stringValue, stringMap.get(biome));

        int intValue = 1024;
        BiomeMap<Integer> intMap = createBiomeMap(AS_INT, "[{\"value\": \"" + intValue + "\", \"biomes\": [\"" + biome.getKey() + "\"]}]");
        Assertions.assertEquals(intValue, intMap.get(biome));
    }

    @Test
    @DisplayName("Test working BiomeMap from JSON")
    void testBiomeMapFromJson() {
        Biome biome1 = Biome.JUNGLE;
        Biome biome2 = Biome.OCEAN;
        Biome biome3 = Biome.DESERT;
        BiomeMap<Integer> biomes = createBiomeMap(AS_INT, "[{\"value\":1,\"biomes\":[\"" + biome1.getKey() + "\", \"" + biome2.getKey() + "\"]}, {\"value\":2, \"biomes\":[\"" + biome3.getKey() + "\"]}]");

        Assertions.assertTrue(biomes.contains(biome1));
        Assertions.assertEquals(1, biomes.get(biome1));

        Assertions.assertTrue(biomes.contains(biome2));
        Assertions.assertEquals(1, biomes.get(biome2));

        Assertions.assertTrue(biomes.contains(biome3));
        Assertions.assertEquals(2, biomes.get(biome3));
    }

    @Test
    @DisplayName("Test working BiomeMap manually")
    void testManualBiomeMap() {
        BiomeMap<String> biomes = new BiomeMap<>(key);
        Biome biome = Biome.OCEAN;
        String value = "Under the sea";

        Assertions.assertTrue(biomes.put(biome, value));
        Assertions.assertTrue(biomes.contains(biome));
        Assertions.assertEquals(value, biomes.get(biome));

        Assertions.assertTrue(biomes.remove(biome));
        Assertions.assertFalse(biomes.contains(biome));
    }

    @ParametersAreNonnullByDefault
    private @Nonnull <T> BiomeMap<T> createBiomeMap(Function<JsonElement, T> function, String json) {
        BiomeMapParser<T> parser = new BiomeMapParser<>(key, function);

        try {
            parser.read(json);
        } catch (BiomeMapException e) {
            Assertions.fail(e);
        }

        return parser.buildBiomeMap();
    }

    @ParametersAreNonnullByDefault
    private <T> void assertMisconfiguration(Function<JsonElement, T> function, String json) {
        BiomeMapParser<T> parser = new BiomeMapParser<>(key, function);
        Assertions.assertThrows(BiomeMapException.class, () -> parser.read(json));
    }

}
