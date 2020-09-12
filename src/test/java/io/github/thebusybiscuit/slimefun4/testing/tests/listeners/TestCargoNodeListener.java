package io.github.thebusybiscuit.slimefun4.testing.tests.listeners;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.implementation.listeners.CargoNodeListener;
import io.github.thebusybiscuit.slimefun4.testing.annotations.SlimefunItemsSource;

class TestCargoNodeListener {

    private static SlimefunPlugin plugin;
    private static CargoNodeListener listener;
    private static ServerMock server;

    @BeforeAll
    public static void load() {
        server = MockBukkit.mock();
        plugin = MockBukkit.load(SlimefunPlugin.class);
        listener = new CargoNodeListener(plugin);
    }

    @AfterAll
    public static void unload() {
        MockBukkit.unmock();
    }

    @ParameterizedTest
    @DisplayName("Test placing Cargo nodes on valid sides of blocks")
    @SlimefunItemsSource(items = { "CARGO_INPUT_NODE", "CARGO_OUTPUT_NODE", "CARGO_OUTPUT_NODE_2" })
    void testSidePlacement(ItemStack item) {
        Player player = server.addPlayer();
        Location l = new Location(player.getWorld(), 190, 50, 400);
        Block b = l.getBlock();
        Block against = b.getRelative(BlockFace.NORTH);

        BlockPlaceEvent event = new BlockPlaceEvent(b, b.getState(), against, item, player, true, EquipmentSlot.HAND);
        listener.onCargoNodePlace(event);
        Assertions.assertFalse(event.isCancelled());
    }

    @ParameterizedTest
    @DisplayName("Test placing Cargo nodes on invalid sides of blocks")
    @SlimefunItemsSource(items = { "CARGO_INPUT_NODE", "CARGO_OUTPUT_NODE", "CARGO_OUTPUT_NODE_2" })
    void testInvalidPlacement(ItemStack item) {
        SlimefunPlugin.getRegistry().setBackwardsCompatible(true);
        Player player = server.addPlayer();
        Location l = new Location(player.getWorld(), 190, 50, 400);
        Block b = l.getBlock();
        Block against = b.getRelative(BlockFace.DOWN);

        BlockPlaceEvent event = new BlockPlaceEvent(b, b.getState(), against, item, player, true, EquipmentSlot.HAND);
        listener.onCargoNodePlace(event);
        Assertions.assertTrue(event.isCancelled());
        SlimefunPlugin.getRegistry().setBackwardsCompatible(false);
    }

    @Test
    @DisplayName("Test non-Cargo node not being affected")
    void testNonCargoNode() {
        SlimefunPlugin.getRegistry().setBackwardsCompatible(true);
        Player player = server.addPlayer();
        Location l = new Location(player.getWorld(), 190, 50, 400);
        Block b = l.getBlock();
        Block against = b.getRelative(BlockFace.DOWN);

        ItemStack item = new ItemStack(Material.PLAYER_HEAD);

        BlockPlaceEvent event = new BlockPlaceEvent(b, b.getState(), against, item, player, true, EquipmentSlot.HAND);
        listener.onCargoNodePlace(event);
        Assertions.assertFalse(event.isCancelled());
        SlimefunPlugin.getRegistry().setBackwardsCompatible(false);
    }

}
