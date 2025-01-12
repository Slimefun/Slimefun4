package io.github.thebusybiscuit.slimefun4.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import be.seeseemelk.mockbukkit.MockBukkit;
import io.github.thebusybiscuit.slimefun4.utils.RecipeUtils.BoundingBox;

class TestRecipeUtils {

    @BeforeAll
    public static void load() {
        MockBukkit.mock();
    }

    @AfterAll
    public static void unload() {
        MockBukkit.unmock();
    }

    @Test
    @DisplayName("Test Bounding Box Calculation")
    void testCalculateBoundingBox() {
        List<Integer> a1 = List.of(
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 1, 0, 0, 0,
            0, 0, 0, 1, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 1, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 1, 0, 0, 0, 0,
            0, 0, 1, 0, 0, 0, 0, 0, 1, 0,
            0, 0, 0, 1, 0, 0, 1, 0, 0, 0
        );
        Assertions.assertEquals(
            new BoundingBox(1, 2, 6, 8),
            RecipeUtils.calculateBoundingBox(a1, 10, 7, i -> i == 0)
        );
        
        List<Integer> a2 = List.of(1);
        Assertions.assertEquals(
            new BoundingBox(0, 0, 0, 0),
            RecipeUtils.calculateBoundingBox(a2, 1, 1, i -> i == 0)
        );
        
        List<Integer> a3 = List.of(0);
        Assertions.assertEquals(
            new BoundingBox(0, 0, 0, 0),
            RecipeUtils.calculateBoundingBox(a3, 1, 1, i -> i == 0)
        );
        
        List<Integer> a4 = List.of(1, 1, 1, 1);
        Assertions.assertEquals(
            new BoundingBox(0, 0, 1, 1),
            RecipeUtils.calculateBoundingBox(a4, 2, 2, i -> i == 0)
        );
        
        List<Integer> a5 = List.of(0, 0, 0, 0);
        Assertions.assertEquals(
            new BoundingBox(0, 0, 0, 0),
            RecipeUtils.calculateBoundingBox(a5, 2, 2, i -> i == 0)
        );
        
        List<Integer> a6 = List.of(
            0, 0, 0, 0,
            0, 0, 0, 0,
            0, 1, 1, 0,
            0, 0, 0, 0
        );
        Assertions.assertEquals(
            new BoundingBox(2, 1, 2, 2),
            RecipeUtils.calculateBoundingBox(a6, 4, 4, i -> i == 0)
        );
    }
    
    @Test
    @DisplayName("Test Bounding Box Calculation")
    void testFlipY() {
        List<Integer> a1 = List.of(1);
        Assertions.assertEquals(
            List.of(1),
            RecipeUtils.flipY(a1, 1, 1)
        );

        List<Integer> a2 = List.of(0, 1, 1, 0);
        Assertions.assertEquals(
            List.of(1, 0, 0, 1),
            RecipeUtils.flipY(a2, 2, 2)
        );

        List<Integer> a3 = List.of();
        Assertions.assertEquals(
            List.of(),
            RecipeUtils.flipY(a3, 0, 0)
        );
    }
    
    @Test
    @DisplayName("Test Bounding Box Calculation")
    void testRotate3x3() {
        List<Integer> a1 = List.of(7, 8, 9, 4, 5, 6, 1, 2, 3);
        Assertions.assertEquals(
            List.of(4, 7, 8, 1, 5, 9, 2, 3, 6),
            RecipeUtils.rotate45deg3x3(a1)
        );
    }
    
    @Test
    @DisplayName("Test Amount-agnostic item hashing")
    void testHashItemStacks() {
        final ItemStack helmetWithMeta1 = new ItemStack(Material.LEATHER_HELMET);
        final LeatherArmorMeta m1 = (LeatherArmorMeta) helmetWithMeta1.getItemMeta();
        m1.setColor(Color.AQUA);
        helmetWithMeta1.setItemMeta(m1);

        final ItemStack helmetWithMeta2 = new ItemStack(Material.LEATHER_HELMET);
        final LeatherArmorMeta m2 = (LeatherArmorMeta) helmetWithMeta2.getItemMeta();
        m2.setUnbreakable(true);
        helmetWithMeta2.setItemMeta(m1);

        List<ItemStack> a1 = List.of(
            new ItemStack(Material.OAK_LOG, 55),
            new ItemStack(Material.LEATHER_HELMET)
        );
        List<ItemStack> a2 = List.of(
            new ItemStack(Material.OAK_LOG, 20),
            new ItemStack(Material.LEATHER_HELMET)
        );
        List<ItemStack> a3 = List.of(
            new ItemStack(Material.OAK_LOG, 55),
            helmetWithMeta1
        );
        List<ItemStack> a4 = List.of(
            new ItemStack(Material.OAK_LOG, 55),
            helmetWithMeta2
        );
        List<ItemStack> a5 = List.of(
            new ItemStack(Material.BIRCH_LOG, 55),
            new ItemStack(Material.LEATHER_HELMET)
        );
        Assertions.assertEquals(RecipeUtils.hashItemsIgnoreAmount(a1), RecipeUtils.hashItemsIgnoreAmount(a1));
        Assertions.assertEquals(RecipeUtils.hashItemsIgnoreAmount(a1), RecipeUtils.hashItemsIgnoreAmount(a2));
        Assertions.assertNotEquals(RecipeUtils.hashItemsIgnoreAmount(a1), RecipeUtils.hashItemsIgnoreAmount(a3));
        Assertions.assertNotEquals(RecipeUtils.hashItemsIgnoreAmount(a1), RecipeUtils.hashItemsIgnoreAmount(a4));
        Assertions.assertNotEquals(RecipeUtils.hashItemsIgnoreAmount(a1), RecipeUtils.hashItemsIgnoreAmount(a5));
        Assertions.assertEquals(RecipeUtils.hashItemsIgnoreAmount(a3), RecipeUtils.hashItemsIgnoreAmount(a3));
        Assertions.assertEquals(RecipeUtils.hashItemsIgnoreAmount(a4), RecipeUtils.hashItemsIgnoreAmount(a4));
        Assertions.assertEquals(
            RecipeUtils.hashItemsIgnoreAmount(new ArrayList<>(Collections.nCopies(3, null))),
            RecipeUtils.hashItemsIgnoreAmount(List.of(new ItemStack(Material.VOID_AIR), new ItemStack(Material.CAVE_AIR), new ItemStack(Material.AIR)))
        );
    }
    
    @Test
    @DisplayName("Test key char conversion")
    void testKeyCharConversion() {
        char[] chars = new char[] {
            '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
            'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
            'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            '!', '"', '#', '$', '%', '&', '\'', '(', ')', '*', '+', ',', '-', '.', '/', '0',
            ':', ';', '<', '=', '>', '?', '@',
            '[', '\\', ']', '^', '_', '`',
            '{', '|', '}', '~'
        };
        for (int i = 0; i < chars.length; ++i) {
            Assertions.assertEquals(chars[i], RecipeUtils.getKeyCharByNumber(i));
        }
    }
}
