package io.github.thebusybiscuit.slimefun4.api.recipes;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import be.seeseemelk.mockbukkit.MockBukkit;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;

class TestRecipeCategory {

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
    @Deprecated
    @DisplayName("Test backwards compatibility with RecipeType")
    void testBackwardsCompat() {
        RecipeType type = new RecipeType(new NamespacedKey(plugin, "crafting-table"), new ItemStack(Material.CRAFTING_TABLE));
        RecipeCategory category = new RecipeCategory(new NamespacedKey(plugin, "crafting-table"), new ItemStack(Material.CRAFTING_TABLE));

        Assertions.assertEquals(type.key(), category.key());
    }

    @Test
    void testRecipeCategory() {
        NamespacedKey key = new NamespacedKey(plugin, "crafting-table");
        ItemStack item = new ItemStack(Material.CRAFTING_TABLE);
        RecipeCategory category = new RecipeCategory(key, item, RecipeStructure.IDENTICAL);

        Assertions.assertEquals(key, category.getKey());
        Assertions.assertEquals("slimefun.crafting-table", category.getTranslationKey());
        Assertions.assertEquals(item, category.getDisplayItem());
        Assertions.assertEquals(RecipeStructure.IDENTICAL, category.getDefaultStructure());
    }
    
}
