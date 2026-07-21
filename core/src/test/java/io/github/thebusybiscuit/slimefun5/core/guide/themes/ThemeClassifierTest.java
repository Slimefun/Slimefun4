package io.github.thebusybiscuit.slimefun5.core.guide.themes;

import org.bukkit.Material;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockbukkit.mockbukkit.MockBukkit;

/**
 * Verifies the material-family heuristic that splits an addon's items across the shared guide themes in
 * the categorized layout (a sword to Weapons, a pickaxe to Tools, ...). Materials that map to no specific
 * theme return null so the caller can fall back to Misc.
 */
class ThemeClassifierTest {

    @BeforeAll
    public static void load() {
        MockBukkit.mock();
    }

    @AfterAll
    public static void unload() {
        MockBukkit.unmock();
    }

    @Test
    @DisplayName("Swords and ranged weapons classify as Weapons")
    void weapons() {
        Assertions.assertEquals(GuideTheme.WEAPONS, ItemThemeClassifier.classifyMaterial(Material.DIAMOND_SWORD));
        Assertions.assertEquals(GuideTheme.WEAPONS, ItemThemeClassifier.classifyMaterial(Material.BOW));
    }

    @Test
    @DisplayName("Pickaxes, axes and utility tools classify as Tools")
    void tools() {
        Assertions.assertEquals(GuideTheme.TOOLS, ItemThemeClassifier.classifyMaterial(Material.IRON_PICKAXE));
        Assertions.assertEquals(GuideTheme.TOOLS, ItemThemeClassifier.classifyMaterial(Material.IRON_AXE));
        Assertions.assertEquals(GuideTheme.TOOLS, ItemThemeClassifier.classifyMaterial(Material.SHEARS));
    }

    @Test
    @DisplayName("Armor pieces and the elytra classify as Armor")
    void armor() {
        Assertions.assertEquals(GuideTheme.ARMOR, ItemThemeClassifier.classifyMaterial(Material.DIAMOND_CHESTPLATE));
        Assertions.assertEquals(GuideTheme.ARMOR, ItemThemeClassifier.classifyMaterial(Material.LEATHER_BOOTS));
        Assertions.assertEquals(GuideTheme.ARMOR, ItemThemeClassifier.classifyMaterial(Material.ELYTRA));
    }

    @Test
    @DisplayName("Edible items classify as Food")
    void food() {
        Assertions.assertEquals(GuideTheme.FOOD, ItemThemeClassifier.classifyMaterial(Material.APPLE));
    }

    @Test
    @DisplayName("Materials with no specific theme are left unclassified (caller falls back to Misc)")
    void unclassified() {
        Assertions.assertNull(ItemThemeClassifier.classifyMaterial(Material.DIAMOND));
        Assertions.assertNull(ItemThemeClassifier.classifyMaterial(Material.IRON_INGOT));
    }
}
