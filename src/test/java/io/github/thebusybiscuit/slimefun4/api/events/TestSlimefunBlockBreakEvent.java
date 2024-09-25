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
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TestSlimefunBlockBreakEvent {

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
    @DisplayName("Test that SlimefunBlockBreakEvent is fired when a SlimefunItem is broken")
    void testEventIsFired() {
        Player player = new PlayerMock(server, "SomePlayer");

        World world = server.addSimpleWorld("my_world");
        Block block = new BlockMock(Material.GREEN_TERRACOTTA, new Location(world, TestUtilities.randomInt(), 100, TestUtilities.randomInt()));

        Slimefun.getRegistry().getWorlds().put("my_world", new BlockStorage(world));
        BlockStorage.addBlockInfo(block, "id", "FOOD_COMPOSTER");

        server.getPluginManager().callEvent(new BlockBreakEvent(block, player));
        server.getPluginManager().assertEventFired(SlimefunBlockBreakEvent.class, e -> true);
    }

    @Test
    @DisplayName("Test the getters are set to the right values")
    void testGetters() {
        Player player = new PlayerMock(server, "SomePlayer");
        ItemStack itemStack = new ItemStack(Material.IRON_PICKAXE);
        player.getInventory().setItemInMainHand(itemStack);

        World world = server.addSimpleWorld("my_world");
        Block block = new BlockMock(Material.GREEN_TERRACOTTA, new Location(world, TestUtilities.randomInt(), 100, TestUtilities.randomInt()));

        Slimefun.getRegistry().getWorlds().put("my_world", new BlockStorage(world));
        BlockStorage.addBlockInfo(block, "id", "FOOD_COMPOSTER");

        server.getPluginManager().callEvent(new BlockBreakEvent(block, player));
        server.getPluginManager().assertEventFired(SlimefunBlockBreakEvent.class, e -> {
            Assertions.assertEquals(block, e.getBlockBroken());
            Assertions.assertEquals(slimefunItem, e.getSlimefunItem());
            Assertions.assertEquals(itemStack, e.getHeldItem());
            Assertions.assertEquals(player, e.getPlayer());
            Assertions.assertFalse(e.isCancelled());
            return true;
        });
    }

    @Test
    @DisplayName("Test that the SlimefunBlockBreakEvent & BlockBreakEvent events are cancelled correctly")
    void testIsCancelled() {
        server.getPluginManager().registerEvents(new Listener() {
            @EventHandler
            public void onBlockBreak(SlimefunBlockBreakEvent event) {
                event.setCancelled(true);
            }
        }, plugin);

        Player player = new PlayerMock(server, "SomePlayer");
        ItemStack itemStack = new ItemStack(Material.IRON_PICKAXE);
        player.getInventory().setItemInMainHand(itemStack);

        World world = server.addSimpleWorld("my_world");
        Block block = new BlockMock(Material.GREEN_TERRACOTTA, new Location(world, TestUtilities.randomInt(), 100, TestUtilities.randomInt()));

        Slimefun.getRegistry().getWorlds().put("my_world", new BlockStorage(world));
        BlockStorage.addBlockInfo(block, "id", "FOOD_COMPOSTER");

        BlockBreakEvent blockBreakEvent = new BlockBreakEvent(block, player);
        server.getPluginManager().callEvent(blockBreakEvent);
        server.getPluginManager().assertEventFired(SlimefunBlockBreakEvent.class, e -> {
            Assertions.assertTrue(e.isCancelled());
            Assertions.assertTrue(blockBreakEvent.isCancelled());
            return true;
        });
    }

    @Test
    @DisplayName("Test that breaking a Slimefun block gets queued for deletion")
    void testBlockBreaksGetQueuedForDeletion() {
        Player player = new PlayerMock(server, "SomePlayer");
        ItemStack itemStack = new ItemStack(Material.IRON_PICKAXE);
        player.getInventory().setItemInMainHand(itemStack);

        World world = server.addSimpleWorld("my_world");
        Block block = new BlockMock(Material.GREEN_TERRACOTTA, new Location(world, TestUtilities.randomInt(), 100, TestUtilities.randomInt()));

        Slimefun.getRegistry().getWorlds().put("my_world", new BlockStorage(world));
        BlockStorage.addBlockInfo(block, "id", "FOOD_COMPOSTER");

        BlockBreakEvent blockBreakEvent = new BlockBreakEvent(block, player);
        server.getPluginManager().callEvent(blockBreakEvent);
        server.getPluginManager().assertEventFired(SlimefunBlockBreakEvent.class, e -> true);

        Assertions.assertTrue(Slimefun.getTickerTask().isDeletedSoon(block.getLocation()));
    }
}
