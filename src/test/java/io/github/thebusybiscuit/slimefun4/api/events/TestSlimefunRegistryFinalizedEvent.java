package io.github.thebusybiscuit.slimefun4.api.events;

import org.junit.jupiter.api.Assertions;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockbukkit.mockbukkit.matcher.plugin.PluginManagerFiredEventFilterMatcher.hasFiredFilteredEvent;

import org.mockbukkit.mockbukkit.MockBukkit;
import org.mockbukkit.mockbukkit.ServerMock;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.implementation.setup.PostSetup;

class TestSlimefunRegistryFinalizedEvent {

    private static ServerMock server;
    private static Slimefun plugin;

    @BeforeAll
    public static void load() {
        server = MockBukkit.mock();
        plugin = MockBukkit.load(Slimefun.class);
    }

    @AfterAll
    public static void unload() {
        MockBukkit.unmock();
    }

    @Test
    @DisplayName("Test that SlimefunRegistryFinalizedEvent is fired")
    void testEventIsFired() {
        // Make sure post setup does not throw
        Assertions.assertDoesNotThrow(() -> PostSetup.loadItems());
        
        // Make sure post setup sent the event
        assertThat(server.getPluginManager(), hasFiredFilteredEvent(SlimefunItemRegistryFinalizedEvent.class, ignored -> true));
 
        server.getPluginManager().clearEvents();
    }
} 
