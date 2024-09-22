package io.github.thebusybiscuit.slimefun4.api.recipes;

import java.util.Collection;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import be.seeseemelk.mockbukkit.MockBukkit;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;

class TestRecipeCrafter {

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
    void testRecipeCrafter() {
        RecipeCategory testCategory1 = new RecipeCategory(new NamespacedKey(plugin, "crafter1"), new ItemStack(Material.CRAFTING_TABLE));
        RecipeCategory testCategory2 = new RecipeCategory(new NamespacedKey(plugin, "crafter2"), new ItemStack(Material.CRAFTING_TABLE));

        Recipe testRecipe1 = Recipe.of(RecipeStructure.IDENTICAL, new ItemStack[] {
            null, null, null,
            null, new ItemStack(Material.IRON_AXE), null,
            null, null, null,
        }, new ItemStack(Material.ACACIA_BOAT));
        Recipe testRecipe2 = Recipe.of(RecipeStructure.IDENTICAL, new ItemStack[] {
            null, null, null,
            null, new ItemStack(Material.IRON_AXE), null,
            new ItemStack(Material.IRON_PICKAXE), null, null,
        }, new ItemStack(Material.ACACIA_BOAT));
        Recipe testRecipe3 = Recipe.of(RecipeStructure.IDENTICAL, new ItemStack[] {
            null, null, null,
            null, new ItemStack(Material.GOLDEN_AXE), null,
            null, null, null,
        }, new ItemStack(Material.ACACIA_BOAT));
        Recipe testRecipe4 = Recipe.of(RecipeStructure.IDENTICAL, new ItemStack[] {
            null, null, null,
            null, new ItemStack(Material.IRON_AXE), null,
            new ItemStack(Material.GOLDEN_AXE), null, null,
        }, new ItemStack(Material.ACACIA_BOAT));

        testCategory1.registerRecipe(testRecipe1);
        testCategory1.registerRecipe(testRecipe2);
        testCategory2.registerRecipe(testRecipe3);
        testCategory2.registerRecipe(testRecipe4);

        RecipeCrafter crafter = () -> List.of(testCategory1, testCategory2);

        Collection<Recipe> craftableRecipes = crafter.getRecipes();

        Assertions.assertTrue(craftableRecipes.contains(testRecipe1));
        Assertions.assertTrue(craftableRecipes.contains(testRecipe2));
        Assertions.assertTrue(craftableRecipes.contains(testRecipe3));
        Assertions.assertTrue(craftableRecipes.contains(testRecipe4));

        Assertions.assertEquals(2, crafter.getSingleInputRecipes().size());
    }
    
}
