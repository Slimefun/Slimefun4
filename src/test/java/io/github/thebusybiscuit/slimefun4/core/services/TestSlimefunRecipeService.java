package io.github.thebusybiscuit.slimefun4.core.services;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import be.seeseemelk.mockbukkit.MockBukkit;
import io.github.bakedlibs.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.recipes.Recipe;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeCategory;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeSearchResult;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeStructure;
import io.github.thebusybiscuit.slimefun4.core.services.SlimefunRecipeService.CachingStrategy;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.test.TestUtilities;

/**
 * These tests are for the Slimefun recipe service, and they assume
 * the recipe API works. Recipe API unit tests are in api/recipes
 */
class TestSlimefunRecipeService {

    private static Slimefun plugin;

    private static RecipeCategory testCategory1;
    private static RecipeCategory testCategory2;
    
    @BeforeAll
    public static void load() {
        MockBukkit.mock();
        plugin = MockBukkit.load(Slimefun.class);

        testCategory1 = new RecipeCategory(
            new NamespacedKey(plugin, "test-category-1"),
            new ItemStack(Material.ENCHANTING_TABLE), RecipeStructure.IDENTICAL);
        testCategory2 = new RecipeCategory(
            new NamespacedKey(plugin, "test-category-2"),
            new ItemStack(Material.CRAFTING_TABLE), RecipeStructure.IDENTICAL);
    }

    @AfterAll
    public static void unload() {
        MockBukkit.unmock();
    }

    @Test
    @DisplayName("Test registering a recipe with the recipe service")
    void testRegistration() {
        SlimefunRecipeService service = new SlimefunRecipeService();

        ItemStack[] inputs1 = new ItemStack[] {
            null, null, new ItemStack(Material.STICK),
            null, new ItemStack(Material.STICK), null,
            null, null, null
        };
        ItemStack output1 = new CustomItemStack(Material.GOLD_BLOCK, "Not an Iron Block", "", "...");

        ItemStack[] inputs2 = new ItemStack[] {
            null, null, null,
            null, new ItemStack(Material.STICK), null,
            new ItemStack(Material.STICK), null, null
        };
        ItemStack output2 = new CustomItemStack(Material.IRON_BLOCK, "Not a Gold Block", "", "...");

        Recipe testRecipe1 = Recipe.of(RecipeStructure.IDENTICAL, inputs1, output1);
        Recipe testRecipe2 = Recipe.of(RecipeStructure.IDENTICAL, inputs2, output2);

        service.registerRecipe(testCategory1, testRecipe2);
        service.registerRecipe(testCategory1, testRecipe1);
        service.registerRecipe(testCategory2, testRecipe1);

        RecipeSearchResult searchResult = service.searchRecipes(testCategory1, inputs1, CachingStrategy.NEVER);
        
        Assertions.assertEquals(testCategory1, searchResult.getSearchCategory());
        Assertions.assertEquals(testRecipe1, searchResult.getRecipe());

        List<Recipe> cat1Recipes = service.getRecipes(testCategory1);
        List<Recipe> cat2Recipes = service.getRecipes(testCategory2);

        Assertions.assertTrue(cat1Recipes.contains(testRecipe2));
        Assertions.assertTrue(cat1Recipes.contains(testRecipe1));
        Assertions.assertTrue(cat2Recipes.contains(testRecipe1));

    }

    @Test
    @DisplayName("Test getting the recipes by output")
    void testGetByOutput() {
        SlimefunRecipeService service = new SlimefunRecipeService();

        ItemStack[] inputs1 = new ItemStack[] {
            null, null, new ItemStack(Material.STICK),
            null, new ItemStack(Material.STICK), null,
            null, null, null
        };
        ItemStack[] inputs2 = new ItemStack[] {
            null, null, null,
            null, new ItemStack(Material.STICK), null,
            new ItemStack(Material.STICK), null, null
        };

        SlimefunItem item1 = TestUtilities.mockSlimefunItem(plugin, "TEST_IRON", new ItemStack(Material.IRON_INGOT));
        SlimefunItem item2 = TestUtilities.mockSlimefunItem(plugin, "TEST_GOLD", new ItemStack(Material.GOLD_INGOT));

        item1.register(plugin);
        item2.register(plugin);

        Recipe testRecipe1 = Recipe.of(RecipeStructure.IDENTICAL, inputs1, item1.getItem());
        Recipe testRecipe2 = Recipe.of(RecipeStructure.IDENTICAL, inputs2, item1.getItem());
        Recipe testRecipe3 = Recipe.of(RecipeStructure.IDENTICAL, inputs2, item1.getItem());
        Recipe testRecipe4 = Recipe.of(RecipeStructure.IDENTICAL, inputs2, item2.getItem());

        service.registerRecipe(testCategory1, testRecipe1);
        service.registerRecipe(testCategory1, testRecipe2);
        service.registerRecipe(testCategory2, testRecipe3);
        service.registerRecipe(testCategory2, testRecipe4);

        Map<RecipeCategory, Set<Recipe>> recipesByOutput = service.getRecipesByOutput(item1.getId());

        Assertions.assertTrue(recipesByOutput.containsKey(testCategory1));
        Assertions.assertTrue(recipesByOutput.containsKey(testCategory2));

        Assertions.assertTrue(recipesByOutput.get(testCategory1).contains(testRecipe1));
        Assertions.assertTrue(recipesByOutput.get(testCategory1).contains(testRecipe2));
        Assertions.assertTrue(recipesByOutput.get(testCategory2).contains(testRecipe3));
        Assertions.assertFalse(recipesByOutput.get(testCategory2).contains(testRecipe4));

        Assertions.assertEquals(3, service.getNumberOfRecipes(item1.getId()));
        Assertions.assertEquals(1, service.getNumberOfRecipes(item2.getId()));

    }

    @Test
    @DisplayName("Test getting the recipes by input")
    void testGetByInput() {
        SlimefunRecipeService service = new SlimefunRecipeService();

        SlimefunItem item1 = TestUtilities.mockSlimefunItem(plugin, "TEST_SALMON", new ItemStack(Material.SALMON));
        SlimefunItem item2 = TestUtilities.mockSlimefunItem(plugin, "TEST_COD", new ItemStack(Material.COD));

        item1.register(plugin);
        item2.register(plugin);

        ItemStack itemStack1 = item1.getItem();
        ItemStack itemStack2 = item2.getItem();

        ItemStack[] inputs1 = new ItemStack[] {
            null, null, itemStack1,
            null, itemStack2, null,
            null, null, null
        };
        ItemStack[] inputs2 = new ItemStack[] {
            itemStack1, null, itemStack2,
            itemStack1, itemStack1, null,
            itemStack1, null, null
        };
        ItemStack[] inputs3 = new ItemStack[] {
            null, null, null,
            null, itemStack1, null,
            null, null, null
        };
        ItemStack[] inputs4 = new ItemStack[] {
            itemStack2, null, itemStack2,
            null, null, null,
            null, itemStack2, null
        };

        Recipe testRecipe1 = Recipe.of(RecipeStructure.IDENTICAL, inputs1, new ItemStack(Material.IRON_BLOCK));
        Recipe testRecipe2 = Recipe.of(RecipeStructure.IDENTICAL, inputs2, new ItemStack(Material.GOLD_BLOCK));
        Recipe testRecipe3 = Recipe.of(RecipeStructure.IDENTICAL, inputs3, new ItemStack(Material.COAL_BLOCK));
        Recipe testRecipe4 = Recipe.of(RecipeStructure.IDENTICAL, inputs4, new ItemStack(Material.DIAMOND_BLOCK));

        service.registerRecipe(testCategory1, testRecipe1);
        service.registerRecipe(testCategory1, testRecipe2);
        service.registerRecipe(testCategory2, testRecipe3);
        service.registerRecipe(testCategory2, testRecipe4);

        Map<RecipeCategory, Set<Recipe>> recipesByInput1 = service.getRecipesByInput(item1.getId());
        Map<RecipeCategory, Set<Recipe>> recipesByInput2 = service.getRecipesByInput(item2.getId());

        Assertions.assertTrue(recipesByInput1.containsKey(testCategory1));
        Assertions.assertTrue(recipesByInput1.containsKey(testCategory2));
        Assertions.assertTrue(recipesByInput2.containsKey(testCategory1));
        Assertions.assertTrue(recipesByInput2.containsKey(testCategory2));

        // item1 should be used in recipes 1, 2, 3
        Assertions.assertTrue(recipesByInput1.get(testCategory1).contains(testRecipe1));
        Assertions.assertTrue(recipesByInput1.get(testCategory1).contains(testRecipe2));
        Assertions.assertTrue(recipesByInput1.get(testCategory2).contains(testRecipe3));
        Assertions.assertFalse(recipesByInput1.get(testCategory2).contains(testRecipe4));

        // item2 should be used in recipes 1, 2, 4
        Assertions.assertTrue(recipesByInput2.get(testCategory1).contains(testRecipe1));
        Assertions.assertTrue(recipesByInput2.get(testCategory1).contains(testRecipe2));
        Assertions.assertFalse(recipesByInput2.get(testCategory2).contains(testRecipe3));
        Assertions.assertTrue(recipesByInput2.get(testCategory2).contains(testRecipe4));

        Assertions.assertEquals(3, service.getNumberOfRecipesUsedIn(item1.getId()));
        Assertions.assertEquals(3, service.getNumberOfRecipesUsedIn(item2.getId()));

    }

    @Test
    @DisplayName("Test the hashing function used by the service")
    void testHashIgnoreAmount() {
        SlimefunRecipeService service = new SlimefunRecipeService();
        
        ItemStack[] items1 = new ItemStack[] {
            null, null, null,
            null, new ItemStack(Material.STICK), null,
            new ItemStack(Material.STICK), null, null
        };
        ItemStack[] items2 = new ItemStack[] {
            null, null, null,
            null, new ItemStack(Material.STICK, 64), null,
            new ItemStack(Material.STICK), null, null
        };
        ItemStack[] items3 = new ItemStack[] {
            null, null, new ItemStack(Material.STICK),
            null, new ItemStack(Material.STICK), null,
            null, null, null
        };

        int hash1 = service.hashIgnoreAmount(items1, testCategory1);
        int hash2 = service.hashIgnoreAmount(items2, testCategory1);
        int hash3 = service.hashIgnoreAmount(items3, testCategory1);
        int hash4 = service.hashIgnoreAmount(items1, testCategory2);

        Assertions.assertEquals(hash1, hash2);
        Assertions.assertNotEquals(hash1, hash3);
        Assertions.assertNotEquals(hash1, hash4);

    }

    @Test
    @DisplayName("Test the different CachingStrategy enums")
    void testCachingStrategies() {
        SlimefunRecipeService service = new SlimefunRecipeService();
        
        ItemStack[] input = new ItemStack[] {
            null, null, null,
            null, new ItemStack(Material.OAK_LOG), null,
            null, null, null
        };
        
        ItemStack[] singleCraft = new ItemStack[] {
            null, null, null,
            null, new ItemStack(Material.OAK_LOG), null,
            null, null, null
        };
        ItemStack[] multipleCraft = new ItemStack[] {
            null, null, null,
            null, new ItemStack(Material.OAK_LOG, 32), null,
            null, null, null
        };
        ItemStack[] noCraft = new ItemStack[9];

        int singleHash = service.hashIgnoreAmount(singleCraft, testCategory1);
        int multipleHash = service.hashIgnoreAmount(multipleCraft, testCategory1);
        int noHash = service.hashIgnoreAmount(noCraft, testCategory1);

        Recipe testRecipe = Recipe.of(RecipeStructure.IDENTICAL, input, new ItemStack(Material.OAK_PLANKS, 4));

        service.registerRecipe(testCategory1, testRecipe);

        // Not cached
        service.searchRecipes(testCategory1, singleCraft, CachingStrategy.NEVER);
        Assertions.assertFalse(service.getCache().containsKey(singleHash));
        service.clearCache();

        // Not cached
        service.searchRecipes(testCategory1, multipleCraft, CachingStrategy.NEVER);
        Assertions.assertFalse(service.getCache().containsKey(multipleHash));
        service.clearCache();

        // Not cached
        service.searchRecipes(testCategory1, noCraft, CachingStrategy.NEVER);
        Assertions.assertFalse(service.getCache().containsKey(noHash));
        service.clearCache();

        // Not cached
        service.searchRecipes(testCategory1, singleCraft, CachingStrategy.IF_MULTIPLE_CRAFTABLE);
        Assertions.assertFalse(service.getCache().containsKey(singleHash));
        service.clearCache();

        // Cached
        service.searchRecipes(testCategory1, multipleCraft, CachingStrategy.IF_MULTIPLE_CRAFTABLE);
        Assertions.assertEquals(testRecipe, service.getFromCache(multipleHash).map(result -> result.getRecipe()).orElse(null));
        service.clearCache();

        // Cached, no match
        service.searchRecipes(testCategory1, noCraft, CachingStrategy.IF_MULTIPLE_CRAFTABLE);
        Assertions.assertFalse(service.getFromCache(noHash).map(result -> result.isMatch()).orElse(true));
        service.clearCache();

        // Cached
        service.searchRecipes(testCategory1, singleCraft, CachingStrategy.ALWAYS);
        Assertions.assertEquals(testRecipe, service.getFromCache(singleHash).map(result -> result.getRecipe()).orElse(null));
        service.clearCache();

        // Cached
        service.searchRecipes(testCategory1, multipleCraft, CachingStrategy.ALWAYS);
        Assertions.assertEquals(testRecipe, service.getFromCache(multipleHash).map(result -> result.getRecipe()).orElse(null));
        service.clearCache();

        // Cached, no match
        service.searchRecipes(testCategory1, noCraft, CachingStrategy.ALWAYS);
        Assertions.assertFalse(service.getFromCache(noHash).map(result -> result.isMatch()).orElse(true));
        service.clearCache();

    }
}
