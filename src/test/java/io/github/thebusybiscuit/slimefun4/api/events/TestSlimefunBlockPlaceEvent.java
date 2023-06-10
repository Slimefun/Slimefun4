package io.github.thebusybiscuit.slimefun4.api.events;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.block.BlockMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.implementation.listeners.BlockListener;
import io.github.thebusybiscuit.slimefun4.test.TestUtilities;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TestSlimefunBlockPlaceEvent {
    private static ServerMock server;
    private static Slimefun plugin;
    private static SlimefunItem slimefunItem;
    @BeforeAll
    public static void load() {
        server = MockBukkit.mock();
        plugin = MockBukkit.load(Slimefun.class);
        new BlockListener(plugin);
        slimefunItem = TestUtilities.mockSlimefunItem(plugin, "FOOD_COMPOSTER", new ItemStack(Material.GREEN_TERRACOTTA));
        slimefunItem.register(plugin);
    }

    @AfterAll
    public static void unload() {
        MockBukkit.unmock();
    }

    @Test
    @DisplayName("Test firing Slimefun Block place Event")
    void testEventIsFired() throws InterruptedException {
        server.getPluginManager().clearEvents();

        Player player = new PlayerMock(server, "SomePlayer");
        ItemStack itemStack = new ItemStack(Material.IRON_PICKAXE);
        World world = server.addSimpleWorld("my_world");
        ItemStack vanillaItem = new ItemStack(Material.GREEN_TERRACOTTA);
        Block block = new BlockMock(Material.GREEN_TERRACOTTA, new Location(world, 1, 1, 1));
        Slimefun.getRegistry().getWorlds().put("my_world", new BlockStorage(world));
        BlockStorage.addBlockInfo(block, "id", "FOOD_COMPOSTER");

        server.getPluginManager().callEvent(new SlimefunBlockPlaceEvent(player, vanillaItem, block, slimefunItem));
        server.getPluginManager().assertEventFired(SlimefunBlockPlaceEvent.class, e -> true);
    }

    @Test
    @DisplayName("Test getting the getters from the event")
    void testGetters() {
        server.getPluginManager().clearEvents();

        Player player = new PlayerMock(server, "SomePlayer");
        ItemStack itemStack = new ItemStack(Material.GREEN_TERRACOTTA);
        player.getInventory().setItemInMainHand(itemStack);
        World world = server.addSimpleWorld("my_world");
        Block block = new BlockMock(Material.GREEN_TERRACOTTA, new Location(world, 1, 1, 1));
        Slimefun.getRegistry().getWorlds().put("my_world", new BlockStorage(world));
        BlockStorage.addBlockInfo(block, "id", "FOOD_COMPOSTER");

        server.getPluginManager().callEvent(new SlimefunBlockPlaceEvent(player, itemStack, block, slimefunItem));
        server.getPluginManager().assertEventFired(SlimefunBlockPlaceEvent.class, e -> {
            Assertions.assertEquals(block, e.getBlockPlaced());
            Assertions.assertEquals(slimefunItem, e.getSlimefunItem());
            Assertions.assertEquals(itemStack, e.getItemStack());
            Assertions.assertEquals(player, e.getPlayer());
            Assertions.assertFalse(e.isCancelled());
            return true;
        });
    }

    @Test
    @DisplayName("Test the event cancelling")
    void testIsCancelled() {
        server.getPluginManager().clearEvents();
        server.getPluginManager().registerEvents(new Listener() {
            @EventHandler
            public void onBlockPlace(SlimefunBlockPlaceEvent event) {
                event.setCancelled(true);
            }
        }, plugin);

        Player player = new PlayerMock(server, "SomePlayer");
        ItemStack itemStack = new ItemStack(Material.GREEN_TERRACOTTA);
        player.getInventory().setItemInMainHand(itemStack);
        World world = server.addSimpleWorld("my_world");
        Block block = new BlockMock(Material.GREEN_TERRACOTTA, new Location(world, 1, 1, 1));
        Slimefun.getRegistry().getWorlds().put("my_world", new BlockStorage(world));
        BlockStorage.addBlockInfo(block, "id", "FOOD_COMPOSTER");

        server.getPluginManager().callEvent(new SlimefunBlockPlaceEvent(player, itemStack, block, slimefunItem));
        server.getPluginManager().assertEventFired(SlimefunBlockPlaceEvent.class, e -> {
            Assertions.assertTrue(e.isCancelled());
            return true;
        });
    }
}
