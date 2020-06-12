package io.github.thebusybiscuit.slimefun4.testing.tests.commands;

import org.bukkit.entity.Player;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;

public class TestDebugFishCommand {

    private static ServerMock server;

    @BeforeAll
    public static void load() {
        server = MockBukkit.mock();
        MockBukkit.load(SlimefunPlugin.class);
    }

    @AfterAll
    public static void unload() {
        MockBukkit.unmock();
    }

    @ParameterizedTest
    @ValueSource(booleans = { true, false })
    public void testCommand(boolean op) {
        Player player = server.addPlayer();
        player.setOp(op);
        server.execute("slimefun", player, "debug_fish").assertSucceeded();

        Assertions.assertEquals(op, SlimefunUtils.containsSimilarItem(player.getInventory(), SlimefunItems.DEBUG_FISH, true));
    }
}
