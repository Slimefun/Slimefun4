package io.github.thebusybiscuit.slimefun4.api.recipes.output;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import be.seeseemelk.mockbukkit.MockBukkit;
import io.github.bakedlibs.dough.collections.RandomizedSet;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.test.TestUtilities;

class TestRandomItemOutput {

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
    void testRandomItemOutput() {
        SlimefunItem sfItem = TestUtilities.mockSlimefunItem(plugin, "RANDOM_GOLD_INGOT", new ItemStack(Material.GOLD_INGOT));

        sfItem.register(plugin);

        RandomizedSet<ItemStack> output = new RandomizedSet<>();
        output.add(new ItemStack(Material.GOLD_INGOT), 2);
        output.add(sfItem.getItem().clone(), 2);

        RandomItemOutput randomComponent = new RandomItemOutput(output);

        Assertions.assertEquals(output, randomComponent.getOutputSet());
        Assertions.assertFalse(randomComponent.isDisabled());
        Assertions.assertEquals(1, randomComponent.getSlimefunItemIDs().size());
        Assertions.assertTrue(randomComponent.getSlimefunItemIDs().contains(sfItem.getId()));
    }
    
}
