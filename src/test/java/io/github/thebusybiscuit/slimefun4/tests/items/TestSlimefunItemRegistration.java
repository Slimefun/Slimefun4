package io.github.thebusybiscuit.slimefun4.tests.items;

import java.util.Optional;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import io.github.thebusybiscuit.slimefun4.implementation.items.VanillaItem;
import io.github.thebusybiscuit.slimefun4.mocks.SlimefunMocks;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.ItemState;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;

public class TestSlimefunItemRegistration {

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
    public void testSuccessfulRegistration() {
        String id = "TEST_ITEM";
        SlimefunItem item = SlimefunMocks.mockSlimefunItem(id, new CustomItem(Material.DIAMOND, "&cTest"));

        Assertions.assertEquals(ItemState.UNREGISTERED, item.getState());

        item.register(plugin);

        Assertions.assertEquals(ItemState.ENABLED, item.getState());
        Assertions.assertFalse(item.isDisabled());
        Assertions.assertEquals(id, item.getID());
        Assertions.assertFalse(item.isAddonItem());
        Assertions.assertEquals(item, SlimefunItem.getByID(id));
    }

    @Test
    public void testDisabledItem() {
        SlimefunItem item = SlimefunMocks.mockSlimefunItem("DISABLED_ITEM", new CustomItem(Material.DIAMOND, "&cTest"));
        SlimefunPlugin.getItemCfg().setValue("DISABLED_ITEM.enabled", false);
        item.register(plugin);

        Assertions.assertEquals(ItemState.DISABLED, item.getState());
        Assertions.assertTrue(item.isDisabled());
    }

    @Test
    public void testWikiPages() {
        SlimefunItem item = SlimefunMocks.mockSlimefunItem("WIKI_ITEM", new CustomItem(Material.BOOK, "&cTest"));
        item.register(plugin);

        Assertions.assertFalse(item.getWikipage().isPresent());

        // null should not be a valid argument
        Assertions.assertThrows(IllegalArgumentException.class, () -> item.addOficialWikipage(null));

        item.addOficialWikipage("Test");

        Optional<String> wiki = item.getWikipage();
        Assertions.assertTrue(wiki.isPresent());
        Assertions.assertEquals("https://github.com/TheBusyBiscuit/Slimefun4/wiki/Test", wiki.get());
    }

    @Test
    public void testGetItemName() {
        SlimefunItem item = SlimefunMocks.mockSlimefunItem("ITEM_NAME_TEST", new CustomItem(Material.DIAMOND, "&cTest"));
        item.register(plugin);

        Assertions.assertEquals(ChatColor.RED + "Test", item.getItemName());
    }

    @Test
    public void testVanillaItemFallback() {
        VanillaItem item = SlimefunMocks.mockVanillaItem(Material.ACACIA_SIGN, false);
        item.register(plugin);

        Assertions.assertTrue(item.isUseableInWorkbench());
        Assertions.assertEquals(ItemState.VANILLA_FALLBACK, item.getState());
        Assertions.assertTrue(item.isDisabled());
    }

    @Test
    public void testIdConflict() {
        SlimefunItem item = SlimefunMocks.mockSlimefunItem("DUPLICATE_ID", new CustomItem(Material.DIAMOND, "&cTest"));
        item.register(plugin);

        SlimefunItem item2 = SlimefunMocks.mockSlimefunItem("DUPLICATE_ID", new CustomItem(Material.DIAMOND, "&cTest"));
        item2.register(plugin);

        Assertions.assertEquals(ItemState.ENABLED, item.getState());
        Assertions.assertEquals(ItemState.UNREGISTERED, item2.getState());
    }

    @Test
    public void testRecipeOutput() {
        SlimefunItem item = SlimefunMocks.mockSlimefunItem("RECIPE_OUTPUT_TEST", new CustomItem(Material.DIAMOND, "&cTest"));
        item.register(plugin);

        Assertions.assertEquals(item.getItem(), item.getRecipeOutput());

        ItemStack output = new ItemStack(Material.EMERALD, 64);
        item.setRecipeOutput(output);
        Assertions.assertEquals(output, item.getRecipeOutput());

        item.setRecipeOutput(item.getItem());
        Assertions.assertEquals(item.getItem(), item.getRecipeOutput());
    }

    @Test
    public void testIsItem() {
        CustomItem item = new CustomItem(Material.BEACON, "&cItem Test");
        SlimefunItem sfItem = SlimefunMocks.mockSlimefunItem("IS_ITEM_TEST", item);
        sfItem.register(plugin);

        Assertions.assertTrue(sfItem.isItem(sfItem.getItem()));
        Assertions.assertTrue(sfItem.isItem(item));
        Assertions.assertTrue(sfItem.isItem(new CustomItem(Material.BEACON, "&cItem Test")));

        Assertions.assertFalse(sfItem.isItem(null));
        Assertions.assertFalse(sfItem.isItem(new ItemStack(Material.BEACON)));
        Assertions.assertFalse(sfItem.isItem(new CustomItem(Material.REDSTONE, "&cTest")));

        Assertions.assertEquals(sfItem, SlimefunItem.getByItem(item));
    }

    @Test
    public void testCategoryRegistration() {
        SlimefunItem item = SlimefunMocks.mockSlimefunItem("CATEGORY_TEST", new CustomItem(Material.DIAMOND, "&cTest"));
        item.register(plugin);
        item.load();

        // null should not be a valid argument
        Assertions.assertThrows(IllegalArgumentException.class, () -> item.setCategory(null));

        Category category = SlimefunMocks.getCategory();
        Category category2 = new Category(new NamespacedKey(plugin, "test2"), new CustomItem(Material.OBSIDIAN, "&6Test 2"));

        Assertions.assertTrue(category.contains(item));
        Assertions.assertFalse(category2.contains(item));
        Assertions.assertEquals(category, item.getCategory());

        item.setCategory(category2);
        Assertions.assertFalse(category.contains(item));
        Assertions.assertTrue(category2.contains(item));
        Assertions.assertEquals(category2, item.getCategory());
    }

    @Test
    public void testHiddenItem() {
        SlimefunItem item = SlimefunMocks.mockSlimefunItem("HIDDEN_TEST", new CustomItem(Material.DIAMOND, "&cTest"));
        item.setHidden(true);
        item.register(plugin);
        item.load();

        Category category = SlimefunMocks.getCategory();

        Assertions.assertTrue(item.isHidden());
        Assertions.assertFalse(category.contains(item));
        Assertions.assertEquals(category, item.getCategory());

        item.setHidden(false);
        Assertions.assertFalse(item.isHidden());
        Assertions.assertTrue(category.contains(item));
        Assertions.assertEquals(category, item.getCategory());

        item.setHidden(true);
        Assertions.assertTrue(item.isHidden());
        Assertions.assertFalse(category.contains(item));
        Assertions.assertEquals(category, item.getCategory());

        // Do nothing if the value hasn't changed
        item.setHidden(true);
        Assertions.assertTrue(item.isHidden());
        Assertions.assertFalse(category.contains(item));
    }
}
