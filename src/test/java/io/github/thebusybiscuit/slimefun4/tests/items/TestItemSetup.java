package io.github.thebusybiscuit.slimefun4.tests.items;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import io.github.thebusybiscuit.slimefun4.implementation.setup.SlimefunItemSetup;
import io.github.thebusybiscuit.slimefun4.mocks.TestUtilities;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;

public class TestItemSetup {

    private static SlimefunPlugin plugin;

    @BeforeAll
    public static void load() {
        ServerMock server = MockBukkit.mock();
        plugin = MockBukkit.load(SlimefunPlugin.class);
        TestUtilities.registerDefaultTags(server);
    }

    @AfterAll
    public static void unload() {
        MockBukkit.unmock();
    }

    @Test
    public void testForExceptions() {
        // Not really ideal but still important to test.
        // Item amount is variable, so we can't test for that.
        // We are really only concerned about any runtime exceptions here.
        Assertions.assertDoesNotThrow(() -> SlimefunItemSetup.setup(plugin));

        // Running it a second time should NOT be allowed.
        Assertions.assertThrows(UnsupportedOperationException.class, () -> SlimefunItemSetup.setup(plugin));
    }
}
