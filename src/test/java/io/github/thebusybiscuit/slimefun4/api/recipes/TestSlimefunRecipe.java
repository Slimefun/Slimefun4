package io.github.thebusybiscuit.slimefun4.api.recipes;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import be.seeseemelk.mockbukkit.MockBukkit;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.test.TestUtilities;

class TestSlimefunRecipe {

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
    @DisplayName("Test recipes disabling when its inputs or output is disabled")
    void testDisabling() {
        SlimefunItem disabledItem = TestUtilities.mockSlimefunItem(plugin, "DISABLED_RECIPE_TEST", new ItemStack(Material.IRON_INGOT));
        Slimefun.getItemCfg().setValue("DISABLED_RECIPE_TEST.enabled", false);
        disabledItem.register(plugin);
        
        Recipe inputRecipe = Recipe.of(RecipeStructure.IDENTICAL, disabledItem.getItem(), new ItemStack(Material.IRON_NUGGET));
        Recipe outputRecipe = Recipe.of(RecipeStructure.IDENTICAL, new ItemStack(Material.IRON_NUGGET), disabledItem.getItem());

        Assertions.assertTrue(inputRecipe.isDisabled());
        Assertions.assertTrue(outputRecipe.isDisabled());
    }
    
}
