package io.github.thebusybiscuit.slimefun4.implementation.registration;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.github.bakedlibs.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.api.exceptions.IdConflictException;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.ItemState;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.implementation.items.VanillaItem;
import io.github.thebusybiscuit.slimefun4.test.TestUtilities;

import be.seeseemelk.mockbukkit.MockBukkit;

class TestSlimefunItemRegistration {

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
    @DisplayName("Test SlimefunItem registering properly")
    void testSuccessfulRegistration() {
        String id = "TEST_ITEM";
        SlimefunItem item = TestUtilities.mockSlimefunItem(plugin, id, CustomItemStack.create(Material.DIAMOND, "&cTest"));

        Assertions.assertEquals(ItemState.UNREGISTERED, item.getState());

        item.register(plugin);

        Assertions.assertEquals(ItemState.ENABLED, item.getState());
        Assertions.assertFalse(item.isDisabled());
        Assertions.assertEquals(id, item.getId());
        Assertions.assertEquals(item, SlimefunItem.getById(id));
    }

    @Test
    @DisplayName("Test disabled SlimefunItem being disabled")
    void testDisabledItem() {
        SlimefunItem item = TestUtilities.mockSlimefunItem(plugin, "DISABLED_ITEM", CustomItemStack.create(Material.DIAMOND, "&cTest"));
        Slimefun.getItemCfg().setValue("DISABLED_ITEM.enabled", false);
        item.register(plugin);

        Assertions.assertEquals(ItemState.DISABLED, item.getState());
        Assertions.assertTrue(item.isDisabled());
    }

    @Test
    @DisplayName("Test VanillaItem falling back to vanilla.")
    void testVanillaItemFallback() {
        VanillaItem item = TestUtilities.mockVanillaItem(plugin, Material.ACACIA_SIGN, false);
        item.register(plugin);

        Assertions.assertTrue(item.isUseableInWorkbench());
        Assertions.assertEquals(ItemState.VANILLA_FALLBACK, item.getState());
        Assertions.assertTrue(item.isDisabled());
    }

    @Test
    @DisplayName("Test id conflicts being handled with an exception")
    void testIdConflict() {
        SlimefunItem item = TestUtilities.mockSlimefunItem(plugin, "DUPLICATE_ID", CustomItemStack.create(Material.DIAMOND, "&cTest"));
        item.register(plugin);

        SlimefunItem item2 = TestUtilities.mockSlimefunItem(plugin, "DUPLICATE_ID", CustomItemStack.create(Material.DIAMOND, "&cTest"));
        Assertions.assertThrows(IdConflictException.class, () -> item2.register(plugin));

        Assertions.assertEquals(ItemState.ENABLED, item.getState());
        Assertions.assertEquals(ItemState.UNREGISTERED, item2.getState());
    }

    @Test
    @DisplayName("Test ItemGroup registration when registering an item")
    void testItemGroupRegistration() {
        SlimefunItem item = TestUtilities.mockSlimefunItem(plugin, "ITEMGROUP_TEST", CustomItemStack.create(Material.DIAMOND, "&cTest"));
        item.register(plugin);
        item.load();

        // null should not be a valid argument
        Assertions.assertThrows(IllegalArgumentException.class, () -> item.setItemGroup(null));

        ItemGroup itemGroup = item.getItemGroup();
        ItemGroup itemGroup2 = new ItemGroup(new NamespacedKey(plugin, "test2"), CustomItemStack.create(Material.OBSIDIAN, "&6Test 2"));

        Assertions.assertTrue(itemGroup.contains(item));
        Assertions.assertFalse(itemGroup2.contains(item));
        Assertions.assertEquals(itemGroup, item.getItemGroup());

        item.setItemGroup(itemGroup2);
        Assertions.assertFalse(itemGroup.contains(item));
        Assertions.assertTrue(itemGroup2.contains(item));
        Assertions.assertEquals(itemGroup2, item.getItemGroup());
    }

    @Test
    @DisplayName("Test hidden items being hidden")
    void testHiddenItem() {
        SlimefunItem item = TestUtilities.mockSlimefunItem(plugin, "HIDDEN_TEST", CustomItemStack.create(Material.DIAMOND, "&cTest"));
        item.setHidden(true);
        item.register(plugin);
        item.load();

        ItemGroup itemGroup = item.getItemGroup();

        Assertions.assertTrue(item.isHidden());
        Assertions.assertFalse(itemGroup.contains(item));
        Assertions.assertEquals(itemGroup, item.getItemGroup());

        item.setHidden(false);
        Assertions.assertFalse(item.isHidden());
        Assertions.assertTrue(itemGroup.contains(item));
        Assertions.assertEquals(itemGroup, item.getItemGroup());

        item.setHidden(true);
        Assertions.assertTrue(item.isHidden());
        Assertions.assertFalse(itemGroup.contains(item));
        Assertions.assertEquals(itemGroup, item.getItemGroup());

        // Do nothing if the value hasn't changed
        item.setHidden(true);
        Assertions.assertTrue(item.isHidden());
        Assertions.assertFalse(itemGroup.contains(item));
    }
}
