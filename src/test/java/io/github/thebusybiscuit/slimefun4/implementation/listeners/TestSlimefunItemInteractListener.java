package io.github.thebusybiscuit.slimefun4.implementation.listeners;

import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import io.github.bakedlibs.dough.common.ChatColors;
import io.github.thebusybiscuit.slimefun4.api.events.PlayerRightClickEvent;
import io.github.thebusybiscuit.slimefun4.api.events.SlimefunBlockBreakEvent;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.implementation.items.electric.EnergyConnector;
import io.github.thebusybiscuit.slimefun4.implementation.items.electric.machines.ElectricFurnace;
import io.github.thebusybiscuit.slimefun4.implementation.items.magical.staves.WindStaff;
import io.github.thebusybiscuit.slimefun4.test.TestUtilities;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;

class TestSlimefunItemInteractListener {

    private static ServerMock server;
    private static Slimefun plugin;
    // Block with inventory
    private static SlimefunItem electricFurnace;
    // Interactable block
    private static SlimefunItem energyConnector;
    // Interactable item
    private static SlimefunItem windStaff;

    @BeforeAll
    public static void load() {
        server = MockBukkit.mock();
        plugin = MockBukkit.load(Slimefun.class);

        // Register block listener (for place + break) and our interact listener for inventory handling
        new BlockListener(plugin);
        new SlimefunItemInteractListener(plugin);

        // Enable tickers so the electric furnace can be registered
        Slimefun.getCfg().setValue("URID.enable-tickers", true);

        ItemGroup testGroup = TestUtilities.getItemGroup(plugin, "test");

        electricFurnace = new ElectricFurnace(testGroup, SlimefunItems.ELECTRIC_FURNACE, RecipeType.NULL, new ItemStack[]{})
            .setCapacity(100)
            .setEnergyConsumption(10)
            .setProcessingSpeed(1);
        electricFurnace.register(plugin);

        energyConnector = new EnergyConnector(testGroup, SlimefunItems.ENERGY_CONNECTOR, RecipeType.NULL, new ItemStack[9], null);
        energyConnector.register(plugin);

        windStaff = new WindStaff(testGroup, SlimefunItems.STAFF_WIND, RecipeType.NULL, new ItemStack[9]);
        windStaff.register(plugin);
    }

    @AfterAll
    public static void unload() {
        MockBukkit.unmock();
    }

    @AfterEach
    public void afterEach() {
        server.getPluginManager().clearEvents();
    }

    // Test for dupe bug - issue #4087
    @Test
    void testCannotOpenInvOfBrokenBlock() {
        // Place down an electric furnace
        Player player = server.addPlayer();
        ItemStack itemStack = electricFurnace.getItem();
        player.getInventory().setItemInMainHand(itemStack);

        // Create a world and place the block
        World world = TestUtilities.createWorld(server);
        Block block = TestUtilities.placeSlimefunBlock(server, itemStack, world, player);

        // Right click on the block
        PlayerInteractEvent playerInteractEvent = new PlayerInteractEvent(
            player, Action.RIGHT_CLICK_BLOCK, itemStack, block, BlockFace.UP, EquipmentSlot.HAND
        );

        server.getPluginManager().callEvent(playerInteractEvent);
        server.getPluginManager().assertEventFired(PlayerInteractEvent.class, e -> {
            // We cancel the event on inventory open
            Assertions.assertSame(e.useInteractedBlock(), Result.DENY);
            return true;
        });

        // Assert our right click event fired and the block usage was not denied
        server.getPluginManager().assertEventFired(PlayerRightClickEvent.class, e -> {
            Assertions.assertNotSame(e.useBlock(), Result.DENY);
            return true;
        });

        // Assert we do have an inventory which would be opened
        // TODO: Create an event for open inventory so this isn't guess work
        Assertions.assertTrue(BlockMenuPreset.isInventory(electricFurnace.getId()));
        Assertions.assertTrue(BlockStorage.getStorage(block.getWorld()).hasInventory(block.getLocation()));
        // TODO(future): Check viewers - MockBukkit does not implement this today

        // Break the block
        BlockBreakEvent blockBreakEvent = new BlockBreakEvent(block, player);
        server.getPluginManager().callEvent(blockBreakEvent);
        server.getPluginManager().assertEventFired(SlimefunBlockBreakEvent.class, e -> {
            Assertions.assertEquals(electricFurnace.getId(), e.getSlimefunItem().getId());
            return true;
        });

        // Assert the block is queued for removal
        Assertions.assertTrue(Slimefun.getTickerTask().isDeletedSoon(block.getLocation()));

        // Clear event queue since we'll be running duplicate events
        server.getPluginManager().clearEvents();

         // Right click on the block again now that it's broken
        PlayerInteractEvent secondPlayerInteractEvent = new PlayerInteractEvent(
            player, Action.RIGHT_CLICK_BLOCK, itemStack, block, BlockFace.UP, EquipmentSlot.HAND
        );

        server.getPluginManager().callEvent(secondPlayerInteractEvent);
        server.getPluginManager().assertEventFired(PlayerInteractEvent.class, e -> {
            // We cancelled the event due to the block being removed
            Assertions.assertSame(e.useInteractedBlock(), Result.DENY);
            return true;
        });

        // Assert our right click event was not fired due to the block being broken
        Assertions.assertThrows(
            AssertionError.class,
            () -> server.getPluginManager().assertEventFired(PlayerRightClickEvent.class, e -> true)
        );
    }

    @Test
    void testRightClickItem() {
        Player player = server.addPlayer();
        ItemStack itemStack = windStaff.getItem();
        player.getInventory().setItemInMainHand(itemStack);

        // Assert player is at full food level (wind staff reduces food level on usage)
        Assertions.assertEquals(20, player.getFoodLevel()); 

        // Right click the air
        PlayerInteractEvent playerInteractEvent = new PlayerInteractEvent(
            player, Action.RIGHT_CLICK_AIR, itemStack, null, BlockFace.UP, EquipmentSlot.HAND
        );

        server.getPluginManager().callEvent(playerInteractEvent);
        server.getPluginManager().assertEventFired(PlayerInteractEvent.class, e -> {
            // Assert our interaction was not cancelled
            Assertions.assertNotSame(e.useItemInHand(), Result.DENY);
            return true;
        });

        // Assert our right click event fired and the item usage was not denied
        server.getPluginManager().assertEventFired(PlayerRightClickEvent.class, e -> {
            Assertions.assertNotSame(e.useItem(), Result.DENY);
            return true;
        });

        // Assert our food level is now 18
        Assertions.assertEquals(18, player.getFoodLevel());
    }

    @Test
    void testRightClickInteractableBlock() {
        // Place down an energy connector
        PlayerMock player = server.addPlayer();
        ItemStack itemStack = energyConnector.getItem();
        player.getInventory().setItemInMainHand(itemStack);

        // Create a world and place the block
        World world = TestUtilities.createWorld(server);
        Block block = TestUtilities.placeSlimefunBlock(server, itemStack, world, player);

        // Right click on the block
        PlayerInteractEvent playerInteractEvent = new PlayerInteractEvent(
            player, Action.RIGHT_CLICK_BLOCK, itemStack, block, BlockFace.UP, EquipmentSlot.HAND
        );

        server.getPluginManager().callEvent(playerInteractEvent);
        server.getPluginManager().assertEventFired(PlayerInteractEvent.class, e -> {
            // Allow interaction of the block
            Assertions.assertSame(e.useInteractedBlock(), Result.ALLOW);
            return true;
        });

        // Assert our right click event fired and the block usage was not denied
        server.getPluginManager().assertEventFired(PlayerRightClickEvent.class, e -> {
            Assertions.assertNotSame(e.useBlock(), Result.DENY);
            return true;
        });

        // Assert the message our energy connector sends
        Assertions.assertEquals(ChatColors.color("&7Connected: " + "&4\u2718"), player.nextMessage());
    }
}
