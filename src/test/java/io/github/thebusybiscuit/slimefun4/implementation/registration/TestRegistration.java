package io.github.thebusybiscuit.slimefun4.implementation.registration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

import javax.annotation.Nonnull;

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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.ItemState;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.groups.FlexItemGroup;
import io.github.thebusybiscuit.slimefun4.api.researches.Research;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.implementation.setup.PostSetup;
import io.github.thebusybiscuit.slimefun4.implementation.setup.ResearchSetup;
import io.github.thebusybiscuit.slimefun4.implementation.setup.SlimefunItemSetup;

import be.seeseemelk.mockbukkit.MockBukkit;

@TestMethodOrder(value = OrderAnnotation.class)
class TestRegistration {

    private static Slimefun plugin;

    @BeforeAll
    public static void load() {
        MockBukkit.mock();
        plugin = MockBukkit.load(Slimefun.class);
    }

    @AfterAll
    public static void unload() {
        MockBukkit.unmock();
    }

    @Test
    @Order(value = 1)
    @DisplayName("Test whether SlimefunItemSetup.setup() throws any Exceptions")
    void testItemRegistration() {
        // Not really ideal but still important to test.
        // Item amount is variable, so we can't test for that.
        // We are really only concerned about any runtime exceptions here.
        Assertions.assertDoesNotThrow(() -> SlimefunItemSetup.setup(plugin));

        // Running it a second time should NOT be allowed.
        Assertions.assertThrows(UnsupportedOperationException.class, () -> SlimefunItemSetup.setup(plugin));
    }

    @Order(value = 2)
    @MethodSource("allItems")
    @ParameterizedTest(name = "Assert that {0} is enabled")
    void testNoDisabledItems(@Nonnull SlimefunItem item) {
        Assertions.assertNotEquals(ItemState.UNREGISTERED, item.getState(), item.toString() + " was not registered?");
    }

    @Test
    @Order(value = 3)
    @DisplayName("Test whether PostSetup.setupWiki() throws any Exceptions")
    void testWikiSetup() {
        Assertions.assertDoesNotThrow(() -> PostSetup.setupWiki());
    }

    @Test
    @Order(value = 4)
    @DisplayName("Test whether every ItemGroup is added to the translation files")
    void testItemGroupTranslations() throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/languages/en/categories.yml"), StandardCharsets.UTF_8))) {
            FileConfiguration config = YamlConfiguration.loadConfiguration(reader);

            for (ItemGroup itemGroup : Slimefun.getRegistry().getAllItemGroups()) {
                if (itemGroup instanceof FlexItemGroup) {
                    // Skip flex groups
                    continue;
                }

                String path = itemGroup.getKey().getNamespace() + '.' + itemGroup.getKey().getKey();
                Assertions.assertTrue(config.contains(path));
            }
        }
    }

    @Test
    @Order(value = 5)
    @DisplayName("Test if ResearchSetup throws an Exception")
    void testResearchRegistration() {
        // Not really ideal but still important to test.
        // Research amount is variable, so we can't test for that.
        // We are really only concerned about any runtime exceptions here.
        Slimefun.getRegistry().setResearchingEnabled(true);

        // It is important that this is run after item registration
        Assertions.assertDoesNotThrow(() -> ResearchSetup.setupResearches());

        // Running it a second time should NOT be allowed.
        Assertions.assertThrows(UnsupportedOperationException.class, () -> ResearchSetup.setupResearches());
    }

    @Test
    @Order(value = 6)
    void testPostSetup() {
        Assertions.assertDoesNotThrow(() -> PostSetup.loadItems());
    }

    @Test
    @Order(value = 7)
    @DisplayName("Test if all researches have a translation")
    void testResearchTranslations() throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/languages/en/researches.yml"), StandardCharsets.UTF_8))) {
            FileConfiguration config = YamlConfiguration.loadConfiguration(reader);

            for (Research research : Slimefun.getRegistry().getResearches()) {
                String path = research.getKey().getNamespace() + '.' + research.getKey().getKey();
                Assertions.assertTrue(config.contains(path));
            }
        }
    }

    @Order(value = 8)
    @MethodSource("allResearches")
    @ParameterizedTest(name = "Test if {0} has items assigned")
    void testForEmptyResearches(@Nonnull Research research) throws IOException {
        // This test is related to Issue #3368
        Assertions.assertFalse(research.getAffectedItems().isEmpty());
    }

    private static @Nonnull Stream<Arguments> allItems() {
        return Slimefun.getRegistry().getAllSlimefunItems().stream().map(Arguments::of);
    }

    private static @Nonnull Stream<Arguments> allResearches() {
        return Slimefun.getRegistry().getResearches().stream().map(Arguments::of);
    }
}
