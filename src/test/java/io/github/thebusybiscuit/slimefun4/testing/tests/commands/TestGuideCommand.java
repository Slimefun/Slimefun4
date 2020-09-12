package io.github.thebusybiscuit.slimefun4.testing.tests.commands;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import io.github.thebusybiscuit.slimefun4.core.guide.SlimefunGuide;
import io.github.thebusybiscuit.slimefun4.core.guide.SlimefunGuideLayout;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;

class TestGuideCommand {

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
    @DisplayName("Test if Slimefun Guide is given on /sf guide")
    @ValueSource(booleans = { true, false })
    void testCommand(boolean op) {
        Player player = server.addPlayer();
        player.setOp(op);
        server.execute("slimefun", player, "guide").assertSucceeded();

        ItemStack guide = SlimefunGuide.getItem(SlimefunGuideLayout.CHEST);
        Assertions.assertEquals(op, SlimefunUtils.containsSimilarItem(player.getInventory(), guide, true));
    }
}
