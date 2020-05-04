package io.github.thebusybiscuit.slimefun4.tests.listeners;

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

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.inventory.meta.EnchantedBookMetaMock;
import io.github.thebusybiscuit.slimefun4.implementation.listeners.VanillaMachinesListener;
import io.github.thebusybiscuit.slimefun4.mocks.SlimefunMocks;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;

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

    @Test
    public void testGrindStoneWithoutSlimefunItems() {
        Player player = server.addPlayer();
        Inventory inv = SlimefunMocks.mockInventory(InventoryType.GRINDSTONE, new ItemStack(Material.ENCHANTED_BOOK), null);
        InventoryView view = player.openInventory(inv);
        InventoryClickEvent event = new InventoryClickEvent(view, SlotType.CONTAINER, 2, ClickType.LEFT, InventoryAction.PICKUP_ONE);
        listener.onGrindstone(event);

        Assertions.assertEquals(Result.DEFAULT, event.getResult());
    }

}
