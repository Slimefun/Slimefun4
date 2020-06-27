package io.github.thebusybiscuit.slimefun4.testing.tests;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import be.seeseemelk.mockbukkit.MockBukkit;
import io.github.thebusybiscuit.slimefun4.api.MinecraftVersion;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;

public class TestPluginClass {

    @BeforeAll
    public static void load() {
        MockBukkit.mock();
        MockBukkit.load(SlimefunPlugin.class);
    }

    @AfterAll
    public static void unload() {
        MockBukkit.unmock();
    }

    @Test
    public void verifyTestEnvironment() {
        MinecraftVersion version = SlimefunPlugin.getMinecraftVersion();

        Assertions.assertEquals(MinecraftVersion.UNIT_TEST, version);
        Assertions.assertEquals("Unit Test Environment", version.getName());
    }

    @Test
    public void testConfigs() {
        Assertions.assertNotNull(SlimefunPlugin.getCfg());
        Assertions.assertNotNull(SlimefunPlugin.getResearchCfg());
        Assertions.assertNotNull(SlimefunPlugin.getItemCfg());
    }

    @Test
    public void testGetters() {
        Assertions.assertNotNull(SlimefunPlugin.getTickerTask());
        Assertions.assertNotNull(SlimefunPlugin.getVersion());
        Assertions.assertNotNull(SlimefunPlugin.getRegistry());
        Assertions.assertNotNull(SlimefunPlugin.getCommand());
    }

    @Test
    public void testServicesNotNull() {
        Assertions.assertNotNull(SlimefunPlugin.getLocalization());
        Assertions.assertNotNull(SlimefunPlugin.getMinecraftRecipeService());
        Assertions.assertNotNull(SlimefunPlugin.getItemDataService());
        Assertions.assertNotNull(SlimefunPlugin.getItemTextureService());
        Assertions.assertNotNull(SlimefunPlugin.getPermissionsService());
        Assertions.assertNotNull(SlimefunPlugin.getBlockDataService());
        Assertions.assertNotNull(SlimefunPlugin.getThirdPartySupportService());
        Assertions.assertNotNull(SlimefunPlugin.getWorldSettingsService());
        Assertions.assertNotNull(SlimefunPlugin.getGitHubService());
        Assertions.assertNotNull(SlimefunPlugin.getUpdater());
    }

    @Test
    public void testListenersNotNull() {
        Assertions.assertNotNull(SlimefunPlugin.getAncientAltarListener());
        Assertions.assertNotNull(SlimefunPlugin.getGrapplingHookListener());
        Assertions.assertNotNull(SlimefunPlugin.getBackpackListener());
        Assertions.assertNotNull(SlimefunPlugin.getBowListener());
    }

}
