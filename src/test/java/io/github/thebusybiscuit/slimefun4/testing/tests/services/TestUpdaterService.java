package io.github.thebusybiscuit.slimefun4.testing.tests.services;

import java.io.File;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import be.seeseemelk.mockbukkit.MockBukkit;
import io.github.thebusybiscuit.slimefun4.api.SlimefunBranch;
import io.github.thebusybiscuit.slimefun4.core.services.UpdaterService;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;

public class TestUpdaterService {

    private static SlimefunPlugin plugin;

    private final File file = new File("test.jar");

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
    public void testDevelopmentBuilds() {
        UpdaterService service = new UpdaterService(plugin, "DEV - 131 (git 123456)", file);
        Assertions.assertEquals(SlimefunBranch.DEVELOPMENT, service.getBranch());
        Assertions.assertTrue(service.getBranch().isOfficial());
        // Cannot currently be tested... yay
        // Assertions.assertEquals(131, service.getBuildNumber());
    }

    @Test
    public void testStableBuilds() {
        UpdaterService service = new UpdaterService(plugin, "RC - 6 (git 123456)", file);
        Assertions.assertEquals(SlimefunBranch.STABLE, service.getBranch());
        Assertions.assertTrue(service.getBranch().isOfficial());
        // Cannot currently be tested... yay
        // Assertions.assertEquals(6, service.getBuildNumber());
    }

    @Test
    public void testUnofficialBuilds() {
        UpdaterService service = new UpdaterService(plugin, "4.20 UNOFFICIAL", file);
        Assertions.assertEquals(SlimefunBranch.UNOFFICIAL, service.getBranch());
        Assertions.assertFalse(service.getBranch().isOfficial());
        Assertions.assertEquals(-1, service.getBuildNumber());
    }

    @Test
    public void testUnknownBuilds() {
        UpdaterService service = new UpdaterService(plugin, "I am special", file);
        Assertions.assertEquals(SlimefunBranch.UNKNOWN, service.getBranch());
        Assertions.assertFalse(service.getBranch().isOfficial());
        Assertions.assertEquals(-1, service.getBuildNumber());
    }
}
