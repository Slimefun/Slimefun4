package me.mrCookieSlime.CSCoreLibPlugin.general.Inventory;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockbukkit.mockbukkit.MockBukkit;

import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun5.utils.ChestMenuUtils;

/**
 * Regression guard for the double-click / drag duplication class. The naive menu click dispatch only
 * evaluated the clicked slot, so a {@code COLLECT_TO_CURSOR} could vacuum protected display slots (which
 * regenerate on reopen) into the cursor, and an ungoverned drag could place into protected slots. These
 * assert the pure guard predicates {@link MenuListener} now applies.
 */
class TestMenuListenerCollectGuard {

    @BeforeAll
    public static void load() {
        MockBukkit.mock();
        MockBukkit.load(Slimefun.class);
    }

    @AfterAll
    public static void unload() {
        MockBukkit.unmock();
    }

    /** A menu with a protected display pane at slot 0 (handler) and a free input item at slot 1 (none). */
    private static ChestMenu menuWith(ItemStack protectedItem, ItemStack freeItem) {
        ChestMenu menu = new ChestMenu("Test Menu");
        menu.addItem(0, protectedItem, ChestMenuUtils.getEmptyClickHandler());
        menu.addItem(1, freeItem);
        menu.getContents(); // force inventory setup
        return menu;
    }

    @Test
    @DisplayName("Collect is flagged when the cursor matches a protected (handler) display slot")
    void testCollectFromProtectedSlotFlagged() {
        ItemStack pane = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ChestMenu menu = menuWith(pane.clone(), new ItemStack(Material.IRON_INGOT));

        Assertions.assertTrue(
            MenuListener.collectWouldTouchProtectedSlot(pane.clone(), menu.toInventory(), menu),
            "gathering the protected display pane must be flagged (dupe guard)");
    }

    @Test
    @DisplayName("Collect is allowed when the cursor matches only a free input slot")
    void testCollectFromFreeSlotAllowed() {
        ItemStack pane = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ChestMenu menu = menuWith(pane.clone(), new ItemStack(Material.IRON_INGOT));

        Assertions.assertFalse(
            MenuListener.collectWouldTouchProtectedSlot(new ItemStack(Material.IRON_INGOT), menu.toInventory(), menu),
            "gathering an item that only sits in a free input slot must be allowed");
    }

    @Test
    @DisplayName("Collect is allowed when the cursor matches nothing protected")
    void testCollectNoMatchAllowed() {
        ItemStack pane = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ChestMenu menu = menuWith(pane.clone(), new ItemStack(Material.IRON_INGOT));

        Assertions.assertFalse(
            MenuListener.collectWouldTouchProtectedSlot(new ItemStack(Material.DIAMOND), menu.toInventory(), menu),
            "a gather that touches no protected slot must not be flagged");
    }

    @Test
    @DisplayName("Drag into a protected slot is flagged; drag into only free/player slots is allowed")
    void testDragGuard() {
        ChestMenu menu = menuWith(new ItemStack(Material.GRAY_STAINED_GLASS_PANE), new ItemStack(Material.IRON_INGOT));
        int topSize = menu.toInventory().getSize();

        Assertions.assertTrue(
            MenuListener.dragTouchesProtectedSlot(Arrays.asList(0, topSize + 3), topSize, menu),
            "a drag touching a protected slot must be flagged");

        Assertions.assertFalse(
            MenuListener.dragTouchesProtectedSlot(Arrays.asList(1, topSize + 3), topSize, menu),
            "a drag over only free/player slots must be allowed");
    }
}
