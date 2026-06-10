package io.github.thebusybiscuit.slimefun5.core.commands;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import io.github.thebusybiscuit.slimefun5.core.guide.SlimefunGuide;
import io.github.thebusybiscuit.slimefun5.core.guide.SlimefunGuideMode;
import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun5.utils.SlimefunUtils;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockbukkit.mockbukkit.matcher.command.CommandResultSucceedMatcher.hasSucceeded;

import org.mockbukkit.mockbukkit.MockBukkit;
import org.mockbukkit.mockbukkit.ServerMock;

class TestGuideCommand {

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
    @DisplayName("Test if Slimefun Guide is given on /sf guide")
    @ValueSource(booleans = { true, false })
    void testCommand(boolean op) {
        Player player = server.addPlayer();
        player.setOp(op);
        boolean hasPermission = player.hasPermission("slimefun.command.guide");
        assertThat(server.execute("slimefun", player, "guide"), hasSucceeded());
        ItemStack guide = SlimefunGuide.getItem(SlimefunGuideMode.SURVIVAL_MODE);
        Assertions.assertEquals(hasPermission, SlimefunUtils.containsSimilarItem(player.getInventory(), guide, true));
    }
}

