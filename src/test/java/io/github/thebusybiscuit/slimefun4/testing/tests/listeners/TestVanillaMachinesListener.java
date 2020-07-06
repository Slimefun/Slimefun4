package io.github.thebusybiscuit.slimefun4.testing.tests.listeners;

import org.apache.commons.lang.mutable.MutableObject;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.BrewingStand;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.Mockito;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import io.github.thebusybiscuit.slimefun4.core.guide.SlimefunGuide;
import io.github.thebusybiscuit.slimefun4.core.guide.SlimefunGuideLayout;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.implementation.items.VanillaItem;
import io.github.thebusybiscuit.slimefun4.implementation.listeners.VanillaMachinesListener;
import io.github.thebusybiscuit.slimefun4.testing.TestUtilities;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;

public class TestVanillaMachinesListener {

    private static SlimefunPlugin plugin;
    private static VanillaMachinesListener listener;
    private static ServerMock server;

    @BeforeAll
    public static void load() {
        server = MockBukkit.mock();
        plugin = MockBukkit.load(SlimefunPlugin.class);
        listener = new VanillaMachinesListener(plugin);
    }

    @AfterAll
    public static void unload() {
        MockBukkit.unmock();
    }

    private InventoryClickEvent mockGrindStoneEvent(ItemStack item) {
        Player player = server.addPlayer();
        Inventory inv = TestUtilities.mockInventory(InventoryType.GRINDSTONE, item, null);
        InventoryView view = player.openInventory(inv);
        InventoryClickEvent event = new InventoryClickEvent(view, SlotType.CONTAINER, 2, ClickType.LEFT, InventoryAction.PICKUP_ONE);

        listener.onGrindstone(event);
        return event;
    }

    private InventoryClickEvent mockAnvilEvent(ItemStack item) {
        Player player = server.addPlayer();
        Inventory inv = TestUtilities.mockInventory(InventoryType.ANVIL, item, null, new ItemStack(Material.IRON_CHESTPLATE));
        InventoryView view = player.openInventory(inv);
        InventoryClickEvent event = new InventoryClickEvent(view, SlotType.CONTAINER, 2, ClickType.LEFT, InventoryAction.PICKUP_ONE);

        listener.onAnvil(event);
        return event;
    }

    private InventoryClickEvent mockBrewingEvent(ItemStack item) {
        Player player = server.addPlayer();
        Inventory inv = TestUtilities.mockInventory(InventoryType.BREWING);
        Mockito.when(inv.getHolder()).thenReturn(Mockito.mock(BrewingStand.class));
        Mockito.when(inv.getSize()).thenReturn(5);

        InventoryView view = player.openInventory(inv);
        view.setCursor(item);
        InventoryClickEvent event = new InventoryClickEvent(view, SlotType.CONTAINER, 1, ClickType.LEFT, InventoryAction.PICKUP_ONE);
        listener.onPreBrew(event);
        return event;
    }

    private CraftItemEvent mockCraftingEvent(ItemStack item) {
        Recipe recipe = new ShapedRecipe(new NamespacedKey(plugin, "test_recipe"), new ItemStack(Material.EMERALD));
        Player player = server.addPlayer();

        CraftingInventory inv = Mockito.mock(CraftingInventory.class);
        Mockito.when(inv.getContents()).thenReturn(new ItemStack[] { item, null, null, null, null, null, null, null, null });

        InventoryView view = player.openInventory(inv);
        CraftItemEvent event = new CraftItemEvent(recipe, view, SlotType.RESULT, 9, ClickType.LEFT, InventoryAction.PICKUP_ALL);

        listener.onCraft(event);
        return event;
    }

    private PrepareItemCraftEvent mockPreCraftingEvent(ItemStack item) {
        Player player = server.addPlayer();

        CraftingInventory inv = Mockito.mock(CraftingInventory.class);
        MutableObject result = new MutableObject(new ItemStack(Material.EMERALD));

        Mockito.doAnswer(invocation -> {
            ItemStack argument = invocation.getArgument(0);
            result.setValue(argument);
            return null;
        }).when(inv).setResult(Mockito.any());

        Mockito.when(inv.getResult()).thenAnswer(invocation -> result.getValue());
        Mockito.when(inv.getContents()).thenReturn(new ItemStack[] { null, null, item, null, null, null, null, null, null });

        InventoryView view = player.openInventory(inv);
        PrepareItemCraftEvent event = new PrepareItemCraftEvent(inv, view, false);

        listener.onPrepareCraft(event);
        return event;
    }

    @Test
    public void testGrindStoneWithoutSlimefunItems() {
        InventoryClickEvent event = mockGrindStoneEvent(new ItemStack(Material.ENCHANTED_BOOK));
        Assertions.assertEquals(Result.DEFAULT, event.getResult());
    }

    @Test
    public void testGrindStoneWithSlimefunItem() {
        SlimefunItem item = TestUtilities.mockSlimefunItem(plugin, "ENCHANTED_MOCK_BOOK", new CustomItem(Material.ENCHANTED_BOOK, "&6Mock"));
        item.register(plugin);

        InventoryClickEvent event = mockGrindStoneEvent(item.getItem());
        Assertions.assertEquals(Result.DENY, event.getResult());
    }

    @Test
    public void testGrindStoneWithVanillaItem() {
        VanillaItem item = TestUtilities.mockVanillaItem(plugin, Material.ENCHANTED_BOOK, true);
        item.register(plugin);

        InventoryClickEvent event = mockGrindStoneEvent(item.getItem());
        Assertions.assertEquals(Result.DEFAULT, event.getResult());
    }

    @ParameterizedTest
    @EnumSource(SlimefunGuideLayout.class)
    public void testGrindStoneWithSlimefunGuide(SlimefunGuideLayout layout) {
        InventoryClickEvent event = mockGrindStoneEvent(SlimefunGuide.getItem(layout));
        Assertions.assertEquals(Result.DENY, event.getResult());
    }

    @Test
    public void testCraftEventWithoutSlimefunItems() {
        CraftItemEvent event = mockCraftingEvent(new ItemStack(Material.DIAMOND));
        Assertions.assertEquals(Result.DEFAULT, event.getResult());
    }

    @Test
    public void testCraftEventWithSlimefunItem() {
        SlimefunItem item = TestUtilities.mockSlimefunItem(plugin, "MOCK_DIAMOND", new CustomItem(Material.DIAMOND, "&cMock Diamond"));
        item.register(plugin);

        CraftItemEvent event = mockCraftingEvent(item.getItem());
        Assertions.assertEquals(Result.DENY, event.getResult());
    }

    @Test
    public void testCraftEventWithChangingSlimefunItem() {
        SlimefunItem item = TestUtilities.mockSlimefunItem(plugin, "CHANGING_ITEM", new CustomItem(Material.DIAMOND, "&dChanging Diamond"));
        item.register(plugin);

        item.setUseableInWorkbench(true);
        CraftItemEvent event = mockCraftingEvent(item.getItem());
        Assertions.assertEquals(Result.DEFAULT, event.getResult());

        item.setUseableInWorkbench(false);
        CraftItemEvent event2 = mockCraftingEvent(item.getItem());
        Assertions.assertEquals(Result.DENY, event2.getResult());
    }

    @Test
    public void testCraftEventWithVanillaItem() {
        VanillaItem item = TestUtilities.mockVanillaItem(plugin, Material.DIAMOND, true);
        item.register(plugin);

        CraftItemEvent event = mockCraftingEvent(item.getItem());
        Assertions.assertEquals(Result.DEFAULT, event.getResult());
    }

    @Test
    public void testPreCraftEventWithoutSlimefunItems() {
        PrepareItemCraftEvent event = mockPreCraftingEvent(new ItemStack(Material.DIAMOND));
        Assertions.assertNotNull(event.getInventory().getResult());
    }

    @Test
    public void testPreCraftEventWithSlimefunItem() {
        SlimefunItem item = TestUtilities.mockSlimefunItem(plugin, "MOCK_DIAMOND2", new CustomItem(Material.DIAMOND, "&cMock Diamond"));
        item.register(plugin);

        PrepareItemCraftEvent event = mockPreCraftingEvent(item.getItem());
        Assertions.assertNull(event.getInventory().getResult());
    }

    @Test
    public void testPreCraftEventWithVanillaItem() {
        VanillaItem item = TestUtilities.mockVanillaItem(plugin, Material.GOLD_INGOT, true);
        item.register(plugin);

        PrepareItemCraftEvent event = mockPreCraftingEvent(item.getItem());
        Assertions.assertNotNull(event.getInventory().getResult());
    }

    @Test
    public void testAnvilWithoutSlimefunItems() {
        InventoryClickEvent event = mockAnvilEvent(new ItemStack(Material.IRON_SWORD));
        Assertions.assertEquals(Result.DEFAULT, event.getResult());
    }

    @Test
    public void testAnvilWithSlimefunItem() {
        SlimefunItem item = TestUtilities.mockSlimefunItem(plugin, "MOCKED_IRON_SWORD", new CustomItem(Material.IRON_SWORD, "&6Mock"));
        item.register(plugin);

        InventoryClickEvent event = mockAnvilEvent(item.getItem());
        Assertions.assertEquals(Result.DENY, event.getResult());
    }

    @Test
    public void testAnvilWithVanillaItem() {
        VanillaItem item = TestUtilities.mockVanillaItem(plugin, Material.IRON_SWORD, true);
        item.register(plugin);

        InventoryClickEvent event = mockAnvilEvent(item.getItem());
        Assertions.assertEquals(Result.DEFAULT, event.getResult());
    }

    @Test
    public void testBrewingWithoutSlimefunItems() {
        InventoryClickEvent event = mockBrewingEvent(new ItemStack(Material.BLAZE_POWDER));
        Assertions.assertEquals(Result.ALLOW, event.getResult());
    }

    @Test
    public void testBrewingWithSlimefunItem() {
        SlimefunItem item = TestUtilities.mockSlimefunItem(plugin, "MOCK_POWDER", new CustomItem(Material.BLAZE_POWDER, "&6Magic Mock Powder"));
        item.register(plugin);

        InventoryClickEvent event = mockBrewingEvent(item.getItem());
        Assertions.assertEquals(Result.DENY, event.getResult());
    }

    @Test
    public void testBrewingithVanillaItem() {
        VanillaItem item = TestUtilities.mockVanillaItem(plugin, Material.BLAZE_POWDER, true);
        item.register(plugin);

        InventoryClickEvent event = mockBrewingEvent(item.getItem());
        Assertions.assertEquals(Result.ALLOW, event.getResult());
    }
}
