package io.github.thebusybiscuit.slimefun4.utils.biomes;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.NamespacedKey;
import org.bukkit.block.Biome;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.google.gson.JsonElement;

import io.github.thebusybiscuit.slimefun4.api.exceptions.BiomeMapException;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;

import be.seeseemelk.mockbukkit.MockBukkit;

class TestBiomeMapParser {

    private static final BiomeDataConverter<String> AS_STRING = JsonElement::getAsString;
    private static final BiomeDataConverter<Integer> AS_INT = JsonElement::getAsInt;

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
    @DisplayName("Test BiomeMapParser being not lenient by default")
    void testParserNotLenientByDefault() {
        BiomeMapParser<Integer> parser = new BiomeMapParser<>(key, AS_INT);

        // Test default value
        Assertions.assertFalse(parser.isLenient());
    }

    @ParameterizedTest
    @ValueSource(booleans = { true, false })
    @DisplayName("Test lenient parser")
    void testLenientParsing(boolean isLenient) {
        BiomeMapParser<Integer> parser = new BiomeMapParser<>(key, AS_INT);

        parser.setLenient(isLenient);
        Assertions.assertEquals(isLenient, parser.isLenient());

        /*
         * I picked a random String here.
         * If this biome will exist in Minecraft one day... then you saw it here first, folks ;D
         */
        String json = "[{\"value\": 2048, \"biomes\": [\"minecraft:thebusybiscuits_wonderland\"]}]";

        if (isLenient) {
            Assertions.assertDoesNotThrow(() -> parser.read(json));
        } else {
            Assertions.assertThrows(BiomeMapException.class, () -> parser.read(json));
        }
    }

    @ParametersAreNonnullByDefault
    private <T> void assertMisconfiguration(BiomeDataConverter<T> function, String json) {
        BiomeMapParser<T> parser = new BiomeMapParser<>(key, function);
        Assertions.assertThrows(BiomeMapException.class, () -> parser.read(json));
    }

}
