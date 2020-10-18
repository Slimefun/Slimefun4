package io.github.thebusybiscuit.slimefun4.testing.tests.registration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import be.seeseemelk.mockbukkit.MockBukkit;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.implementation.setup.PostSetup;
import io.github.thebusybiscuit.slimefun4.implementation.setup.SlimefunItemSetup;
import me.mrCookieSlime.Slimefun.Objects.Category;

@TestMethodOrder(value = OrderAnnotation.class)
class TestItemSetup {

    private static SlimefunPlugin plugin;

    @BeforeAll
    public static void load() {
        MockBukkit.mock();
        plugin = MockBukkit.load(SlimefunPlugin.class);
    }

    @AfterAll
    public static void unload() {
        MockBukkit.unmock();
    }

    @Test
    @Order(value = 1)
    @DisplayName("Test whether SlimefunItemSetup.setup() throws any Exceptions")
    void testForExceptions() {
        // Not really ideal but still important to test.
        // Item amount is variable, so we can't test for that.
        // We are really only concerned about any runtime exceptions here.
        Assertions.assertDoesNotThrow(() -> SlimefunItemSetup.setup(plugin));

        // Running it a second time should NOT be allowed.
        Assertions.assertThrows(UnsupportedOperationException.class, () -> SlimefunItemSetup.setup(plugin));
    }

    @Test
    @Order(value = 2)
    @DisplayName("Test whether PostSetup.setupWiki() throws any Exceptions")
    void testWikiSetup() {
        Assertions.assertDoesNotThrow(() -> PostSetup.setupWiki());
    }

    @Test
    @Order(value = 3)
    @DisplayName("Test whether every Category is added to the translation files")
    void testCategoryTranslations() throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/languages/categories_en.yml"), StandardCharsets.UTF_8))) {
            FileConfiguration config = YamlConfiguration.loadConfiguration(reader);

            for (Category category : SlimefunPlugin.getRegistry().getCategories()) {
                String path = category.getKey().getNamespace() + '.' + category.getKey().getKey();
                Assertions.assertTrue(config.contains(path));
            }
        }
    }
}
