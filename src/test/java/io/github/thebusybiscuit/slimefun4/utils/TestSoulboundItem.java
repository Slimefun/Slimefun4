package io.github.thebusybiscuit.slimefun4.utils;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import io.github.thebusybiscuit.slimefun4.core.attributes.Soulbound;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;

import be.seeseemelk.mockbukkit.MockBukkit;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

class TestSoulboundItem {

    private static SlimefunPlugin plugin;

    @BeforeAll
    public static void load() {
        MockBukkit.mock();
        plugin = MockBukkit.load(SlimefunPlugin.class);
    }

    @AfterAll
    public static void unload() {
        MockBukkit.unmock();
    }

    @Test
    @DisplayName("Verify that null and air throw Illegal Argument Exceptions")
    void testNullAndAir() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> SlimefunUtils.setSoulbound(null, true));

        ItemStack item = new ItemStack(Material.AIR);
        Assertions.assertThrows(IllegalArgumentException.class, () -> SlimefunUtils.setSoulbound(item, true));

        Assertions.assertFalse(SlimefunUtils.isSoulbound(null));
        Assertions.assertFalse(SlimefunUtils.isSoulbound(item));
    }

    @Test
    @DisplayName("Test whether an Item can be marked as soulbound")
    void testSetSoulbound() {
        ItemStack item = new CustomItem(Material.DIAMOND, "&cI wanna be soulbound!");

        Assertions.assertFalse(SlimefunUtils.isSoulbound(item));

        SlimefunUtils.setSoulbound(item, true);
        Assertions.assertTrue(SlimefunUtils.isSoulbound(item));
        Assertions.assertEquals(1, item.getItemMeta().getLore().size());

        SlimefunUtils.setSoulbound(item, false);
        Assertions.assertFalse(SlimefunUtils.isSoulbound(item));
        Assertions.assertEquals(0, item.getItemMeta().getLore().size());
    }

    @Test
    @DisplayName("Make sure that marking an item as soulbound twice has no effect")
    void testDoubleCalls() {
        ItemStack item = new CustomItem(Material.DIAMOND, "&cI wanna be soulbound!");

        SlimefunUtils.setSoulbound(item, true);
        SlimefunUtils.setSoulbound(item, true);
        Assertions.assertTrue(SlimefunUtils.isSoulbound(item));
        Assertions.assertEquals(1, item.getItemMeta().getLore().size());

        SlimefunUtils.setSoulbound(item, false);
        SlimefunUtils.setSoulbound(item, false);
        Assertions.assertFalse(SlimefunUtils.isSoulbound(item));
        Assertions.assertEquals(0, item.getItemMeta().getLore().size());
    }

    @Test
    @DisplayName("Test that soulbound Slimefun Items are soulbound")
    void testSoulboundSlimefunItem() {
        SlimefunItem item = new SoulboundMock(new Category(new NamespacedKey(plugin, "soulbound_category"), new CustomItem(Material.REDSTONE, "&4Walshrus forever")));
        item.register(plugin);

        Assertions.assertTrue(SlimefunUtils.isSoulbound(item.getItem()));
    }

    private class SoulboundMock extends SlimefunItem implements Soulbound {

        public SoulboundMock(Category category) {
            super(category, new SlimefunItemStack("MOCK_SOULBOUND", Material.REDSTONE, "&4Almighty Redstone"), RecipeType.NULL, new ItemStack[9]);
        }

    }

}
