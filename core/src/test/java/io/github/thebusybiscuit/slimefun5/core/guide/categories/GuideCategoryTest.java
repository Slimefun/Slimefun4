package io.github.thebusybiscuit.slimefun5.core.guide.categories;

import java.util.List;

import com.cryptomorin.xseries.XMaterial;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockbukkit.mockbukkit.MockBukkit;

import io.github.thebusybiscuit.slimefun5.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun5.libraries.keys.NamespacedKey;

class GuideCategoryTest {

    @BeforeAll
    public static void load() {
        MockBukkit.mock();
        // Loading Slimefun initialises the version-dependent item-flag helper the ItemGroup constructor
        // uses; without it, constructing an ItemGroup throws ExceptionInInitializerError.
        MockBukkit.load(Slimefun.class);
    }

    @AfterAll
    public static void unload() {
        MockBukkit.unmock();
    }

    private static GuideCategory cat(String id, int order) {
        return new GuideCategory(id, "&7" + id, XMaterial.CHEST, order);
    }

    @Test
    void registerAndLookup() {
        GuideCategoryRegistry reg = new GuideCategoryRegistry();
        GuideCategory weapons = cat("weapons", 10);
        reg.register(weapons);
        Assertions.assertSame(weapons, reg.getById("weapons"));
        Assertions.assertNull(reg.getById("nope"));
        Assertions.assertNull(reg.getById(null));
    }

    @Test
    void getAllIsOrderedByOrderThenId() {
        GuideCategoryRegistry reg = new GuideCategoryRegistry();
        reg.register(cat("b", 20));
        reg.register(cat("a", 20));
        reg.register(cat("z", 10));
        List<GuideCategory> all = reg.getAll();
        Assertions.assertEquals("z", all.get(0).getId());
        Assertions.assertEquals("a", all.get(1).getId());
        Assertions.assertEquals("b", all.get(2).getId());
    }

    @Test
    void registerOverridesSameId() {
        GuideCategoryRegistry reg = new GuideCategoryRegistry();
        reg.register(cat("weapons", 10));
        GuideCategory replacement = cat("weapons", 15);
        reg.register(replacement);
        Assertions.assertSame(replacement, reg.getById("weapons"));
        Assertions.assertEquals(1, reg.getAll().size());
    }

    @Test
    @DisplayName("classifyMaterial types weapons/tools/armor/food; axe is a Tool; unknown is null")
    void classifierMaterialHeuristic() {
        Assertions.assertEquals(DefaultGuideCategories.WEAPONS, ItemTypeClassifier.classifyMaterial(Material.DIAMOND_SWORD));
        Assertions.assertEquals(DefaultGuideCategories.TOOLS, ItemTypeClassifier.classifyMaterial(Material.IRON_PICKAXE));
        Assertions.assertEquals(DefaultGuideCategories.TOOLS, ItemTypeClassifier.classifyMaterial(Material.DIAMOND_AXE));
        Assertions.assertEquals(DefaultGuideCategories.ARMOR, ItemTypeClassifier.classifyMaterial(Material.DIAMOND_CHESTPLATE));
        Assertions.assertEquals(DefaultGuideCategories.FOOD, ItemTypeClassifier.classifyMaterial(Material.APPLE));
        Assertions.assertNull(ItemTypeClassifier.classifyMaterial(Material.DIAMOND));
    }

    @Test
    @DisplayName("typeSingular maps ids to section-title words")
    void typeSingularWords() {
        Assertions.assertEquals("Weapon", ItemTypeClassifier.typeSingular(DefaultGuideCategories.WEAPONS));
        Assertions.assertEquals("Machine", ItemTypeClassifier.typeSingular(DefaultGuideCategories.MACHINES));
        Assertions.assertEquals("Misc", ItemTypeClassifier.typeSingular("resources"));
    }

    @Test
    @SuppressWarnings("deprecation")
    void legacySetThemeDelegatesToCategory() {
        // Addons in the wild (Networks, InfinityExpansion, ...) call the old setTheme("machines") with the
        // exact ids that are now canonical category ids. setTheme must survive (deprecated) and delegate,
        // or those addons NoSuchMethodError on enable and their groups never register (invisible in guide).
        ItemGroup group = new ItemGroup(new NamespacedKey("myaddon", "machines"), new org.bukkit.inventory.ItemStack(org.bukkit.Material.FURNACE));
        group.setTheme("machines");
        Assertions.assertEquals("machines", group.getCategoryId());
        Assertions.assertEquals("machines", group.getThemeId());
    }

    @Test
    void resolveCategoryIdUsesDeclaredThenAddonFallback() {
        ItemGroup declared = new ItemGroup(new NamespacedKey("myaddon", "weapons"), new ItemStack(Material.DIAMOND_SWORD));
        declared.setCategory("weapons");
        Assertions.assertEquals("weapons", CategoryMenuBuilder.resolveCategoryId(declared));

        ItemGroup undeclared = new ItemGroup(new NamespacedKey("myaddon", "stuff"), new ItemStack(Material.CHEST));
        Assertions.assertEquals("addon:myaddon", CategoryMenuBuilder.resolveCategoryId(undeclared));
    }
}
