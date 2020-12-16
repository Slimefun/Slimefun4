package io.github.thebusybiscuit.slimefun4.testing.tests.commands;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.testing.TestUtilities;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

class DropCommandTest {

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

    @Test
    @DisplayName("Test if /sf drop command drops an item at the location")
    void testCommand() {
        Category category = TestUtilities.getCategory(plugin, "testcategory");
        SlimefunItemStack TEST_ITEM = new SlimefunItemStack("DROP_COMMAND_TEST_ITEM", Material.REDSTONE_BLOCK, "Drop Test Item");
        new SlimefunItem(category, TEST_ITEM, RecipeType.NULL, new ItemStack[9]).register(plugin);
        server.addSimpleWorld("testworld");

        server.executeConsole("slimefun", "drop", "testworld", "0", "0", "0", "DROP_COMMAND_TEST_ITEM", "1");
        for (Entity en : server.getWorld("testworld").getEntities()) {
            Assertions.assertEquals(SlimefunItem.getByItem(((Item) en).getItemStack()), TEST_ITEM.getItem());
        }
    }
}