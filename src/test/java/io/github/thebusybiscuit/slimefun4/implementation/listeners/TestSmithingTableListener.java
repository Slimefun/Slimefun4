package io.github.thebusybiscuit.slimefun4.implementation.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.github.bakedlibs.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.implementation.items.VanillaItem;
import io.github.thebusybiscuit.slimefun4.implementation.listeners.crafting.SmithingTableListener;
import io.github.thebusybiscuit.slimefun4.test.TestUtilities;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;

class TestSmithingTableListener {

    private static SmithingTableListener listener;
    private static ServerMock server;

    private static SlimefunItem slimefunIngot;
    private static SlimefunItem slimefunTool;
    private static VanillaItem vanillaIngot;
    private static VanillaItem vanillaTool;

    @BeforeAll
    public static void load() {
        server = MockBukkit.mock();
        Slimefun plugin = MockBukkit.load(Slimefun.class);
        listener = new SmithingTableListener(plugin);

        slimefunTool = TestUtilities.mockSlimefunItem(plugin, "MOCK_DIAMOND_SWORD", new CustomItemStack(Material.DIAMOND_SWORD, "&6Mock"));
        slimefunIngot = TestUtilities.mockSlimefunItem(plugin, "MOCK_NETHERITE_INGOT", new CustomItemStack(Material.NETHERITE_INGOT, "&6Mock"));
        vanillaTool = TestUtilities.mockVanillaItem(plugin, Material.DIAMOND_SWORD, true);
        vanillaIngot = TestUtilities.mockVanillaItem(plugin, Material.NETHERITE_INGOT, true);

        slimefunTool.register(plugin);
        slimefunIngot.register(plugin);
        vanillaTool.register(plugin);
        vanillaIngot.register(plugin);
    }

    @AfterAll
    public static void unload() {
        MockBukkit.unmock();
    }

    private InventoryClickEvent mockSmithingEvent(ItemStack tool, ItemStack material) {
        Player player = server.addPlayer();
        Inventory inv = TestUtilities.mockInventory(InventoryType.SMITHING, tool, material, null);
        InventoryView view = player.openInventory(inv);
        InventoryClickEvent event = new InventoryClickEvent(view, SlotType.CONTAINER, 2, ClickType.LEFT, InventoryAction.PICKUP_ONE);

        listener.onSmith(event);
        return event;
    }

    @Test
    @DisplayName("Test that vanilla is unchanged (ItemStack tool x ItemStack material)")
    void testSmithingTableWithItemStacks() {
        InventoryClickEvent event = mockSmithingEvent(new ItemStack(Material.DIAMOND_SWORD), new ItemStack(Material.NETHERITE_INGOT));
        Assertions.assertEquals(Result.DEFAULT, event.getResult());
    }

    @Test
    @DisplayName("Test that SlimefunItem material doesn't work (ItemStack tool x SlimefunItem material)")
    void testSmithingTableWithItemStackAndSlimefunItem() {
        InventoryClickEvent event = mockSmithingEvent(new ItemStack(Material.DIAMOND_SWORD), slimefunIngot.getItem());
        Assertions.assertEquals(Result.DENY, event.getResult());
    }

    @Test
    @DisplayName("Test that VanillaItem material works (ItemStack tool x VanillaItem material)")
    void testSmithingTableWithItemStackAndVanillaItem() {
        InventoryClickEvent event = mockSmithingEvent(new ItemStack(Material.DIAMOND_SWORD), vanillaIngot.getItem());
        Assertions.assertEquals(Result.DEFAULT, event.getResult());
    }

    @Test
    @DisplayName("Test that SlimefunItems can upgrade with vanilla (SlimefunItem tool x ItemStack material)")
    void testSmithingTableWithSlimefunItemAndItemStack() {
        InventoryClickEvent event = mockSmithingEvent(slimefunTool.getItem(), new ItemStack(Material.NETHERITE_INGOT));
        Assertions.assertEquals(Result.DEFAULT, event.getResult());
    }

    @Test
    @DisplayName("Test that SlimefunItems can't upgrade with SlimefunItem materials (SlimefunItem tool x SlimefunItem material)")
    void testSmithingTableWithSlimefunItems() {
        InventoryClickEvent event = mockSmithingEvent(slimefunTool.getItem(), slimefunIngot.getItem());
        Assertions.assertEquals(Result.DENY, event.getResult());
    }

    @Test
    @DisplayName("Test that SlimefunItems can upgrade with VanillaItems (SlimefunItem tool x VanillaItem material)")
    void testSmithingTableWithSlimefunItemAndVanillaItem() {
        InventoryClickEvent event = mockSmithingEvent(slimefunTool.getItem(), vanillaIngot.getItem());
        Assertions.assertEquals(Result.DEFAULT, event.getResult());
    }

    @Test
    @DisplayName("Test that SlimefunItems can upgrade with vanilla (SlimefunItem tool x ItemStack material)")
    void testSmithingTableWithVanillaItemAndItemStack() {
        InventoryClickEvent event = mockSmithingEvent(vanillaTool.getItem(), new ItemStack(Material.NETHERITE_INGOT));
        Assertions.assertEquals(Result.DEFAULT, event.getResult());
    }

    @Test
    @DisplayName("Test that SlimefunItems can't upgrade with SlimefunItem materials (SlimefunItem tool x SlimefunItem material)")
    void testSmithingTableWithVanillaItemAndSlimefunItem() {
        InventoryClickEvent event = mockSmithingEvent(vanillaTool.getItem(), slimefunIngot.getItem());
        Assertions.assertEquals(Result.DENY, event.getResult());
    }

    @Test
    @DisplayName("Test that SlimefunItems can upgrade with VanillaItems (SlimefunItem tool x VanillaItem material)")
    void testSmithingTableWithVanillaItemAndVanillaItem() {
        InventoryClickEvent event = mockSmithingEvent(vanillaTool.getItem(), vanillaIngot.getItem());
        Assertions.assertEquals(Result.DEFAULT, event.getResult());
    }

}
