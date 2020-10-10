package io.github.thebusybiscuit.slimefun4.testing.tests.tags;

import javax.annotation.Nonnull;

import org.bukkit.NamespacedKey;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import be.seeseemelk.mockbukkit.MockBukkit;
import io.github.thebusybiscuit.slimefun4.api.exceptions.TagMisconfigurationException;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.utils.tags.TagParser;

class TestTagParser {

    private static SlimefunPlugin plugin;
    private static NamespacedKey key;

    @BeforeAll
    public static void load() {
        MockBukkit.mock();
        plugin = MockBukkit.load(SlimefunPlugin.class);
        key = new NamespacedKey(plugin, "test");
    }

    @AfterAll
    public static void unload() {
        MockBukkit.unmock();
    }

    @Test
    @DisplayName("Test Keyed implementation")
    void testkey() {
        TagParser parser = new TagParser(key);
        Assertions.assertEquals(key, parser.getKey());
    }

    @Test
    @DisplayName("Test Nullability check")
    void testNullability() {
        TagParser parser = new TagParser(key);
        Assertions.assertThrows(IllegalArgumentException.class, () -> parser.parse(null, (a, b) -> {}));
    }

    @Test
    @DisplayName("Test JSON Parsing Error handling")
    void testInvalidJson() {
        assertMisconfiguration("");
        assertMisconfiguration("hello world");
    }

    @Test
    @DisplayName("Test no Arrays")
    void testMissingArray() {
        assertMisconfiguration("{}");
        assertMisconfiguration("{\"values\":\"derp\"}");
    }

    @Test
    @DisplayName("Test invalid Type")
    void testInvalidMaterial() {
        assertMisconfiguration("{\"values\":[123456]}");
    }

    @Test
    @DisplayName("Test invalid Materials")
    void testInvalidMaterials() {
        assertMisconfiguration("{\"values\":[\"NO\"]}");
        assertMisconfiguration("{\"values\":[\"lol:jk\"]}");
        assertMisconfiguration("{\"values\":[\"minecraft:no\"]}");
    }

    @Test
    @DisplayName("Test invalid Minecraft Tags")
    void testInvalidMinecraftTags() {
        assertMisconfiguration("{\"values\":[\"#minecraft:never_gonna_give_you_up\"]}");
    }

    @Test
    @DisplayName("Test invalid Slimefun Tags")
    void testInvalidSlimefunTags() {
        assertMisconfiguration("{\"values\":[\"#slimefun:never_gonna_give_you_up\"]}");
    }

    @Test
    @DisplayName("Test invalid Object elements")
    void testInvalidJSONObjects() {
        assertMisconfiguration("{\"values\":[{}]}");
        assertMisconfiguration("{\"values\":[{\"id\":123}]}");
        assertMisconfiguration("{\"values\":[{\"id\":\"wooh\"}]}");
        assertMisconfiguration("{\"values\":[{\"required\":false}]}");
        assertMisconfiguration("{\"values\":[{\"id\":\"wooh\",\"required\":\"wooh\"}]}");
        assertMisconfiguration("{\"values\":[{\"id\":\"wooh\",\"required\":true}]}");
    }

    private void assertMisconfiguration(@Nonnull String json) {
        TagParser parser = new TagParser(key);
        Assertions.assertThrows(TagMisconfigurationException.class, () -> parser.parse(json, (a, b) -> {}));
    }

}
