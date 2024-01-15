package io.github.thebusybiscuit.slimefun4.api.recipes.output;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import be.seeseemelk.mockbukkit.MockBukkit;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.test.TestUtilities;

class TestItemOutput {

    private static Slimefun plugin;

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
    void testItemOutput() {
        SlimefunItem sfItem = TestUtilities.mockSlimefunItem(plugin, "SMALL_IRON_INGOT", new ItemStack(Material.IRON_INGOT));

        sfItem.register(plugin);

        ItemOutput vanillaOutput = new ItemOutput(new ItemStack(Material.IRON_INGOT, 5));
        ItemOutput slimefunOutput = new ItemOutput(sfItem.getItem());

        Assertions.assertEquals(new ItemStack(Material.IRON_INGOT, 5), vanillaOutput.generateOutput());
        Assertions.assertFalse(vanillaOutput.isDisabled());
        Assertions.assertTrue(vanillaOutput.getSlimefunItemIDs().isEmpty());

        Assertions.assertEquals(sfItem.getItem(), slimefunOutput.generateOutput());
        Assertions.assertFalse(slimefunOutput.isDisabled());
        Assertions.assertEquals(1, slimefunOutput.getSlimefunItemIDs().size());
        Assertions.assertTrue(slimefunOutput.getSlimefunItemIDs().contains(sfItem.getId()));
    }
    
}
