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
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
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

    @BeforeEach
    public void beforeEach() {
        server.getPluginManager().clearEvents();
    }

    @Test
    @DisplayName("Test that SlimefunBlockPlaceEvent is fired when a SlimefunItem is placed")
    void testEventIsFired() {
        Player player = new PlayerMock(server, "SomePlayer");
        ItemStack itemStack = slimefunItem.getItem();
        player.getInventory().setItemInMainHand(itemStack);

        World world = server.addSimpleWorld("my_world");
        int x = TestUtilities.randomInt();
        int z = TestUtilities.randomInt();
        Block block = new BlockMock(Material.GREEN_TERRACOTTA, new Location(world, x, 0, z));
        Block blockAgainst = new BlockMock(Material.GRASS_BLOCK, new Location(world, x, 1, z));

        Slimefun.getRegistry().getWorlds().put("my_world", new BlockStorage(world));

        BlockPlaceEvent blockPlaceEvent  = new BlockPlaceEvent(
            block, block.getState(), blockAgainst, itemStack, player, true, EquipmentSlot.HAND
        );

        server.getPluginManager().callEvent(blockPlaceEvent);
        server.getPluginManager().assertEventFired(SlimefunBlockPlaceEvent.class, e -> true);
    }

    @Test
    @DisplayName("Test the getters are set to the right values")
    void testGetters() {
        Player player = new PlayerMock(server, "SomePlayer");
        ItemStack itemStack = slimefunItem.getItem();
        player.getInventory().setItemInMainHand(itemStack);

        World world = server.addSimpleWorld("my_world");
        int x = TestUtilities.randomInt();
        int z = TestUtilities.randomInt();
        Block block = new BlockMock(Material.GREEN_TERRACOTTA, new Location(world, x, 0, z));
        Block blockAgainst = new BlockMock(Material.GRASS_BLOCK, new Location(world, x, 1, z));

        Slimefun.getRegistry().getWorlds().put("my_world", new BlockStorage(world));

        BlockPlaceEvent blockPlaceEvent  = new BlockPlaceEvent(
            block, block.getState(), blockAgainst, itemStack, player, true, EquipmentSlot.HAND
        );

        server.getPluginManager().callEvent(blockPlaceEvent);
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
    @DisplayName("Test that the SlimefunBlockPlaceEvent & BlockPlaceEvent events are cancelled correctly")
    void testIsCancelled() {
        server.getPluginManager().registerEvents(new Listener() {
            @EventHandler
            public void onBlockPlace(SlimefunBlockPlaceEvent event) {
                event.setCancelled(true);
            }
        }, plugin);

        Player player = new PlayerMock(server, "SomePlayer");
        ItemStack itemStack = slimefunItem.getItem();
        player.getInventory().setItemInMainHand(itemStack);

        World world = server.addSimpleWorld("my_world");
        int x = TestUtilities.randomInt();
        int z = TestUtilities.randomInt();
        Block block = new BlockMock(Material.GREEN_TERRACOTTA, new Location(world, x, 0, z));
        Block blockAgainst = new BlockMock(Material.GRASS_BLOCK, new Location(world, x, 1, z));

        Slimefun.getRegistry().getWorlds().put("my_world", new BlockStorage(world));

        BlockPlaceEvent blockPlaceEvent  = new BlockPlaceEvent(
            block, block.getState(), blockAgainst, itemStack, player, true, EquipmentSlot.HAND
        );

        server.getPluginManager().callEvent(blockPlaceEvent);
        server.getPluginManager().assertEventFired(SlimefunBlockPlaceEvent.class, e -> {
            Assertions.assertTrue(e.isCancelled());
            Assertions.assertTrue(blockPlaceEvent.isCancelled());
            return true;
        });
    }

    @Test
    @DisplayName("Test that you cannot place before a SlimefunBlock is fully cleared")
    void testBlockPlacementBeforeFullDeletion() {
        Player player = new PlayerMock(server, "SomePlayer");
        ItemStack itemStack = slimefunItem.getItem();
        player.getInventory().setItemInMainHand(itemStack);

        // Place first block
        World world = server.addSimpleWorld("my_world");
        int x = TestUtilities.randomInt();
        int z = TestUtilities.randomInt();
        Block firstBlock = new BlockMock(Material.GREEN_TERRACOTTA, new Location(world, x, 0, z));
        Block firstBlockAgainst = new BlockMock(Material.GRASS_BLOCK, new Location(world, x, 1, z));

        Slimefun.getRegistry().getWorlds().put("my_world", new BlockStorage(world));

        BlockPlaceEvent firstBlockPlaceEvent  = new BlockPlaceEvent(
                firstBlock, firstBlock.getState(), firstBlockAgainst, itemStack, player, true, EquipmentSlot.HAND
        );

        server.getPluginManager().callEvent(firstBlockPlaceEvent);
        server.getPluginManager().assertEventFired(SlimefunBlockPlaceEvent.class, e -> {
            Assertions.assertFalse(e.isCancelled());
            return true;
        });

        // Break block
        server.getPluginManager().callEvent(new BlockBreakEvent(firstBlock, player));
        server.getPluginManager().assertEventFired(SlimefunBlockBreakEvent.class, e -> true);
        
        // Assert that the block is not fully deleted
        Assertions.assertTrue(Slimefun.getTickerTask().isDeletedSoon(firstBlock.getLocation()));

        // Place second block in the same location
        Block secondBlock = new BlockMock(Material.GREEN_TERRACOTTA, new Location(world, x, 0, z));
        Block secondBlockAgainst = new BlockMock(Material.GRASS_BLOCK, new Location(world, x, 1, z));

        BlockPlaceEvent secondBlockPlaceEvent  = new BlockPlaceEvent(
                secondBlock, secondBlock.getState(), secondBlockAgainst, itemStack, player, true, EquipmentSlot.HAND
        );
        server.getPluginManager().callEvent(secondBlockPlaceEvent);
        Assertions.assertTrue(secondBlockPlaceEvent.isCancelled());
    }
}
