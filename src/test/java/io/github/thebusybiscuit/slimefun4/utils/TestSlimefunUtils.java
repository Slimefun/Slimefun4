package io.github.thebusybiscuit.slimefun4.utils;

import java.util.Arrays;
import java.util.OptionalInt;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import be.seeseemelk.mockbukkit.MockBukkit;
import io.github.bakedlibs.dough.data.persistent.PersistentDataAPI;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.attributes.DistinctiveItem;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.test.TestUtilities;

class TestSlimefunUtils {

    private static SlimefunItem distinctiveDiamond;
    
    @BeforeAll
    public static void load() {
        MockBukkit.mock();
        MockBukkit.load(Slimefun.class);

        distinctiveDiamond = new DistinctiveDiamond();
        distinctiveDiamond.register(Slimefun.instance());
    }

    @AfterAll
    public static void unload() {
        MockBukkit.unmock();
    }

    @Test
    @DisplayName("compareItem() with both ItemStack's being null")
    public void testCompareItem_bothNull() {
        // Both null should return true
        Assertions.assertTrue(SlimefunUtils.compareItem(null, null));
    }

    @Test
    @DisplayName("compareItem() with one ItemStack being null")
    public void testCompareItem_oneIsNull() {
        // One null should return false
        Assertions.assertFalse(SlimefunUtils.compareItem(new ItemStack(Material.DIAMOND), null));
        Assertions.assertFalse(SlimefunUtils.compareItem(null, new ItemStack(Material.DIAMOND)));
    }

    @Test
    @DisplayName("compareItem() with both ItemStack's not having ItemMeta")
    public void testCompareItem_noItemMeta() {
        // Vanilla item with no rename/lore should return true if the type is the same
        Assertions.assertTrue(SlimefunUtils.compareItem(new ItemStack(Material.DIAMOND), new ItemStack(Material.DIAMOND)));

        // Different types should return false
        Assertions.assertFalse(SlimefunUtils.compareItem(new ItemStack(Material.DIAMOND), new ItemStack(Material.IRON_INGOT)));
        Assertions.assertFalse(SlimefunUtils.compareItem(new ItemStack(Material.IRON_INGOT), new ItemStack(Material.DIAMOND)));
    }

    @Test
    @DisplayName("compareItem() with one ItemStack having a ItemMeta")
    public void testCompareItem_oneHasItemMeta() {
        ItemStack plainDiamond = new ItemStack(Material.DIAMOND);
        
        ItemStack renamedDiamond = new ItemStack(Material.DIAMOND);
        ItemMeta meta = renamedDiamond.getItemMeta();
        meta.setDisplayName("Renamed diamond");
        renamedDiamond.setItemMeta(meta);

        Assertions.assertFalse(SlimefunUtils.compareItem(plainDiamond, renamedDiamond));
        Assertions.assertFalse(SlimefunUtils.compareItem(renamedDiamond, plainDiamond));
    }

    @Test
    @DisplayName("compareItem() with both ItemStack's having a ItemMeta")
    public void testCompareItem_bothHaveItemMeta() {
        // Basic display name comparison
        ItemStack renamedDiamond = new ItemStack(Material.DIAMOND);
        ItemMeta meta = renamedDiamond.getItemMeta();
        meta.setDisplayName("Renamed diamond");
        renamedDiamond.setItemMeta(meta);

        Assertions.assertTrue(SlimefunUtils.compareItem(renamedDiamond, renamedDiamond));
        Assertions.assertFalse(SlimefunUtils.compareItem(new ItemStack(Material.DIAMOND), renamedDiamond));

        // Different lores
        ItemStack loreDiamond = new ItemStack(Material.DIAMOND);
        ItemMeta loreMeta = loreDiamond.getItemMeta();
        loreMeta.setLore(Arrays.asList("Lore 1", "Lore 2"));
        loreDiamond.setItemMeta(loreMeta);

        Assertions.assertTrue(SlimefunUtils.compareItem(loreDiamond, loreDiamond));
        Assertions.assertFalse(SlimefunUtils.compareItem(new ItemStack(Material.DIAMOND), loreDiamond));

        // Different enchants
        ItemStack enchantedDiamond = new ItemStack(Material.DIAMOND_SWORD);
        enchantedDiamond.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 1);

        Assertions.assertTrue(SlimefunUtils.compareItem(enchantedDiamond, enchantedDiamond));
        Assertions.assertFalse(SlimefunUtils.compareItem(new ItemStack(Material.DIAMOND_SWORD), enchantedDiamond));

        // Different damage
        ItemStack damagedDiamond = new ItemStack(Material.DIAMOND);
        damagedDiamond.setDurability((short) 1);

        Assertions.assertTrue(SlimefunUtils.compareItem(damagedDiamond, damagedDiamond));
        Assertions.assertFalse(SlimefunUtils.compareItem(new ItemStack(Material.DIAMOND), damagedDiamond));
    }

    @Test
    @DisplayName("compareItem() with both ItemStack's having a ItemMeta and a Slimefun ID")
    public void testCompareItem_slimefunIds() {
        // Same IDs
        ItemStack diamond = new ItemStack(Material.DIAMOND);
        ItemMeta meta = diamond.getItemMeta();
        Slimefun.getItemDataService().setItemData(meta, "test_item");
        diamond.setItemMeta(meta);

        Assertions.assertTrue(SlimefunUtils.compareItem(diamond, diamond));

        // Different IDs
        ItemStack emerald = new ItemStack(Material.EMERALD);
        ItemMeta emeraldMeta = emerald.getItemMeta();
        Slimefun.getItemDataService().setItemData(emeraldMeta, "test_item_two");
        emerald.setItemMeta(emeraldMeta);

        Assertions.assertFalse(SlimefunUtils.compareItem(diamond, emerald));
        Assertions.assertFalse(SlimefunUtils.compareItem(emerald, diamond));
    }

    @Test
    @DisplayName("compareItem() with DistinctiveItem")
    public void testCompareItem_oneDistinctiveItem() {
        // Compare vanilla item and distinctive item
        SlimefunItemStack distinctiveDiamond = createDistinctiveItem(1);
        ItemStack diamond = new ItemStack(Material.DIAMOND);

        Assertions.assertFalse(SlimefunUtils.compareItem(diamond, distinctiveDiamond));
        Assertions.assertFalse(SlimefunUtils.compareItem(distinctiveDiamond, diamond));

        // Compare Slimefun item not distinctive with distinctive item
        Slimefun.getItemDataService().setItemData(diamond, "NOT_DISTINCTIVE_DIAMOND");

        Assertions.assertFalse(SlimefunUtils.compareItem(diamond, distinctiveDiamond));
        Assertions.assertFalse(SlimefunUtils.compareItem(distinctiveDiamond, diamond));

        // Compare two distinctive items which are not the same
        SlimefunItemStack differentDistinctiveItem = createDistinctiveItem(2);

        Assertions.assertFalse(SlimefunUtils.compareItem(distinctiveDiamond, differentDistinctiveItem));
        Assertions.assertFalse(SlimefunUtils.compareItem(differentDistinctiveItem, distinctiveDiamond));

        // Compare two distinctive items which are the same
        SlimefunItemStack anotherDistinctiveDiamond = createDistinctiveItem(1);

        Assertions.assertTrue(SlimefunUtils.compareItem(distinctiveDiamond, anotherDistinctiveDiamond));
        Assertions.assertTrue(SlimefunUtils.compareItem(anotherDistinctiveDiamond, distinctiveDiamond));
    }

    private static SlimefunItemStack createDistinctiveItem(int mode) {
        ItemStack diamond = new ItemStack(Material.DIAMOND);
        ItemMeta meta = diamond.getItemMeta();
        PersistentDataAPI.setInt(meta, new NamespacedKey(Slimefun.instance(), "test_mode"), mode);
        diamond.setItemMeta(meta);

        return new SlimefunItemStack("DISTINCTIVE_DIAMOND", diamond);
    }

    protected static class DistinctiveDiamond extends SlimefunItem implements DistinctiveItem {

        protected DistinctiveDiamond() {
            super(
                TestUtilities.getItemGroup(Slimefun.instance(), "test"),
                new SlimefunItemStack("DISTINCTIVE_DIAMOND", new ItemStack(Material.DIAMOND)),
                "DISTINCTIVE_DIAMOND",
                RecipeType.NULL,
                null
            );
        }

        @Override
        public boolean canStack(ItemMeta itemMetaOne, ItemMeta itemMetaTwo) {
            OptionalInt modeOne = PersistentDataAPI.getOptionalInt(itemMetaOne, new NamespacedKey(Slimefun.instance(), "test_mode"));
            OptionalInt modeTwo = PersistentDataAPI.getOptionalInt(itemMetaTwo, new NamespacedKey(Slimefun.instance(), "test_mode"));

            return modeOne.isPresent() && modeTwo.isPresent() && modeOne.getAsInt() == modeTwo.getAsInt();
        }
    }
}
