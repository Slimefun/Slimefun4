package io.github.thebusybiscuit.slimefun4.testing.tests.utils;

import java.util.Collections;

import be.seeseemelk.mockbukkit.MockBukkit;
import io.github.thebusybiscuit.cscorelib2.data.PersistentDataAPI;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.utils.RechargeableHelper;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TestRechargeableHelper {

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
    @DisplayName("Test setting charge")
    void testSetCharge() {
        ItemStack item = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta meta = item.getItemMeta();

        // Make sure the lore is set
        RechargeableHelper.setCharge(meta, 1, 10);
        Assertions.assertTrue(meta.hasLore());
        Assertions.assertEquals(1, meta.getLore().size());

        // Make sure the lore is correct
        RechargeableHelper.setCharge(meta, 10.1f, 100.5f);
        Assertions.assertEquals("&8\u21E8 &e\u26A1 &710.1 / 100.5 J".replace('&', ChatColor.COLOR_CHAR), meta.getLore().get(0));

        // Make sure the persistent data was set
        Assertions.assertEquals(10.1, PersistentDataAPI.getFloat(meta, SlimefunPlugin.getRegistry().getItemChargeDataKey()), 0.001);

        // Test exceptions
        Assertions.assertThrows(IllegalArgumentException.class, () -> RechargeableHelper.setCharge(null, 1, 1));
        Assertions.assertThrows(IllegalArgumentException.class, () -> RechargeableHelper.setCharge(meta, -1, 10));
        Assertions.assertThrows(IllegalArgumentException.class, () -> RechargeableHelper.setCharge(meta, 100, 10));
    }

    @Test
    @DisplayName("Test getting charge")
    void testGetCharge() {
        // Test with persistent data
        ItemStack itemWithData = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta metaWithData = itemWithData.getItemMeta();
        PersistentDataAPI.setFloat(metaWithData, SlimefunPlugin.getRegistry().getItemChargeDataKey(), 10.5f);

        Assertions.assertEquals(10.5f, RechargeableHelper.getCharge(metaWithData), 0.001);

        // Test with lore
        ItemStack itemWithLore = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta metaWithLore = itemWithLore.getItemMeta();
        metaWithLore.setLore(Collections.singletonList("&8\u21E8 &e\u26A1 &710.5 / 100.5 J".replace('&', ChatColor.COLOR_CHAR)));

        Assertions.assertEquals(10.5, RechargeableHelper.getCharge(metaWithLore), 0.001);
        Assertions.assertTrue(PersistentDataAPI.hasFloat(metaWithLore, SlimefunPlugin.getRegistry().getItemChargeDataKey()));

        // Test no data and no lore
        ItemStack itemWithNoDataOrLore = new ItemStack(Material.DIAMOND_SWORD);

        Assertions.assertEquals(0, RechargeableHelper.getCharge(itemWithNoDataOrLore.getItemMeta()));

        // Test exceptions
        Assertions.assertThrows(IllegalArgumentException.class, () -> RechargeableHelper.getCharge(null));
    }
}
