package io.github.thebusybiscuit.slimefun4.testing.tests.utils;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import be.seeseemelk.mockbukkit.MockBukkit;
import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import io.github.thebusybiscuit.slimefun4.utils.itemstack.ItemStackWrapper;

class TestItemStackWrapper {

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
    @DisplayName("Test if an ItemStackWrappers can be compared properly (With ItemMeta)")
    void testEqualityWithItemMeta() {
        ItemStack item = new CustomItem(Material.LAVA_BUCKET, "&4SuperHot.exe", "", "&6Hello");
        ItemStackWrapper wrapper = new ItemStackWrapper(item);

        Assertions.assertEquals(item.getType(), wrapper.getType());
        Assertions.assertEquals(item.hasItemMeta(), wrapper.hasItemMeta());
        Assertions.assertEquals(item.getItemMeta(), wrapper.getItemMeta());
        Assertions.assertTrue(SlimefunUtils.isItemSimilar(wrapper, item, true));
    }

    @Test
    @DisplayName("Test if an ItemStackWrappers can be compared properly (No ItemMeta)")
    void testEqualityWithoutItemMeta() {
        ItemStack item = new ItemStack(Material.DIAMOND_AXE);
        ItemStackWrapper wrapper = new ItemStackWrapper(item);

        Assertions.assertEquals(item.getType(), wrapper.getType());
        Assertions.assertEquals(item.hasItemMeta(), wrapper.hasItemMeta());
        Assertions.assertEquals(item.getItemMeta(), wrapper.getItemMeta());
        Assertions.assertTrue(SlimefunUtils.isItemSimilar(wrapper, item, true));
    }

    @Test
    @DisplayName("Test if an ItemStackWrapper is immutable")
    void testImmutability() {
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

    @Test
    @DisplayName("Test wrapping an ItemStack Array")
    void testWrapArray() {
        ItemStack[] items = { new ItemStack(Material.DIAMOND), null, new ItemStack(Material.EMERALD), new CustomItem(Material.REDSTONE, "&4Firey thing", "with lore :o") };
        ItemStackWrapper[] wrappers = ItemStackWrapper.wrapArray(items);

        Assertions.assertEquals(items.length, wrappers.length);

        for (int i = 0; i < items.length; i++) {
            assertWrapped(items[i], wrappers[i]);
        }
    }

    @Test
    @DisplayName("Test wrapping an ItemStack List")
    void testWrapList() {
        List<ItemStack> items = Arrays.asList(new ItemStack(Material.DIAMOND), null, new ItemStack(Material.EMERALD), new CustomItem(Material.REDSTONE, "&4Firey thing", "with lore :o"));
        List<ItemStackWrapper> wrappers = ItemStackWrapper.wrapList(items);

        Assertions.assertEquals(items.size(), wrappers.size());

        for (int i = 0; i < items.size(); i++) {
            assertWrapped(items.get(i), wrappers.get(i));
        }
    }

    private void assertWrapped(ItemStack expected, ItemStack actual) {
        if (expected == null) {
            Assertions.assertNull(actual);
        } else {
            Assertions.assertTrue(actual instanceof ItemStackWrapper);
            Assertions.assertTrue(SlimefunUtils.isItemSimilar(actual, expected, true));
        }
    }
}
