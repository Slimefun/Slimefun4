package io.github.thebusybiscuit.slimefun4.testing.tests.registration;

import java.time.LocalDate;
import java.time.Month;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import io.github.thebusybiscuit.slimefun4.api.player.PlayerProfile;
import io.github.thebusybiscuit.slimefun4.core.categories.FlexCategory;
import io.github.thebusybiscuit.slimefun4.core.categories.LockedCategory;
import io.github.thebusybiscuit.slimefun4.core.categories.SeasonalCategory;
import io.github.thebusybiscuit.slimefun4.core.guide.SlimefunGuideLayout;
import io.github.thebusybiscuit.slimefun4.core.researching.Research;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.testing.TestUtilities;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

public class TestCategories {

    private static ServerMock server;
    private static SlimefunPlugin plugin;

    @BeforeAll
    public static void load() {
        server = MockBukkit.mock();
        plugin = MockBukkit.load(SlimefunPlugin.class);
    }

    @AfterAll
    public static void unload() {
        MockBukkit.unmock();
    }

    @Test
    public void testCategoryGetters() {
        Category category = new Category(new NamespacedKey(plugin, "getter_test"), new CustomItem(Material.DIAMOND_AXE, "&6Testing"));

        Assertions.assertEquals(3, category.getTier());
        Assertions.assertEquals(new NamespacedKey(SlimefunPlugin.instance(), "getter_test"), category.getKey());
        Assertions.assertEquals("Testing", category.getUnlocalizedName());
        Assertions.assertEquals(0, category.getItems().size());
    }

    @Test
    public void testAddItem() {
        Category category = new Category(new NamespacedKey(plugin, "items_test"), new CustomItem(Material.DIAMOND_AXE, "&6Testing"));
        SlimefunItem item = TestUtilities.mockSlimefunItem(plugin, "CATEGORY_ITEMS_TEST_ITEM", new CustomItem(Material.BAMBOO, "&6Test Bamboo"));
        item.setCategory(category);
        item.register(plugin);
        item.load();

        Assertions.assertTrue(category.getItems().contains(item));
        Assertions.assertEquals(1, category.getItems().size());

        // Size must still be 1 since we disallow duplicates
        item.setCategory(category);

        Assertions.assertEquals(1, category.getItems().size());
        Assertions.assertThrows(IllegalArgumentException.class, () -> category.add(null));
    }

    @Test
    public void testHidden() {
        Category category = new Category(new NamespacedKey(plugin, "hiddenCategory"), new ItemStack(Material.BEACON));
        Player player = server.addPlayer();

        // Empty Categories are also hidden
        Assertions.assertTrue(category.isHidden(player));

        SlimefunItem disabledItem = TestUtilities.mockSlimefunItem(plugin, "DISABLED_CATEGORY_ITEM", new CustomItem(Material.BEETROOT, "&4Disabled"));
        SlimefunPlugin.getItemCfg().setValue("DISABLED_CATEGORY_ITEM.enabled", false);
        disabledItem.setCategory(category);
        disabledItem.register(plugin);
        disabledItem.load();

        // A disabled Item should also make the Category hide
        Assertions.assertTrue(category.isHidden(player));

        SlimefunItem item = TestUtilities.mockSlimefunItem(plugin, "CATEGORY_HIDDEN_TEST", new CustomItem(Material.BAMBOO, "&6Test Bamboo"));
        item.setCategory(category);
        item.setHidden(true);
        item.register(plugin);
        item.load();

        // A hidden Item should also make the Category hide
        Assertions.assertTrue(category.isHidden(player));

        item.setHidden(false);
        Assertions.assertFalse(category.isHidden(player));
    }

    @Test
    public void testContains() {
        SlimefunItem item = TestUtilities.mockSlimefunItem(plugin, "CATEGORY_TEST_ITEM_2", new CustomItem(Material.BOW, "&6Test Bow"));
        item.register(plugin);
        item.load();

        Category category = item.getCategory();

        Assertions.assertTrue(category.contains(item));
        Assertions.assertFalse(category.contains(null));

        // Unregistered Item
        Assertions.assertFalse(category.contains(TestUtilities.mockSlimefunItem(plugin, "NULL", new ItemStack(Material.BEDROCK))));
    }

    @Test
    public void testLockedCategoriesParents() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new LockedCategory(new NamespacedKey(plugin, "locked"), new CustomItem(Material.GOLD_NUGGET, "&6Locked Test"), (NamespacedKey) null));

        Category category = new Category(new NamespacedKey(plugin, "unlocked"), new CustomItem(Material.EMERALD, "&5I am SHERlocked"));
        category.register();

        Category unregistered = new Category(new NamespacedKey(plugin, "unregistered"), new CustomItem(Material.EMERALD, "&5I am unregistered"));

        LockedCategory locked = new LockedCategory(new NamespacedKey(plugin, "locked"), new CustomItem(Material.GOLD_NUGGET, "&6Locked Test"), category.getKey(), unregistered.getKey());
        locked.register();

        Assertions.assertTrue(locked.getParents().contains(category));
        Assertions.assertFalse(locked.getParents().contains(unregistered));

        locked.removeParent(category);
        Assertions.assertFalse(locked.getParents().contains(category));

        Assertions.assertThrows(IllegalArgumentException.class, () -> locked.addParent(locked));
        Assertions.assertThrows(IllegalArgumentException.class, () -> locked.addParent(null));

        locked.addParent(category);
        Assertions.assertTrue(locked.getParents().contains(category));
    }

    @Test
    public void testLockedCategoriesUnlocking() throws InterruptedException {
        Player player = server.addPlayer();
        PlayerProfile profile = TestUtilities.awaitProfile(player);

        Assertions.assertThrows(IllegalArgumentException.class, () -> new LockedCategory(new NamespacedKey(plugin, "locked"), new CustomItem(Material.GOLD_NUGGET, "&6Locked Test"), (NamespacedKey) null));

        Category category = new Category(new NamespacedKey(plugin, "parent"), new CustomItem(Material.EMERALD, "&5I am SHERlocked"));
        category.register();

        LockedCategory locked = new LockedCategory(new NamespacedKey(plugin, "locked"), new CustomItem(Material.GOLD_NUGGET, "&6Locked Test"), category.getKey());
        locked.register();

        // No Items, so it should be unlocked
        Assertions.assertTrue(locked.hasUnlocked(player, profile));

        SlimefunItem item = new SlimefunItem(category, new SlimefunItemStack("LOCKED_CATEGORY_TEST", new CustomItem(Material.LANTERN, "&6Test Item for locked categories")), RecipeType.NULL, new ItemStack[9]);
        item.register(plugin);
        item.load();

        SlimefunPlugin.getRegistry().setResearchingEnabled(true);
        Research research = new Research(new NamespacedKey(plugin, "cant_touch_this"), 432432, "MC Hammer", 90);
        research.addItems(item);
        research.register();

        Assertions.assertFalse(profile.hasUnlocked(research));
        Assertions.assertFalse(locked.hasUnlocked(player, profile));

        profile.setResearched(research, true);

        Assertions.assertTrue(locked.hasUnlocked(player, profile));
    }

    @Test
    public void testSeasonalCategories() {
        // Category with current Month
        Month month = LocalDate.now().getMonth();
        SeasonalCategory category = new SeasonalCategory(new NamespacedKey(plugin, "seasonal"), month, 1, new CustomItem(Material.NETHER_STAR, "&cSeasonal Test"));
        SlimefunItem item = TestUtilities.mockSlimefunItem(plugin, "SEASONAL_ITEM", new CustomItem(Material.NETHER_STAR, "&dSeasonal Test Star"));
        item.setCategory(category);
        item.register(plugin);
        item.load();

        Player player = server.addPlayer();

        Assertions.assertEquals(month, category.getMonth());
        Assertions.assertFalse(category.isHidden(player));

        // Category with future Month
        SeasonalCategory category2 = new SeasonalCategory(category.getKey(), month.plus(6), 1, new CustomItem(Material.MILK_BUCKET, "&dSeasonal Test"));
        Assertions.assertTrue(category2.isHidden(player));
    }

    @Test
    public void testFlexCategory() {
        FlexCategory category = new FlexCategory(new NamespacedKey(plugin, "flex"), new CustomItem(Material.REDSTONE, "&4Weird flex but ok")) {

            @Override
            public void open(Player p, PlayerProfile profile, SlimefunGuideLayout layout) {
                // Nothing
            }

            @Override
            public boolean isVisible(Player p, PlayerProfile profile, SlimefunGuideLayout layout) {
                return true;
            }
        };

        Player player = server.addPlayer();
        Assertions.assertFalse(category.isHidden(player));

        Assertions.assertThrows(UnsupportedOperationException.class, () -> category.add(null));
        Assertions.assertThrows(UnsupportedOperationException.class, () -> category.contains(null));
        Assertions.assertThrows(UnsupportedOperationException.class, () -> category.remove(null));
        Assertions.assertThrows(UnsupportedOperationException.class, () -> category.getItems());
    }
}
