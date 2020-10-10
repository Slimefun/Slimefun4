package io.github.thebusybiscuit.slimefun4.testing.tests.registration;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import be.seeseemelk.mockbukkit.MockBukkit;
import io.github.thebusybiscuit.cscorelib2.chat.ChatColors;
import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import io.github.thebusybiscuit.slimefun4.core.attributes.Rechargeable;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.testing.TestUtilities;
import io.github.thebusybiscuit.slimefun4.utils.LoreBuilder;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

public class TestRechargeableItems {

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
    public void testInvalidItems() {
        Rechargeable rechargeable = mock("INVALID_CHARGING_TEST", 1);

        Assertions.assertThrows(IllegalArgumentException.class, () -> rechargeable.getItemCharge(null));
        Assertions.assertThrows(IllegalArgumentException.class, () -> rechargeable.getItemCharge(new ItemStack(Material.AIR)));

        Assertions.assertThrows(IllegalArgumentException.class, () -> rechargeable.setItemCharge(null, 1));
        Assertions.assertThrows(IllegalArgumentException.class, () -> rechargeable.setItemCharge(new ItemStack(Material.AIR), 1));

        Assertions.assertThrows(IllegalArgumentException.class, () -> rechargeable.addItemCharge(null, 1));
        Assertions.assertThrows(IllegalArgumentException.class, () -> rechargeable.addItemCharge(new ItemStack(Material.AIR), 1));

        Assertions.assertThrows(IllegalArgumentException.class, () -> rechargeable.removeItemCharge(null, 1));
        Assertions.assertThrows(IllegalArgumentException.class, () -> rechargeable.removeItemCharge(new ItemStack(Material.AIR), 1));
    }

    @Test
    public void testSetItemCharge() {
        Rechargeable rechargeable = mock("CHARGING_TEST", 10);
        ItemStack item = new CustomItem(Material.REDSTONE_ORE, "&4Chargeable Item", "", LoreBuilder.powerCharged(0, 10));

        Assertions.assertEquals(0, rechargeable.getItemCharge(item));

        rechargeable.setItemCharge(item, 10);
        Assertions.assertEquals(10, rechargeable.getItemCharge(item));

        String lore = ChatColors.color("&8\u21E8 &e\u26A1 &7") + "10.0 / 10.0 J";
        Assertions.assertEquals(lore, item.getItemMeta().getLore().get(1));
    }

    @Test
    public void testItemChargeBounds() {
        Rechargeable rechargeable = mock("CHARGING_BOUNDS_TEST", 10);
        ItemStack item = new CustomItem(Material.REDSTONE_BLOCK, "&4Chargeable Item with bounds", "", LoreBuilder.powerCharged(0, 10));

        Assertions.assertThrows(IllegalArgumentException.class, () -> rechargeable.setItemCharge(item, -1));
        Assertions.assertThrows(IllegalArgumentException.class, () -> rechargeable.setItemCharge(item, -0.01F));
        Assertions.assertThrows(IllegalArgumentException.class, () -> rechargeable.setItemCharge(item, 10.01F));
        Assertions.assertThrows(IllegalArgumentException.class, () -> rechargeable.setItemCharge(item, 11));

        Assertions.assertThrows(IllegalArgumentException.class, () -> rechargeable.addItemCharge(item, -0.1F));
        Assertions.assertThrows(IllegalArgumentException.class, () -> rechargeable.removeItemCharge(item, -0.1F));
    }

    @Test
    public void testAddItemCharge() {
        Rechargeable rechargeable = mock("CHARGING_BOUNDS_TEST", 10);
        ItemStack item = new CustomItem(Material.REDSTONE_BLOCK, "&4Chargeable Item with additions", "", LoreBuilder.powerCharged(0, 10));

        Assertions.assertTrue(rechargeable.addItemCharge(item, 10));
        Assertions.assertEquals(10, rechargeable.getItemCharge(item));

        Assertions.assertFalse(rechargeable.addItemCharge(item, 1));
    }

    @Test
    public void testAddItemChargeWithoutLore() {
        Rechargeable rechargeable = mock("CHARGING_NO_LORE_TEST", 10);
        ItemStack item = new CustomItem(Material.REDSTONE_BLOCK, "&4Chargeable Item with no lore");

        Assertions.assertEquals(0, rechargeable.getItemCharge(item));

        Assertions.assertTrue(rechargeable.addItemCharge(item, 10));
        Assertions.assertEquals(10, rechargeable.getItemCharge(item));

        String lore = ChatColors.color("&8\u21E8 &e\u26A1 &7") + "10.0 / 10.0 J";
        Assertions.assertEquals(lore, item.getItemMeta().getLore().get(0));
    }

    @Test
    public void testRemoveItemCharge() {
        Rechargeable rechargeable = mock("CHARGING_BOUNDS_TEST", 10);
        ItemStack item = new CustomItem(Material.REDSTONE_BLOCK, "&4Chargeable Item with removal", "", LoreBuilder.powerCharged(0, 10));

        Assertions.assertFalse(rechargeable.removeItemCharge(item, 1));

        rechargeable.setItemCharge(item, 10);

        Assertions.assertTrue(rechargeable.removeItemCharge(item, 10));
        Assertions.assertEquals(0, rechargeable.getItemCharge(item));

        Assertions.assertFalse(rechargeable.removeItemCharge(item, 1));
    }

    private RechargeableMock mock(String id, float capacity) {
        Category category = TestUtilities.getCategory(plugin, "rechargeable");
        return new RechargeableMock(category, new SlimefunItemStack(id, new CustomItem(Material.REDSTONE_LAMP, "&3" + id)), capacity);
    }

    private class RechargeableMock extends SlimefunItem implements Rechargeable {

        private final float capacity;

        protected RechargeableMock(Category category, SlimefunItemStack item, float capacity) {
            super(category, item, RecipeType.NULL, new ItemStack[9]);
            this.capacity = capacity;
        }

        @Override
        public float getMaxItemCharge(ItemStack item) {
            return capacity;
        }

    }

}
