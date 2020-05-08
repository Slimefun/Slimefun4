package io.github.thebusybiscuit.slimefun4.tests.items;

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
import io.github.thebusybiscuit.slimefun4.core.categories.SeasonalCategory;
import io.github.thebusybiscuit.slimefun4.implementation.items.VanillaItem;
import io.github.thebusybiscuit.slimefun4.mocks.SlimefunMocks;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.LockedCategory;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;

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
        Category category = SlimefunMocks.getCategory();

        Assertions.assertEquals(3, category.getTier());
        Assertions.assertEquals(new NamespacedKey(SlimefunPlugin.instance, "test"), category.getKey());
        Assertions.assertEquals("Test Category", category.getUnlocalizedName());
        Assertions.assertEquals(0, category.getItems().size());

        SlimefunItem item = SlimefunMocks.mockSlimefunItem("CATEGORY_TEST_ITEM", new CustomItem(Material.BAMBOO, "&6Test Bamboo"));
        item.register(plugin);
        item.load();

        Assertions.assertEquals(1, category.getItems().size());
        Assertions.assertTrue(category.getItems().contains(item));
    }

    @Test
    public void testHidden() {
        Category category = new Category(new NamespacedKey(plugin, "testCategory2"), new ItemStack(Material.BEACON));
        Player player = server.addPlayer();

        // Empty Categories are also hidden
        Assertions.assertTrue(category.isHidden(player));

        VanillaItem vanillaItem = SlimefunMocks.mockVanillaItem(Material.BEETROOT, false);
        vanillaItem.setCategory(category);
        vanillaItem.register(plugin);
        vanillaItem.load();

        // A disabled Item should also make the Category hide
        Assertions.assertTrue(category.isHidden(player));

        SlimefunItem item = SlimefunMocks.mockSlimefunItem("CATEGORY_HIDDEN_TEST", new CustomItem(Material.BAMBOO, "&6Test Bamboo"));
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
        Category category = SlimefunMocks.getCategory();
        SlimefunItem item = SlimefunMocks.mockSlimefunItem("CATEGORY_TEST_ITEM_2", new CustomItem(Material.BOW, "&6Test Bow"));
        item.register(plugin);
        item.load();

        Assertions.assertTrue(category.contains(item));
        Assertions.assertFalse(category.contains(null));
        Assertions.assertFalse(category.contains(SlimefunMocks.mockSlimefunItem("NULL", new ItemStack(Material.BEDROCK))));
    }

    @Test
    public void testLockedCategories() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new LockedCategory(new NamespacedKey(plugin, "locked"), new CustomItem(Material.GOLD_NUGGET, "&6Locked Test"), (NamespacedKey) null));

        Category category = SlimefunMocks.getCategory();
        LockedCategory locked = new LockedCategory(new NamespacedKey(plugin, "locked"), new CustomItem(Material.GOLD_NUGGET, "&6Locked Test"), category.getKey());
        locked.register();

        Assertions.assertTrue(locked.getParents().contains(category));

        locked.removeParent(category);
        Assertions.assertFalse(locked.getParents().contains(category));

        Assertions.assertThrows(IllegalArgumentException.class, () -> locked.addParent(locked));
        Assertions.assertThrows(IllegalArgumentException.class, () -> locked.addParent(null));

        locked.addParent(category);
        Assertions.assertTrue(locked.getParents().contains(category));
    }

    @Test
    public void testSeasonalCategories() {
        // Category with current Month
        Month month = LocalDate.now().getMonth();
        SeasonalCategory category = new SeasonalCategory(new NamespacedKey(plugin, "seasonal"), month, 1, new CustomItem(Material.NETHER_STAR, "&cSeasonal Test"));
        SlimefunItem item = SlimefunMocks.mockSlimefunItem("SEASONAL_ITEM", new CustomItem(Material.NETHER_STAR, "&dSeasonal Test Star"));
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
}
