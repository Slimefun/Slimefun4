package io.github.thebusybiscuit.slimefun4.core.services;

import java.io.File;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.github.bakedlibs.dough.updater.PluginUpdater;
import io.github.bakedlibs.dough.versions.PrefixedVersion;
import io.github.thebusybiscuit.slimefun4.api.SlimefunBranch;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;

import org.mockbukkit.mockbukkit.MockBukkit;
import org.mockito.Mockito;

class TestUpdaterService {

    private static Slimefun plugin;

    private final File file = new File("test.jar");

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
    @DisplayName("Test if the development branch is recognized correctly")
    void testDevelopmentBuilds() {
        UpdaterService service = new UpdaterService(plugin, "Dev - 131 (git 123456)", file);
        Assertions.assertEquals(SlimefunBranch.DEVELOPMENT, service.getBranch());
        Assertions.assertTrue(service.getBranch().isOfficial());
        Assertions.assertEquals(131, service.getBuildNumber());
    }

    @Test
    @DisplayName("Test if the stable branch is recognized correctly")
    void testStableBuilds() {
        UpdaterService service = new UpdaterService(plugin, "RC - 6 (git 123456)", file);
        Assertions.assertEquals(SlimefunBranch.STABLE, service.getBranch());
        Assertions.assertTrue(service.getBranch().isOfficial());
        Assertions.assertEquals(6, service.getBuildNumber());
    }

    @Test
    @DisplayName("Test build parsing with invalid number")
    void testInvalidBuildNumber() {
        UpdaterService service = new UpdaterService(plugin, "Dev - abc", file);
        Assertions.assertEquals(SlimefunBranch.DEVELOPMENT, service.getBranch());
        Assertions.assertEquals(-1, service.getBuildNumber());
    }

    @Test
    @DisplayName("Test if an unofficial build is recognized correctly")
    void testUnofficialBuilds() {
        UpdaterService service = new UpdaterService(plugin, "4.20 UNOFFICIAL", file);
        Assertions.assertEquals(SlimefunBranch.UNOFFICIAL, service.getBranch());
        Assertions.assertFalse(service.getBranch().isOfficial());
        Assertions.assertEquals(-1, service.getBuildNumber());
    }

    @Test
    @DisplayName("Test if unknown builds are caught")
    void testUnknownBuilds() {
        UpdaterService service = new UpdaterService(plugin, "I am special", file);
        Assertions.assertEquals(SlimefunBranch.UNKNOWN, service.getBranch());
        Assertions.assertFalse(service.getBranch().isOfficial());
        Assertions.assertEquals(-1, service.getBuildNumber());
    }

    @Test
    @DisplayName("Test if auto-update config is respected")
    void testAutoUpdateConfig() {
        PluginUpdater<PrefixedVersion> updater = Mockito.mock(PluginUpdater.class);
        UpdaterService service = new UpdaterService(plugin, updater, SlimefunBranch.DEVELOPMENT);

        Slimefun.getCfg().setValue("options.auto-update", false);
        Assertions.assertFalse(service.isEnabled());

        Slimefun.getCfg().setValue("options.auto-update", true);
        Assertions.assertTrue(service.isEnabled());
    }

    @Test
    @DisplayName("Test getting the latest version")
    void testGetLatestVersion() {
        PluginUpdater<PrefixedVersion> updater = Mockito.mock(PluginUpdater.class);
        PrefixedVersion version = Mockito.mock(PrefixedVersion.class);
        Mockito.when(version.getVersionNumber()).thenReturn(42);
        java.util.concurrent.CompletableFuture<PrefixedVersion> future = java.util.concurrent.CompletableFuture.completedFuture(version);
        Mockito.when(updater.getLatestVersion()).thenReturn(future);

        UpdaterService service = new UpdaterService(plugin, updater, SlimefunBranch.DEVELOPMENT);
        Assertions.assertEquals(42, service.getLatestVersion());
    }
}
