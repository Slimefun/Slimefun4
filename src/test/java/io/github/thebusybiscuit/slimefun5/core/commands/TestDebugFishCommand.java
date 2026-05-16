package io.github.thebusybiscuit.slimefun5.core.commands;

import org.bukkit.entity.Player;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun5.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun5.utils.SlimefunUtils;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockbukkit.mockbukkit.matcher.command.CommandResultSucceedMatcher.hasSucceeded;

import org.mockbukkit.mockbukkit.MockBukkit;
import org.mockbukkit.mockbukkit.ServerMock;

class TestDebugFishCommand {

    private static ServerMock server;

    @BeforeAll
    public static void load() {
        server = MockBukkit.mock();
        MockBukkit.load(Slimefun.class);
    }

    @AfterAll
    public static void unload() {
        MockBukkit.unmock();
    }

    @ParameterizedTest
    @ValueSource(booleans = { true, false })
    @DisplayName("Test if Debug Fish is given on /sf debug_fish")
    void testCommand(boolean op) {
        Player player = server.addPlayer();
        player.setOp(op);
        assertThat(server.execute("slimefun", player, "debug_fish"), hasSucceeded());

        Assertions.assertEquals(op, SlimefunUtils.containsSimilarItem(player.getInventory(), SlimefunItems.DEBUG_FISH.item(), true));
    }
}

