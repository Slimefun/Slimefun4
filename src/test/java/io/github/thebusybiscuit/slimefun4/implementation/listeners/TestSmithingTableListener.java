package io.github.thebusybiscuit.slimefun4.implementation.listeners;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;

import org.apache.commons.lang3.mutable.MutableObject;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.inventory.PrepareSmithingEvent;
import org.bukkit.event.inventory.SmithItemEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.SmithingInventory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import io.github.bakedlibs.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.implementation.items.VanillaItem;
import io.github.thebusybiscuit.slimefun4.implementation.listeners.crafting.SmithingTableListener;
import io.github.thebusybiscuit.slimefun4.test.TestUtilities;

class TestSmithingTableListener {

    private static SmithingTableListener listener;
    private static ServerMock server;

    private static SlimefunItem slimefunIngot;
    private static SlimefunItem usableSlimefunIngot;
    private static SlimefunItem slimefunTool;
    private static VanillaItem vanillaIngot;
    private static VanillaItem vanillaTool;

    @BeforeAll
    public static void load() {
        server = MockBukkit.mock();
        Slimefun plugin = MockBukkit.load(Slimefun.class);
        listener = new SmithingTableListener(plugin);

        slimefunTool = TestUtilities.mockSlimefunItem(plugin, "MOCK_DIAMOND_SWORD", CustomItemStack.create(Material.DIAMOND_SWORD, "&6Mock"));
        slimefunIngot = TestUtilities.mockSlimefunItem(plugin, "MOCK_NETHERITE_INGOT", CustomItemStack.create(Material.NETHERITE_INGOT, "&6Mock"));
        usableSlimefunIngot = TestUtilities.mockSlimefunItem(plugin, "MOCK_NETHERITE_INGOT_USABLE", CustomItemStack.create(Material.NETHERITE_INGOT, "&6Mock"));
        usableSlimefunIngot.setUseableInWorkbench(true);

        vanillaTool = TestUtilities.mockVanillaItem(plugin, Material.DIAMOND_SWORD, true);
        vanillaIngot = TestUtilities.mockVanillaItem(plugin, Material.NETHERITE_INGOT, true);

        slimefunTool.register(plugin);
        slimefunIngot.register(plugin);
        usableSlimefunIngot.register(plugin);
        vanillaTool.register(plugin);
        vanillaIngot.register(plugin);
    }

    @AfterAll
    public static void unload() {
        MockBukkit.unmock();
    }

    private SmithItemEvent mockSmithingEvent(ItemStack tool, ItemStack material) {
        Player player = server.addPlayer();

        SmithingInventory inv = Mockito.mock(SmithingInventory.class);
        // MinecraftVersion#isAtLeast always returns true during unit test, so we use the 1.20 layout here.
        Mockito.when(inv.getContents()).thenReturn(new ItemStack[] { new ItemStack(Material.NETHERITE_UPGRADE_SMITHING_TEMPLATE), tool, material, null });

        InventoryView view = player.openInventory(inv);
        SmithItemEvent event = new SmithItemEvent(view, SlotType.RESULT, 3, ClickType.LEFT, InventoryAction.PICKUP_ONE);

        listener.onSmith(event);
        return event;
    }

    private PrepareSmithingEvent mockPrepareSmithingEvent(ItemStack tool, ItemStack material) {
        Player player = server.addPlayer();

        SmithingInventory inv = Mockito.mock(SmithingInventory.class);
        MutableObject<ItemStack> result = new MutableObject<>(new ItemStack(Material.NETHERITE_PICKAXE));

        Mockito.doAnswer(invocation -> {
            ItemStack argument = invocation.getArgument(0);
            result.setValue(argument);
            return null;
        }).when(inv).setResult(Mockito.any());

        Mockito.when(inv.getResult()).thenAnswer(invocation -> result.getValue());
        // MinecraftVersion#isAtLeast always returns true during unit test, so we use the 1.20 layout here.
        Mockito.when(inv.getContents()).thenReturn(new ItemStack[] { new ItemStack(Material.NETHERITE_UPGRADE_SMITHING_TEMPLATE), tool, material, null });

        InventoryView view = player.openInventory(inv);
        PrepareSmithingEvent event = new PrepareSmithingEvent(view, result.getValue());

        listener.onPrepareSmith(event);
        return event;
    }

    @Test
    @DisplayName("Test that vanilla is unchanged (ItemStack tool x ItemStack material)")
    void testSmithingTableWithItemStacks() {
        SmithItemEvent event = mockSmithingEvent(new ItemStack(Material.DIAMOND_SWORD), new ItemStack(Material.NETHERITE_INGOT));
        Assertions.assertEquals(Result.DEFAULT, event.getResult());
    }

    @Test
    @DisplayName("Test that SlimefunItem material doesn't work (ItemStack tool x SlimefunItem material)")
    void testSmithingTableWithItemStackAndSlimefunItem() {
        SmithItemEvent event = mockSmithingEvent(new ItemStack(Material.DIAMOND_SWORD), slimefunIngot.getItem());
        Assertions.assertEquals(Result.DENY, event.getResult());
    }

    @Test
    @DisplayName("Test that VanillaItem material works (ItemStack tool x VanillaItem material)")
    void testSmithingTableWithItemStackAndVanillaItem() {
        SmithItemEvent event = mockSmithingEvent(new ItemStack(Material.DIAMOND_SWORD), vanillaIngot.getItem());
        Assertions.assertEquals(Result.DEFAULT, event.getResult());
    }

    @Test
    @DisplayName("Test that SlimefunItems can upgrade with vanilla (SlimefunItem tool x ItemStack material)")
    void testSmithingTableWithSlimefunItemAndItemStack() {
        SmithItemEvent event = mockSmithingEvent(slimefunTool.getItem(), new ItemStack(Material.NETHERITE_INGOT));
        Assertions.assertEquals(Result.DEFAULT, event.getResult());
    }

    @Test
    @DisplayName("Test that SlimefunItems can't upgrade with SlimefunItem materials (SlimefunItem tool x SlimefunItem material)")
    void testSmithingTableWithSlimefunItems() {
        SmithItemEvent event = mockSmithingEvent(slimefunTool.getItem(), slimefunIngot.getItem());
        Assertions.assertEquals(Result.DENY, event.getResult());
    }

    @Test
    @DisplayName("Test that SlimefunItems can upgrade with VanillaItems (SlimefunItem tool x VanillaItem material)")
    void testSmithingTableWithSlimefunItemAndVanillaItem() {
        SmithItemEvent event = mockSmithingEvent(slimefunTool.getItem(), vanillaIngot.getItem());
        Assertions.assertEquals(Result.DEFAULT, event.getResult());
    }

    @Test
    @DisplayName("Test that VanillaItems can upgrade with vanilla (VanillaItem tool x ItemStack material)")
    void testSmithingTableWithVanillaItemAndItemStack() {
        SmithItemEvent event = mockSmithingEvent(vanillaTool.getItem(), new ItemStack(Material.NETHERITE_INGOT));
        Assertions.assertEquals(Result.DEFAULT, event.getResult());
    }

    @Test
    @DisplayName("Test that VanillaItems can't upgrade with SlimefunItem materials (VanillaItem tool x SlimefunItem material)")
    void testSmithingTableWithVanillaItemAndSlimefunItem() {
        SmithItemEvent event = mockSmithingEvent(vanillaTool.getItem(), slimefunIngot.getItem());
        Assertions.assertEquals(Result.DENY, event.getResult());
    }

    @Test
    @DisplayName("Test that VanillaItems can upgrade with VanillaItems (VanillaItem tool x VanillaItem material)")
    void testSmithingTableWithVanillaItemAndVanillaItem() {
        SmithItemEvent event = mockSmithingEvent(vanillaTool.getItem(), vanillaIngot.getItem());
        Assertions.assertEquals(Result.DEFAULT, event.getResult());
    }

    @Test
    @DisplayName("Test that ItemStacks can be upgraded with SlimefunItem can-be-used-in-workbenches: true")
    void testCanBeUsedInWorkbenchTrue() {
        Assertions.assertTrue(usableSlimefunIngot.isUseableInWorkbench());
        SmithItemEvent event = mockSmithingEvent(new ItemStack(Material.DIAMOND_SWORD), usableSlimefunIngot.getItem());
        Assertions.assertEquals(Result.DEFAULT, event.getResult());
    }

    @Test
    @DisplayName("Test that vanilla is unchanged (ItemStack tool x ItemStack material)")
    void testPrepareSmithingTableWithItemStacks() {
        PrepareSmithingEvent event = mockPrepareSmithingEvent(new ItemStack(Material.DIAMOND_SWORD), new ItemStack(Material.NETHERITE_INGOT));
        Assertions.assertNotNull(event.getResult());
    }

    @Test
    @DisplayName("Test that SlimefunItem material doesn't work (ItemStack tool x SlimefunItem material)")
    void testPrepareSmithingTableWithItemStackAndSlimefunItem() {
        PrepareSmithingEvent event = mockPrepareSmithingEvent(new ItemStack(Material.DIAMOND_SWORD), slimefunIngot.getItem());
        Assertions.assertNull(event.getResult());
    }

    @Test
    @DisplayName("Test that VanillaItem material works (ItemStack tool x VanillaItem material)")
    void testPrepareSmithingTableWithItemStackAndVanillaItem() {
        PrepareSmithingEvent event = mockPrepareSmithingEvent(new ItemStack(Material.DIAMOND_SWORD), vanillaIngot.getItem());
        Assertions.assertNotNull(event.getResult());
    }

    @Test
    @DisplayName("Test that SlimefunItems can upgrade with vanilla (SlimefunItem tool x ItemStack material)")
    void testPrepareSmithingTableWithSlimefunItemAndItemStack() {
        PrepareSmithingEvent event = mockPrepareSmithingEvent(slimefunTool.getItem(), new ItemStack(Material.NETHERITE_INGOT));
        Assertions.assertNotNull(event.getResult());
    }

    @Test
    @DisplayName("Test that SlimefunItems can't upgrade with SlimefunItem materials (SlimefunItem tool x SlimefunItem material)")
    void testPrepareSmithingTableWithSlimefunItems() {
        PrepareSmithingEvent event = mockPrepareSmithingEvent(slimefunTool.getItem(), slimefunIngot.getItem());
        Assertions.assertNull(event.getResult());
    }

    @Test
    @DisplayName("Test that SlimefunItems can upgrade with VanillaItems (SlimefunItem tool x VanillaItem material)")
    void testPrepareSmithingTableWithSlimefunItemAndVanillaItem() {
        PrepareSmithingEvent event = mockPrepareSmithingEvent(slimefunTool.getItem(), vanillaIngot.getItem());
        Assertions.assertNotNull(event.getResult());
    }

    @Test
    @DisplayName("Test that VanillaItems can upgrade with vanilla (VanillaItem tool x ItemStack material)")
    void testPrepareSmithingTableWithVanillaItemAndItemStack() {
        PrepareSmithingEvent event = mockPrepareSmithingEvent(vanillaTool.getItem(), new ItemStack(Material.NETHERITE_INGOT));
        Assertions.assertNotNull(event.getResult());
    }

    @Test
    @DisplayName("Test that VanillaItems can't upgrade with SlimefunItem materials (VanillaItem tool x SlimefunItem material)")
    void testPrepareSmithingTableWithVanillaItemAndSlimefunItem() {
        PrepareSmithingEvent event = mockPrepareSmithingEvent(vanillaTool.getItem(), slimefunIngot.getItem());
        Assertions.assertNull(event.getResult());
    }

    @Test
    @DisplayName("Test that VanillaItems can upgrade with VanillaItems (VanillaItem tool x VanillaItem material)")
    void testPrepareSmithingTableWithVanillaItemAndVanillaItem() {
        PrepareSmithingEvent event = mockPrepareSmithingEvent(vanillaTool.getItem(), vanillaIngot.getItem());
        Assertions.assertNotNull(event.getResult());
    }

    @Test
    @DisplayName("Test that ItemStacks can be upgraded with SlimefunItem can-be-used-in-workbenches: true")
    void testPrepareCanBeUsedInWorkbenchTrue() {
        PrepareSmithingEvent event = mockPrepareSmithingEvent(new ItemStack(Material.DIAMOND_SWORD), usableSlimefunIngot.getItem());
        Assertions.assertNotNull(event.getResult());
    }

}
