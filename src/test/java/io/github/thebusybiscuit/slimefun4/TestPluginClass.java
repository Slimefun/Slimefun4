package io.github.thebusybiscuit.slimefun4;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.github.thebusybiscuit.slimefun4.api.MinecraftVersion;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;

import be.seeseemelk.mockbukkit.MockBukkit;

class TestPluginClass {

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
    @DisplayName("Verify that we are in a UNIT_TEST environment")
    void verifyTestEnvironment() {
        MinecraftVersion version = SlimefunPlugin.getMinecraftVersion();

        Assertions.assertTrue(plugin.isUnitTest());
        Assertions.assertEquals(MinecraftVersion.UNIT_TEST, version);
        Assertions.assertEquals("Unit Test Environment", version.getName());
    }

    @Test
    @DisplayName("Verify that config files were loaded")
    void testConfigs() {
        Assertions.assertNotNull(SlimefunPlugin.getCfg());
        Assertions.assertNotNull(SlimefunPlugin.getResearchCfg());
        Assertions.assertNotNull(SlimefunPlugin.getItemCfg());
    }

    @Test
    @DisplayName("Test some static Getters")
    void testGetters() {
        Assertions.assertNotNull(SlimefunPlugin.getTickerTask());
        Assertions.assertNotNull(SlimefunPlugin.getVersion());
        Assertions.assertNotNull(SlimefunPlugin.getRegistry());
        Assertions.assertNotNull(SlimefunPlugin.getCommand());
        Assertions.assertNotNull(SlimefunPlugin.getGPSNetwork());
        Assertions.assertNotNull(SlimefunPlugin.getNetworkManager());
        Assertions.assertNotNull(SlimefunPlugin.getProfiler());
    }

    @Test
    @DisplayName("Test some Services being not-null")
    void testServicesNotNull() {
        Assertions.assertNotNull(SlimefunPlugin.getLocalization());
        Assertions.assertNotNull(SlimefunPlugin.getMinecraftRecipeService());
        Assertions.assertNotNull(SlimefunPlugin.getItemDataService());
        Assertions.assertNotNull(SlimefunPlugin.getItemTextureService());
        Assertions.assertNotNull(SlimefunPlugin.getPermissionsService());
        Assertions.assertNotNull(SlimefunPlugin.getBlockDataService());
        Assertions.assertNotNull(SlimefunPlugin.getIntegrations());
        Assertions.assertNotNull(SlimefunPlugin.getWorldSettingsService());
        Assertions.assertNotNull(SlimefunPlugin.getGitHubService());
        Assertions.assertNotNull(SlimefunPlugin.getUpdater());
        Assertions.assertNotNull(SlimefunPlugin.getMetricsService());
    }

    @Test
    @DisplayName("Test some Listeners being not-null")
    void testListenersNotNull() {
        Assertions.assertNotNull(SlimefunPlugin.getGrapplingHookListener());
        Assertions.assertNotNull(SlimefunPlugin.getBackpackListener());
        Assertions.assertNotNull(SlimefunPlugin.getBowListener());
    }

}
