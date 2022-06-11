package io.github.thebusybiscuit.slimefun4.utils.biomes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import io.github.thebusybiscuit.slimefun4.api.MinecraftVersion;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.utils.JsonUtils;

import be.seeseemelk.mockbukkit.MockBukkit;

/**
 * This test checks if biome maps work across multiple versions of Minecraft.
 * A similar test can be written for Material Tags :?
 * 
 * @author TheBusyBiscuit
 *
 */
class TestBiomeMapCompatibility {

    private static final Map<MinecraftVersion, JsonArray> compatibilityMap = new EnumMap<>(MinecraftVersion.class);

    @BeforeAll
    public static void load() {
        MockBukkit.mock();

        for (MinecraftVersion version : MinecraftVersion.values()) {
            if (!version.isVirtual()) {
                loadBiomes(version);
            }
        }
    }

    @AfterAll
    public static void unload() {
        MockBukkit.unmock();
    }

    private static void loadBiomes(@Nonnull MinecraftVersion version) {
        String path = "/biomes/" + version.getName() + ".json";

        if (Slimefun.class.getResource(path) == null) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(Slimefun.class.getResourceAsStream(path), StandardCharsets.UTF_8))) {
            JsonArray biomes = JsonUtils.parseString(reader.lines().collect(Collectors.joining(""))).getAsJsonArray();

            compatibilityMap.put(version, biomes);
        } catch (IOException x) {
            x.printStackTrace();
        }
    }

    @ParameterizedTest(name = "Test if {0} has a biome resource")
    @EnumSource(value = MinecraftVersion.class)
    void testMissingBiomeResources(@Nonnull MinecraftVersion version) {
        if (!version.isVirtual()) {
            Assertions.assertTrue(compatibilityMap.containsKey(version));
        }
    }

    @ParameterizedTest(name = "Test if biome-map {0}.json is compatible with {1}")
    @MethodSource("biomeMaps")
    @ParametersAreNonnullByDefault
    void testCompatibilities(String name, MinecraftVersion version) {
        assertCompatibility(name, version);
    }

    /**
     * We manually specify here which biome map works on which minecraft versions.
     * 
     * @return A {@link Stream} of {@link Arguments} for our unit test.
     */
    private static @Nonnull Stream<Arguments> biomeMaps() {
        Map<String, MinecraftVersion[]> testCases = new HashMap<>();

        // @formatter:off
        testCases.put("nether_ice_v1.14", new MinecraftVersion[] {
            MinecraftVersion.MINECRAFT_1_14,
            MinecraftVersion.MINECRAFT_1_15
        });
        
        testCases.put("nether_ice_v1.16", new MinecraftVersion[] {
            MinecraftVersion.MINECRAFT_1_16,
            MinecraftVersion.MINECRAFT_1_17,
            MinecraftVersion.MINECRAFT_1_18,
            MinecraftVersion.MINECRAFT_1_19
        });
        
        testCases.put("oil_v1.14", new MinecraftVersion[] {
            MinecraftVersion.MINECRAFT_1_14,
            MinecraftVersion.MINECRAFT_1_15,
            MinecraftVersion.MINECRAFT_1_16,
            MinecraftVersion.MINECRAFT_1_17
        });
        
        testCases.put("oil_v1.18", new MinecraftVersion[] {
            MinecraftVersion.MINECRAFT_1_18,
            MinecraftVersion.MINECRAFT_1_19
        });
        
        testCases.put("salt_v1.14", new MinecraftVersion[] {
            MinecraftVersion.MINECRAFT_1_14,
            MinecraftVersion.MINECRAFT_1_15,
            MinecraftVersion.MINECRAFT_1_16,
            MinecraftVersion.MINECRAFT_1_17
        });
        
        testCases.put("salt_v1.18", new MinecraftVersion[] {
            MinecraftVersion.MINECRAFT_1_18,
            MinecraftVersion.MINECRAFT_1_19
        });
        
        testCases.put("uranium_v1.14", new MinecraftVersion[] {
            MinecraftVersion.MINECRAFT_1_14,
            MinecraftVersion.MINECRAFT_1_15
        });
        
        testCases.put("uranium_v1.16", new MinecraftVersion[] {
            MinecraftVersion.MINECRAFT_1_16
        });
        
        testCases.put("uranium_v1.17", new MinecraftVersion[] {
            MinecraftVersion.MINECRAFT_1_17
        });
        
        testCases.put("uranium_v1.18", new MinecraftVersion[] {
            MinecraftVersion.MINECRAFT_1_18,
            MinecraftVersion.MINECRAFT_1_19
        });
        // @formatter:on

        return testCases.entrySet().stream().flatMap(entry -> {
            return Arrays.stream(entry.getValue()).map(version -> Arguments.of(entry.getKey(), version));
        });
    }

    @ParametersAreNonnullByDefault
    private void assertCompatibility(String name, MinecraftVersion version) {
        JsonArray allBiomes = compatibilityMap.get(version);

        String path = "/biome-maps/" + name + ".json";
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(Slimefun.class.getResourceAsStream(path), StandardCharsets.UTF_8))) {
            JsonArray root = JsonUtils.parseString(reader.lines().collect(Collectors.joining(""))).getAsJsonArray();

            for (JsonElement element : root) {
                JsonArray biomes = element.getAsJsonObject().getAsJsonArray("biomes");

                checkBiomes(biomes, allBiomes, version);
            }
        } catch (IOException x) {
            Assertions.fail(x);
        }
    }

    @ParametersAreNonnullByDefault
    private void checkBiomes(JsonArray biomes, JsonArray allBiomes, MinecraftVersion version) {
        for (JsonElement biome : biomes) {
            /*
             * Assert that this biome exists within the list of all biomes
             * for this version of Minecraft.
             */
            Assertions.assertTrue(allBiomes.contains(biome), "Biome \"" + biome.getAsString() + "\" does not exist on Minecraft " + version.getName());
        }
    }

}
