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

class TestMultiItemOutput {

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
    void testMultiItemOutput() {
        SlimefunItem sfItem = TestUtilities.mockSlimefunItem(plugin, "SMALL_GOLD_INGOT", new ItemStack(Material.GOLD_INGOT));

        sfItem.register(plugin);

        MultiItemOutput multiComponent = new MultiItemOutput(
            new ItemOutput(new ItemStack(Material.GOLD_INGOT)),
            new ItemOutput(sfItem.getItem().clone())
        );

        Assertions.assertEquals(2, multiComponent.getOutputs().size());
        Assertions.assertTrue(multiComponent.generateOutputs().contains(new ItemStack(Material.GOLD_INGOT)));
        Assertions.assertTrue(multiComponent.generateOutputs().contains(sfItem.getItem()));
        Assertions.assertFalse(multiComponent.isDisabled());
        Assertions.assertEquals(1, multiComponent.getSlimefunItemIDs().size());
        Assertions.assertTrue(multiComponent.getSlimefunItemIDs().contains(sfItem.getId()));
    }
    
}
