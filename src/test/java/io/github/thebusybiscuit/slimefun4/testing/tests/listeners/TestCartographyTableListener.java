package io.github.thebusybiscuit.slimefun4.testing.tests.listeners;

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
import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.implementation.items.VanillaItem;
import io.github.thebusybiscuit.slimefun4.implementation.listeners.crafting.CartographyTableListener;
import io.github.thebusybiscuit.slimefun4.testing.TestUtilities;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;

public class TestCartographyTableListener {

    private static SlimefunPlugin plugin;
    private static CartographyTableListener listener;
    private static ServerMock server;

    @BeforeAll
    public static void load() {
        server = MockBukkit.mock();
        plugin = MockBukkit.load(SlimefunPlugin.class);
        listener = new CartographyTableListener(plugin);
    }

    @AfterAll
    public static void unload() {
        MockBukkit.unmock();
    }

    private InventoryClickEvent mockCartographyTableEvent(ItemStack item) {
        Player player = server.addPlayer();
        Inventory inv = TestUtilities.mockInventory(InventoryType.CARTOGRAPHY, new ItemStack(Material.FILLED_MAP), item, new ItemStack(Material.FILLED_MAP));
        InventoryView view = player.openInventory(inv);
        InventoryClickEvent event = new InventoryClickEvent(view, SlotType.CONTAINER, 2, ClickType.LEFT, InventoryAction.PICKUP_ONE);

        listener.onCartographyTable(event);
        return event;
    }

    @Test
    public void testCartographyTableWithSlimefunItem() {
        SlimefunItem item = TestUtilities.mockSlimefunItem(plugin, "MOCKED_PAPER", new CustomItem(Material.PAPER, "&6Mock"));
        item.register(plugin);

        InventoryClickEvent event = mockCartographyTableEvent(item.getItem());
        Assertions.assertEquals(Result.DENY, event.getResult());
    }

    @Test
    public void testCartographyTableWithVanillaItem() {
        VanillaItem item = TestUtilities.mockVanillaItem(plugin, Material.PAPER, true);
        item.register(plugin);

        InventoryClickEvent event = mockCartographyTableEvent(item.getItem());
        Assertions.assertEquals(Result.DEFAULT, event.getResult());
    }

}
