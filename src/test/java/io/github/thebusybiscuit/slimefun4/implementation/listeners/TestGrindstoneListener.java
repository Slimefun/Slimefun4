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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import io.github.thebusybiscuit.slimefun4.core.guide.SlimefunGuide;
import io.github.thebusybiscuit.slimefun4.core.guide.SlimefunGuideMode;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.implementation.items.VanillaItem;
import io.github.thebusybiscuit.slimefun4.implementation.listeners.crafting.GrindstoneListener;
import io.github.thebusybiscuit.slimefun4.test.TestUtilities;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;

public class TestGrindstoneListener {

    private static SlimefunPlugin plugin;
    private static GrindstoneListener listener;
    private static ServerMock server;

    @BeforeAll
    public static void load() {
        server = MockBukkit.mock();
        plugin = MockBukkit.load(SlimefunPlugin.class);
        listener = new GrindstoneListener(plugin);
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
    @EnumSource(SlimefunGuideMode.class)
    public void testGrindStoneWithSlimefunGuide(SlimefunGuideMode layout) {
        InventoryClickEvent event = mockGrindStoneEvent(SlimefunGuide.getItem(layout));
        Assertions.assertEquals(Result.DENY, event.getResult());
    }

}
