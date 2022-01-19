package io.github.thebusybiscuit.slimefun4.utils.biomes;

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

class TestBiomeMap {

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
    @DisplayName("Test BiomeMap#getKey")
    void testKeyed() {
        BiomeMap<String> biomes = new BiomeMap<>(key);
        Assertions.assertEquals(key, biomes.getKey());
    }

    @Test
    @DisplayName("Test BiomeMap#toString")
    void testToString() {
        BiomeMap<String> biomes = new BiomeMap<>(key);
        Assertions.assertTrue(biomes.toString().startsWith("BiomeMap"));
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

        Assertions.assertTrue(biomes.containsKey(biome1));
        Assertions.assertEquals(1, biomes.get(biome1));

        Assertions.assertTrue(biomes.containsKey(biome2));
        Assertions.assertEquals(1, biomes.get(biome2));

        Assertions.assertTrue(biomes.containsKey(biome3));
        Assertions.assertEquals(2, biomes.get(biome3));
    }

    @Test
    @DisplayName("Test working BiomeMap manually")
    void testManualBiomeMap() {
        BiomeMap<String> biomes = new BiomeMap<>(key);
        Biome biome = Biome.OCEAN;
        String value = "Under the sea";

        Assertions.assertTrue(biomes.put(biome, value));
        Assertions.assertTrue(biomes.containsKey(biome));
        Assertions.assertEquals(value, biomes.get(biome));

        Assertions.assertTrue(biomes.remove(biome));
        Assertions.assertFalse(biomes.containsKey(biome));
    }

    @ParametersAreNonnullByDefault
    private @Nonnull <T> BiomeMap<T> createBiomeMap(BiomeDataConverter<T> function, String json) {
        BiomeMapParser<T> parser = new BiomeMapParser<>(key, function);

        try {
            parser.read(json);
        } catch (BiomeMapException e) {
            Assertions.fail(e);
        }

        return parser.buildBiomeMap();
    }

}
