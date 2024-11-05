package io.github.thebusybiscuit.slimefun4.core.services;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.github.bakedlibs.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.recipes.Recipe;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.api.recipes.matching.MatchProcedure;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.test.mocks.MockSlimefunItem;
import be.seeseemelk.mockbukkit.MockBukkit;

class TestSFRecipeService {

    private static Slimefun sf;
    private static ItemGroup itemGroup;
    private static MockSlimefunItem testItem1;
    private static MockSlimefunItem testItem2;
    private static MockSlimefunItem testItem3;
    private static MockSlimefunItem testItem4;
    private static MockSlimefunItem testItem5;

    @BeforeAll
    public static void load() {
        MockBukkit.mock();
        sf = MockBukkit.load(Slimefun.class);
        itemGroup = new ItemGroup(new NamespacedKey(sf, "test_group"), new CustomItemStack(Material.DIAMOND_AXE, "Test Group"));
        testItem1 = new MockSlimefunItem(itemGroup, new ItemStack(Material.IRON_INGOT), "TEST_ITEM_1");
        testItem2 = new MockSlimefunItem(itemGroup, new ItemStack(Material.IRON_INGOT), "TEST_ITEM_2");
        testItem3 = new MockSlimefunItem(itemGroup, new ItemStack(Material.IRON_INGOT), "TEST_ITEM_3");
        testItem4 = new MockSlimefunItem(itemGroup, new ItemStack(Material.IRON_INGOT), "TEST_ITEM_4");
        testItem5 = new MockSlimefunItem(itemGroup, new ItemStack(Material.IRON_INGOT), "TEST_ITEM_5");
        testItem1.register(sf);
        testItem2.register(sf);
        testItem3.register(sf);
        testItem4.register(sf);
        testItem5.register(sf);
    }

    @AfterAll
    public static void unload() {
        MockBukkit.unmock();
    }

    @Test
    @DisplayName("Test adding recipes")
    void testRecipe() {
        RecipeService service = new RecipeService(sf);

        Recipe recipe1 = Recipe.fromItemStacks("TEST_ITEM_1", new ItemStack[] {
            null, null, null,
            null, null, testItem1.getItem(),
            null, null, null,
        }, new ItemStack[] { testItem1.getItem() }, RecipeType.NULL);
        Recipe recipe2 = Recipe.fromItemStacks("TEST_ITEM_2", new ItemStack[] {
            null, null, null,
            null, null, testItem1.getItem(),
            testItem2.getItem(), null, null,
        }, new ItemStack[] { testItem2.getItem() }, RecipeType.NULL);
        Recipe recipe3 = Recipe.fromItemStacks("TEST_ITEM_3", new ItemStack[] {
            null, testItem3.getItem(), null,
            null, null, testItem1.getItem(),
            testItem2.getItem(), null, null,
        }, new ItemStack[] { testItem3.getItem() }, RecipeType.NULL);
        Recipe recipe4 = Recipe.fromItemStacks("TEST_ITEM_4", new ItemStack[] {
            null, testItem3.getItem(), null,
            null, null, testItem1.getItem(),
            testItem2.getItem(), null, null,
        }, new ItemStack[] { testItem4.getItem() }, RecipeType.NULL);

        service.addRecipe(recipe1);
        service.addRecipe(recipe2);
        service.addRecipe(recipe3);
        service.addRecipe(recipe4);

        Assertions.assertEquals(recipe1, service.getRecipe("TEST_ITEM_1"));
        Assertions.assertEquals(recipe2, service.getRecipe("TEST_ITEM_2"));
        Assertions.assertEquals(recipe3, service.getRecipe("TEST_ITEM_3"));
        Assertions.assertEquals(recipe4, service.getRecipe("TEST_ITEM_4"));

        ItemStack sfItem = testItem1.getItem().clone();
        var search = service.searchRecipes(RecipeType.NULL, Arrays.asList(
            null, null, null,
            null, sfItem, null,
            null, null, null
        ), MatchProcedure.SHAPED);
        Assertions.assertTrue(search.matchFound());
        var result = search.getResult().get();
        Assertions.assertTrue(result.itemsMatch());
        var recipe = search.getRecipe().get();
        Assertions.assertEquals(recipe1, recipe);

    }

}
