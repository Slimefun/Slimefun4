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

import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.implementation.items.VanillaItem;
import io.github.thebusybiscuit.slimefun4.implementation.listeners.crafting.AnvilListener;
import io.github.thebusybiscuit.slimefun4.test.TestUtilities;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;

public class TestAnvilListener {

    private static SlimefunPlugin plugin;
    private static AnvilListener listener;
    private static ServerMock server;

    @BeforeAll
    public static void load() {
        server = MockBukkit.mock();
        plugin = MockBukkit.load(SlimefunPlugin.class);
        listener = new AnvilListener(plugin);
    }

    @AfterAll
    public static void unload() {
        MockBukkit.unmock();
    }

    private InventoryClickEvent mockAnvilEvent(ItemStack item) {
        Player player = server.addPlayer();
        Inventory inv = TestUtilities.mockInventory(InventoryType.ANVIL, item, null, new ItemStack(Material.IRON_CHESTPLATE));
        InventoryView view = player.openInventory(inv);
        InventoryClickEvent event = new InventoryClickEvent(view, SlotType.CONTAINER, 2, ClickType.LEFT, InventoryAction.PICKUP_ONE);

        listener.onAnvil(event);
        return event;
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

}
