package io.github.thebusybiscuit.slimefun4.implementation.items.backpacks;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.test.TestUtilities;
import io.github.thebusybiscuit.slimefun4.test.presets.SlimefunItemTest;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

class TestEnderBackpack implements SlimefunItemTest<EnderBackpack> {

    private static ServerMock server;
    private static SlimefunPlugin plugin;

    @BeforeAll
    public static void load() {
        server = MockBukkit.mock();
        plugin = MockBukkit.load(SlimefunPlugin.class);
    }

    @AfterAll
    public static void unload() {
        MockBukkit.unmock();
    }

    @Override
    public EnderBackpack registerSlimefunItem(SlimefunPlugin plugin, String id) {
        SlimefunItemStack item = new SlimefunItemStack(id, Material.ENDER_CHEST, "&5Test Ender Backpack");
        EnderBackpack backpack = new EnderBackpack(TestUtilities.getCategory(plugin, "ender_backpack"), item, RecipeType.NULL, new ItemStack[9]);
        backpack.register(plugin);
        return backpack;
    }

    @Test
    @DisplayName("Test Ender Backpack opening Enderchest")
    void testRightClickBehaviour() {
        Player player = server.addPlayer();
        EnderBackpack backpack = registerSlimefunItem(plugin, "TEST_ENDER_BACKPACK");

        simulateRightClick(player, backpack);

        // We expect the Enderchest to be open now
        Assertions.assertEquals(player.getEnderChest(), player.getOpenInventory().getTopInventory());
    }

}
