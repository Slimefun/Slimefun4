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

class TestMultiItemComponent {

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
        SlimefunItem sfItem = TestUtilities.mockSlimefunItem(plugin, "BIG_GOLD_INGOT", new ItemStack(Material.IRON_INGOT));

        sfItem.register(plugin);

        MultiItemComponent multiComponent = new MultiItemComponent(
            new ItemStack(Material.GOLD_INGOT),
            sfItem.getItem().clone()
        );

        Assertions.assertEquals(2, multiComponent.getChoices().size());
        Assertions.assertTrue(multiComponent.getChoices().contains(new ItemComponent(Material.GOLD_INGOT)));
        Assertions.assertTrue(multiComponent.getChoices().contains(new ItemComponent(sfItem.getItem())));
        Assertions.assertTrue(multiComponent.matches(new ItemStack(Material.GOLD_INGOT)));
        Assertions.assertTrue(multiComponent.matches(sfItem.getItem()));
        Assertions.assertFalse(multiComponent.isDisabled());
        Assertions.assertEquals(1, multiComponent.getSlimefunItemIDs().size());
        Assertions.assertTrue(multiComponent.getSlimefunItemIDs().contains(sfItem.getId()));

    }
    
}
