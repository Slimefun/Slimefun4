package io.github.thebusybiscuit.slimefun4.api.recipes;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import be.seeseemelk.mockbukkit.MockBukkit;
import io.github.thebusybiscuit.slimefun4.api.recipes.components.ItemComponent;
import io.github.thebusybiscuit.slimefun4.api.recipes.components.RecipeComponent;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;

class TestRecipeStructure {

    @BeforeAll
    public static void load() {
        MockBukkit.mock();
        MockBukkit.load(Slimefun.class);
    }

    @AfterAll
    public static void unload() {
        MockBukkit.unmock();
    }
    
    @Test
    @DisplayName("Tests IDENTICAL structure matching")
    void testIdentical() {

        Assertions.assertTrue(RecipeStructure.IDENTICAL.match(new ItemStack[9], List.of(
            RecipeComponent.AIR, RecipeComponent.AIR, RecipeComponent.AIR,
            RecipeComponent.AIR, RecipeComponent.AIR, RecipeComponent.AIR,
            RecipeComponent.AIR, RecipeComponent.AIR, RecipeComponent.AIR
        )).isMatch());

        Assertions.assertTrue(RecipeStructure.IDENTICAL.match(new ItemStack[] {
            null, null, null,
            null, new ItemStack(Material.OAK_LOG), null,
            new ItemStack(Material.OAK_LOG), null, null
        }, List.of(
            RecipeComponent.AIR, RecipeComponent.AIR, RecipeComponent.AIR,
            RecipeComponent.AIR, new ItemComponent(Material.OAK_LOG), RecipeComponent.AIR,
            new ItemComponent(Material.OAK_LOG), RecipeComponent.AIR, RecipeComponent.AIR
        )).isMatch());

        Assertions.assertFalse(RecipeStructure.IDENTICAL.match(new ItemStack[] {
            null, null, null,
            null, new ItemStack(Material.OAK_LOG), null,
            new ItemStack(Material.OAK_LOG), null, null
        }, List.of(
            RecipeComponent.AIR, RecipeComponent.AIR, RecipeComponent.AIR,
            RecipeComponent.AIR, RecipeComponent.AIR, new ItemComponent(Material.OAK_LOG),
            RecipeComponent.AIR, new ItemComponent(Material.OAK_LOG), RecipeComponent.AIR
        )).isMatch());

        Assertions.assertFalse(RecipeStructure.IDENTICAL.match(new ItemStack[] {
            null, null, null,
            null, new ItemStack(Material.OAK_LOG), null,
            new ItemStack(Material.OAK_LOG), null, null
        }, List.of(RecipeComponent.AIR)).isMatch());

    }
    
    @Test
    @DisplayName("Tests SHAPED structure matching")
    void testShaped() {

        Assertions.assertTrue(RecipeStructure.SHAPED.match(new ItemStack[9], List.of(
            RecipeComponent.AIR, RecipeComponent.AIR, RecipeComponent.AIR,
            RecipeComponent.AIR, RecipeComponent.AIR, RecipeComponent.AIR,
            RecipeComponent.AIR, RecipeComponent.AIR, RecipeComponent.AIR
        )).isMatch());

        Assertions.assertTrue(RecipeStructure.SHAPED.match(new ItemStack[] {
            null, null, null,
            null, new ItemStack(Material.OAK_LOG), null,
            new ItemStack(Material.OAK_LOG), null, null
        }, List.of(
            RecipeComponent.AIR, RecipeComponent.AIR, RecipeComponent.AIR,
            RecipeComponent.AIR, new ItemComponent(Material.OAK_LOG), RecipeComponent.AIR,
            new ItemComponent(Material.OAK_LOG), RecipeComponent.AIR, RecipeComponent.AIR
        )).isMatch());

        Assertions.assertTrue(RecipeStructure.SHAPED.match(new ItemStack[] {
            null, null, null,
            null, new ItemStack(Material.OAK_LOG), null,
            new ItemStack(Material.OAK_LOG), null, null
        }, List.of(
            RecipeComponent.AIR, RecipeComponent.AIR, RecipeComponent.AIR,
            RecipeComponent.AIR, RecipeComponent.AIR, new ItemComponent(Material.OAK_LOG),
            RecipeComponent.AIR, new ItemComponent(Material.OAK_LOG), RecipeComponent.AIR
        )).isMatch());

        Assertions.assertFalse(RecipeStructure.SHAPED.match(new ItemStack[] {
            null, null, null,
            null, new ItemStack(Material.OAK_LOG), null,
            new ItemStack(Material.OAK_LOG), null, null
        }, List.of(
            RecipeComponent.AIR, RecipeComponent.AIR, RecipeComponent.AIR,
            RecipeComponent.AIR, RecipeComponent.AIR, RecipeComponent.AIR, 
            new ItemComponent(Material.OAK_LOG), RecipeComponent.AIR, new ItemComponent(Material.OAK_LOG)
        )).isMatch());

        Assertions.assertFalse(RecipeStructure.SHAPED.match(new ItemStack[] {
            null, null, null,
            null, new ItemStack(Material.OAK_LOG), null,
            new ItemStack(Material.OAK_LOG), null, null
        }, List.of(
            RecipeComponent.AIR, RecipeComponent.AIR, new ItemComponent(Material.OAK_LOG),
            RecipeComponent.AIR, RecipeComponent.AIR, RecipeComponent.AIR, 
            new ItemComponent(Material.OAK_LOG), RecipeComponent.AIR, new ItemComponent(Material.OAK_LOG)
        )).isMatch());

        Assertions.assertFalse(RecipeStructure.SHAPED.match(new ItemStack[] {
            null, null, null,
            null, new ItemStack(Material.OAK_LOG), null,
            new ItemStack(Material.OAK_LOG), null, null
        }, List.of(RecipeComponent.AIR)).isMatch());

    }
    
    @Test
    @DisplayName("Tests SHAPELESS structure matching")
    void testShapeless() {

        Assertions.assertTrue(RecipeStructure.SHAPELESS.match(new ItemStack[9], List.of(
            RecipeComponent.AIR, RecipeComponent.AIR, RecipeComponent.AIR,
            RecipeComponent.AIR, RecipeComponent.AIR, RecipeComponent.AIR,
            RecipeComponent.AIR, RecipeComponent.AIR, RecipeComponent.AIR
        )).isMatch());

        Assertions.assertTrue(RecipeStructure.SHAPELESS.match(new ItemStack[] {
            null, null, null,
            null, new ItemStack(Material.OAK_LOG), null,
            new ItemStack(Material.OAK_LOG), null, null
        }, List.of(
            RecipeComponent.AIR, RecipeComponent.AIR, RecipeComponent.AIR,
            RecipeComponent.AIR, new ItemComponent(Material.OAK_LOG), RecipeComponent.AIR,
            new ItemComponent(Material.OAK_LOG), RecipeComponent.AIR, RecipeComponent.AIR
        )).isMatch());

        Assertions.assertTrue(RecipeStructure.SHAPELESS.match(new ItemStack[] {
            null, null, null,
            null, new ItemStack(Material.OAK_LOG), null,
            new ItemStack(Material.OAK_LOG), null, null
        }, List.of(
            RecipeComponent.AIR, RecipeComponent.AIR, RecipeComponent.AIR,
            RecipeComponent.AIR, RecipeComponent.AIR, RecipeComponent.AIR, 
            new ItemComponent(Material.OAK_LOG), RecipeComponent.AIR, new ItemComponent(Material.OAK_LOG)
        )).isMatch());

        Assertions.assertFalse(RecipeStructure.SHAPELESS.match(new ItemStack[] {
            null, null, null,
            null, new ItemStack(Material.OAK_LOG), null,
            new ItemStack(Material.OAK_LOG), null, null
        }, List.of(
            RecipeComponent.AIR, RecipeComponent.AIR, new ItemComponent(Material.OAK_LOG),
            RecipeComponent.AIR, RecipeComponent.AIR, RecipeComponent.AIR, 
            new ItemComponent(Material.OAK_LOG), RecipeComponent.AIR, new ItemComponent(Material.OAK_LOG)
        )).isMatch());

        Assertions.assertFalse(RecipeStructure.SHAPELESS.match(new ItemStack[] {
            null, null, null,
            null, new ItemStack(Material.OAK_LOG), null,
            new ItemStack(Material.OAK_LOG), null, null
        }, List.of(RecipeComponent.AIR)).isMatch());

    }

    @Test
    @DisplayName("Tests SHAPELESS structure matching")
    void testSubset() {

        Assertions.assertTrue(RecipeStructure.SUBSET.match(new ItemStack[9], List.of(
            RecipeComponent.AIR, RecipeComponent.AIR, RecipeComponent.AIR,
            RecipeComponent.AIR, RecipeComponent.AIR, RecipeComponent.AIR,
            RecipeComponent.AIR, RecipeComponent.AIR, RecipeComponent.AIR
        )).isMatch());

        Assertions.assertTrue(RecipeStructure.SUBSET.match(new ItemStack[] {
            new ItemStack(Material.OAK_LOG), new ItemStack(Material.OAK_LOG)
        }, List.of(
            RecipeComponent.AIR, RecipeComponent.AIR, RecipeComponent.AIR,
            RecipeComponent.AIR, new ItemComponent(Material.OAK_LOG), RecipeComponent.AIR,
            new ItemComponent(Material.OAK_LOG), RecipeComponent.AIR, RecipeComponent.AIR
        )).isMatch());

        Assertions.assertTrue(RecipeStructure.SUBSET.match(new ItemStack[] {
            null, null, null,
            null, new ItemStack(Material.OAK_LOG), null,
            new ItemStack(Material.OAK_LOG), null, null, null, null, null, null, new ItemStack(Material.OAK_LOG)
        }, List.of(
            RecipeComponent.AIR, RecipeComponent.AIR, RecipeComponent.AIR,
            RecipeComponent.AIR, RecipeComponent.AIR, RecipeComponent.AIR, 
            new ItemComponent(Material.OAK_LOG), RecipeComponent.AIR, new ItemComponent(Material.OAK_LOG)
        )).isMatch());

        Assertions.assertFalse(RecipeStructure.SUBSET.match(new ItemStack[] {
            null, null, null,
            null, new ItemStack(Material.OAK_LOG), null,
            new ItemStack(Material.OAK_LOG), null, null
        }, List.of(
            RecipeComponent.AIR, RecipeComponent.AIR, new ItemComponent(Material.OAK_LOG),
            RecipeComponent.AIR, RecipeComponent.AIR, RecipeComponent.AIR, 
            new ItemComponent(Material.OAK_LOG), RecipeComponent.AIR, new ItemComponent(Material.OAK_LOG)
        )).isMatch());

        Assertions.assertTrue(RecipeStructure.SUBSET.match(new ItemStack[] {
            null, null, null,
            null, new ItemStack(Material.OAK_LOG), null,
            new ItemStack(Material.OAK_LOG), null, null
        }, List.of(RecipeComponent.AIR)).isMatch());

    }
}
