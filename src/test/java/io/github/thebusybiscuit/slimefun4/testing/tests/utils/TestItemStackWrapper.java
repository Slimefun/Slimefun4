package io.github.thebusybiscuit.slimefun4.testing.tests.utils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import be.seeseemelk.mockbukkit.MockBukkit;
import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import io.github.thebusybiscuit.slimefun4.utils.itemstack.ItemStackWrapper;

public class TestItemStackWrapper {

    @BeforeAll
    public static void load() {
        MockBukkit.mock();
        MockBukkit.load(SlimefunPlugin.class);
    }

    @AfterAll
    public static void unload() {
        MockBukkit.unmock();
    }

    @Test
    public void testEquality() {
        ItemStack item = new CustomItem(Material.LAVA_BUCKET, "&4SuperHot.exe", "", "&6Hello");
        ItemStackWrapper wrapper = new ItemStackWrapper(item);

        Assertions.assertEquals(item.getType(), wrapper.getType());
        Assertions.assertEquals(item.hasItemMeta(), wrapper.hasItemMeta());
        Assertions.assertEquals(item.getItemMeta(), wrapper.getItemMeta());
        Assertions.assertTrue(SlimefunUtils.isItemSimilar(wrapper, item, true));
    }

    @Test
    public void testImmutability() {
        ItemStack item = new CustomItem(Material.LAVA_BUCKET, "&4SuperHot.exe", "", "&6Hello");
        ItemStackWrapper wrapper = new ItemStackWrapper(item);

        Assertions.assertThrows(UnsupportedOperationException.class, () -> wrapper.setType(Material.BEDROCK));
        Assertions.assertThrows(UnsupportedOperationException.class, () -> wrapper.setAmount(3));
        Assertions.assertThrows(UnsupportedOperationException.class, () -> wrapper.setItemMeta(item.getItemMeta()));
        Assertions.assertThrows(UnsupportedOperationException.class, () -> wrapper.addUnsafeEnchantment(null, 1));
        Assertions.assertThrows(UnsupportedOperationException.class, () -> wrapper.hashCode());
        Assertions.assertThrows(UnsupportedOperationException.class, () -> wrapper.clone());
        Assertions.assertThrows(UnsupportedOperationException.class, () -> wrapper.equals(wrapper));
    }

}
