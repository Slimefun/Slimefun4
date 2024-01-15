package io.github.thebusybiscuit.slimefun4.api.recipes.components;

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

class TestItemComponent {

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
    void testItemComponent() {
        SlimefunItem sfItem = TestUtilities.mockSlimefunItem(plugin, "BIG_IRON_INGOT", new ItemStack(Material.IRON_INGOT));

        sfItem.register(plugin);

        ItemComponent vanillaComponent = new ItemComponent(new ItemStack(Material.IRON_INGOT, 5));
        ItemComponent slimefunComponent = new ItemComponent(sfItem.getItem());

        Assertions.assertEquals(new ItemStack(Material.IRON_INGOT, 5), vanillaComponent.getComponent());
        Assertions.assertFalse(vanillaComponent.isDisabled());
        Assertions.assertTrue(vanillaComponent.getSlimefunItemIDs().isEmpty());
        Assertions.assertTrue(vanillaComponent.matches(new ItemStack(Material.IRON_INGOT, 5)));
        Assertions.assertTrue(vanillaComponent.matches(new ItemStack(Material.IRON_INGOT, 64)));

        Assertions.assertEquals(sfItem.getItem(), slimefunComponent.getComponent());
        Assertions.assertFalse(slimefunComponent.isDisabled());
        Assertions.assertEquals(1, slimefunComponent.getSlimefunItemIDs().size());
        Assertions.assertTrue(slimefunComponent.getSlimefunItemIDs().contains(sfItem.getId()));
        Assertions.assertFalse(slimefunComponent.matches(new ItemStack(Material.IRON_INGOT)));

    }
    
}
