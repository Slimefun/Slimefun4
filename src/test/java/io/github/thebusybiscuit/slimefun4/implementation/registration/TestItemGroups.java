package io.github.thebusybiscuit.slimefun4.implementation.registration;

import java.time.LocalDate;
import java.time.Month;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.github.bakedlibs.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.items.groups.FlexItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.groups.LockedItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.groups.SeasonalItemGroup;
import io.github.thebusybiscuit.slimefun4.api.player.PlayerProfile;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.api.researches.Research;
import io.github.thebusybiscuit.slimefun4.core.guide.SlimefunGuideMode;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.test.TestUtilities;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;

class TestItemGroups {

    private static ServerMock server;
    private static Slimefun plugin;

    @BeforeAll
    public static void load() {
        server = MockBukkit.mock();
        plugin = MockBukkit.load(Slimefun.class);
    }

    @AfterAll
    public static void unload() {
        MockBukkit.unmock();
    }

    @Test
    @DisplayName("Test the Getters for ItemGroup")
    void testItemGroupGetters() {
        ItemGroup itemGroup = new ItemGroup(new NamespacedKey(plugin, "getter_test"), new CustomItemStack(Material.DIAMOND_AXE, "&6Testing"));

        Assertions.assertEquals(3, itemGroup.getTier());
        Assertions.assertEquals(new NamespacedKey(Slimefun.instance(), "getter_test"), itemGroup.getKey());
        Assertions.assertEquals("Testing", itemGroup.getUnlocalizedName());
        Assertions.assertEquals(0, itemGroup.getItems().size());

        Assertions.assertNull(itemGroup.getAddon());
        itemGroup.register(plugin);
        Assertions.assertEquals(plugin, itemGroup.getAddon());
    }

    @Test
    @DisplayName("Test adding an item to a ItemGroup")
    void testAddItem() {
        ItemGroup itemGroup = new ItemGroup(new NamespacedKey(plugin, "items_test"), new CustomItemStack(Material.DIAMOND_AXE, "&6Testing"));
        SlimefunItem item = TestUtilities.mockSlimefunItem(plugin, "ITEM_GROUPS_TEST_ITEM", new CustomItemStack(Material.BAMBOO, "&6Test Bamboo"));
        item.setItemGroup(itemGroup);
        item.register(plugin);
        item.load();

        Assertions.assertTrue(itemGroup.getItems().contains(item));
        Assertions.assertEquals(1, itemGroup.getItems().size());

        // Size must still be 1 since we disallow duplicates
        item.setItemGroup(itemGroup);

        Assertions.assertEquals(1, itemGroup.getItems().size());
        Assertions.assertThrows(IllegalArgumentException.class, () -> itemGroup.add(null));
    }

    @Test
    @DisplayName("Test hidden Item Groups")
    void testHidden() {
        ItemGroup group = new ItemGroup(new NamespacedKey(plugin, "hiddenItemGroup"), new ItemStack(Material.BEACON));
        Player player = server.addPlayer();

        // Empty Item Groups are also hidden
        Assertions.assertFalse(group.isVisible(player));

        SlimefunItem disabledItem = TestUtilities.mockSlimefunItem(plugin, "DISABLED_ITEM_GROUP_ITEM", new CustomItemStack(Material.BEETROOT, "&4Disabled"));
        Slimefun.getItemCfg().setValue("DISABLED_ITEM_GROUP_ITEM.enabled", false);
        disabledItem.setItemGroup(group);
        disabledItem.register(plugin);
        disabledItem.load();

        // A disabled Item should also make the ItemGroup hide
        Assertions.assertFalse(group.isVisible(player));

        SlimefunItem item = TestUtilities.mockSlimefunItem(plugin, "ITEM_GROUP_HIDDEN_TEST", new CustomItemStack(Material.BAMBOO, "&6Test Bamboo"));
        item.setItemGroup(group);
        item.setHidden(true);
        item.register(plugin);
        item.load();

        // A hidden Item should also make the ItemGroup hide
        Assertions.assertFalse(group.isVisible(player));

        item.setHidden(false);
        Assertions.assertTrue(group.isVisible(player));
    }

    @Test
    @DisplayName("Test ItemGroup#contains")
    void testContains() {
        SlimefunItem item = TestUtilities.mockSlimefunItem(plugin, "ITEM_GROUP_TEST_ITEM_2", new CustomItemStack(Material.BOW, "&6Test Bow"));
        item.register(plugin);
        item.load();

        ItemGroup group = item.getItemGroup();

        Assertions.assertTrue(group.contains(item));
        Assertions.assertFalse(group.contains(null));

        // Unregistered Item
        Assertions.assertFalse(group.contains(TestUtilities.mockSlimefunItem(plugin, "NULL", new ItemStack(Material.BEDROCK))));
    }

    @Test
    @DisplayName("Test LockedItemGroup parental locking")
    void testLockedItemGroupsParents() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new LockedItemGroup(new NamespacedKey(plugin, "locked"), new CustomItemStack(Material.GOLD_NUGGET, "&6Locked Test"), (NamespacedKey) null));

        ItemGroup group = new ItemGroup(new NamespacedKey(plugin, "unlocked"), new CustomItemStack(Material.EMERALD, "&5I am SHERlocked"));
        group.register(plugin);

        ItemGroup unregistered = new ItemGroup(new NamespacedKey(plugin, "unregistered"), new CustomItemStack(Material.EMERALD, "&5I am unregistered"));

        LockedItemGroup locked = new LockedItemGroup(new NamespacedKey(plugin, "locked"), new CustomItemStack(Material.GOLD_NUGGET, "&6Locked Test"), group.getKey(), unregistered.getKey());
        locked.register(plugin);

        Assertions.assertTrue(locked.getParents().contains(group));
        Assertions.assertFalse(locked.getParents().contains(unregistered));

        locked.removeParent(group);
        Assertions.assertFalse(locked.getParents().contains(group));

        Assertions.assertThrows(IllegalArgumentException.class, () -> locked.addParent(locked));
        Assertions.assertThrows(IllegalArgumentException.class, () -> locked.addParent(null));

        locked.addParent(group);
        Assertions.assertTrue(locked.getParents().contains(group));
    }

    @Test
    @DisplayName("Test an unlocked LockedItemGroup")
    void testLockedItemGroupsUnlocking() throws InterruptedException {
        Player player = server.addPlayer();
        PlayerProfile profile = TestUtilities.awaitProfile(player);

        Assertions.assertThrows(IllegalArgumentException.class, () -> new LockedItemGroup(new NamespacedKey(plugin, "locked"), new CustomItemStack(Material.GOLD_NUGGET, "&6Locked Test"), (NamespacedKey) null));

        ItemGroup group = new ItemGroup(new NamespacedKey(plugin, "parent"), new CustomItemStack(Material.EMERALD, "&5I am SHERlocked"));
        group.register(plugin);

        LockedItemGroup locked = new LockedItemGroup(new NamespacedKey(plugin, "locked2"), new CustomItemStack(Material.GOLD_NUGGET, "&6Locked Test"), group.getKey());
        locked.register(plugin);

        // No Items, so it should be unlocked
        Assertions.assertTrue(locked.hasUnlocked(player, profile));

        SlimefunItem item = new SlimefunItem(group, new SlimefunItemStack("LOCKED_ITEMGROUP_TEST", new CustomItemStack(Material.LANTERN, "&6Test Item for locked categories")), RecipeType.NULL, new ItemStack[9]);
        item.register(plugin);
        item.load();

        Slimefun.getRegistry().setResearchingEnabled(true);
        Research research = new Research(new NamespacedKey(plugin, "cant_touch_this"), 432432, "MC Hammer", 90);
        research.addItems(item);
        research.register();

        Assertions.assertFalse(profile.hasUnlocked(research));
        Assertions.assertFalse(locked.hasUnlocked(player, profile));

        profile.setResearched(research, true);

        Assertions.assertTrue(locked.hasUnlocked(player, profile));
    }

    @Test
    @DisplayName("Test a seasonal ItemGroup")
    void ItemGroups() {
        // ItemGroup with current Month
        Month month = LocalDate.now().getMonth();
        SeasonalItemGroup group = new SeasonalItemGroup(new NamespacedKey(plugin, "seasonal"), month, 1, new CustomItemStack(Material.NETHER_STAR, "&cSeasonal Test"));
        SlimefunItem item = TestUtilities.mockSlimefunItem(plugin, "SEASONAL_ITEM", new CustomItemStack(Material.NETHER_STAR, "&dSeasonal Test Star"));
        item.setItemGroup(group);
        item.register(plugin);
        item.load();

        Player player = server.addPlayer();

        Assertions.assertEquals(month, group.getMonth());
        Assertions.assertTrue(group.isVisible(player));

        // ItemGroup with future Month
        SeasonalItemGroup itemGroup2 = new SeasonalItemGroup(group.getKey(), month.plus(6), 1, new CustomItemStack(Material.MILK_BUCKET, "&dSeasonal Test"));
        Assertions.assertFalse(itemGroup2.isVisible(player));
    }

    @Test
    @DisplayName("Test the FlexItemGroup")
    void testFlexItemGroup() {
        FlexItemGroup group = new FlexItemGroup(new NamespacedKey(plugin, "flex"), new CustomItemStack(Material.REDSTONE, "&4Weird flex but ok")) {

            @Override
            public void open(Player p, PlayerProfile profile, SlimefunGuideMode layout) {
                // Nothing
            }

            @Override
            public boolean isVisible(Player p, PlayerProfile profile, SlimefunGuideMode layout) {
                return true;
            }
        };

        Player player = server.addPlayer();
        Assertions.assertTrue(group.isVisible(player));

        Assertions.assertThrows(UnsupportedOperationException.class, () -> group.add(null));
        Assertions.assertThrows(UnsupportedOperationException.class, () -> group.contains(null));
        Assertions.assertThrows(UnsupportedOperationException.class, () -> group.remove(null));
        Assertions.assertThrows(UnsupportedOperationException.class, () -> group.getItems());
    }
}
