package io.github.thebusybiscuit.slimefun4.utils.blockpattern;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.WorldMock;
import be.seeseemelk.mockbukkit.block.BlockMock;

class TestTShapedBlockPattern {

    @BeforeAll
    public static void load() {
        MockBukkit.mock();
        MockBukkit.load(Slimefun.class);
    }

    @AfterAll
    public static void unload() {
        MockBukkit.unmock();
    }

    @Test
    @Order(0)
    @DisplayName("Test matching blocks by their material")
    void testMatches() {
        Material targetMaterial = Material.IRON_BLOCK;
        Collection<Block> validBlocks = Collections.nCopies(3, new BlockMock(targetMaterial));
        Assertions.assertTrue(TShapedBlockPattern.allBlocksMatchMaterial(targetMaterial, validBlocks));
        Collection<Block> invalidBlocks = List.of(new BlockMock(targetMaterial), new BlockMock(Material.AIR));
        Assertions.assertFalse(TShapedBlockPattern.allBlocksMatchMaterial(targetMaterial, invalidBlocks));
    }

    @Test
    @DisplayName("Test that getLineNorthSouth returns the correct blocks")
    @Order(1)
    void testGetLineNorthSouth() {
        WorldMock world = new WorldMock();
        Block center = new BlockMock(new Location(world, 0, 0, 0));
        Block north = center.getRelative(BlockFace.NORTH);
        Block south = center.getRelative(BlockFace.SOUTH);
        Collection<Block> line = TShapedBlockPattern.getLineNorthSouth(center);
        Assertions.assertTrue(line.contains(north));
        Assertions.assertTrue(line.contains(south));
    }

    @Test
    @DisplayName("Test that getLineEastWest returns the correct blocks")
    @Order(1)
    void testGetLineEastWest() {
        WorldMock world = new WorldMock();
        Block center = new BlockMock(new Location(world, 0, 0, 0));
        Block north = center.getRelative(BlockFace.EAST);
        Block south = center.getRelative(BlockFace.WEST);
        Collection<Block> line = TShapedBlockPattern.getLineEastWest(center);
        Assertions.assertTrue(line.contains(north));
        Assertions.assertTrue(line.contains(south));
    }

    @Test
    @DisplayName("Test that getTShapeNorthSouth returns the correct blocks")
    @Order(2)
    void testGetTShapeNorthSouth() {
        WorldMock world = new WorldMock();
        Block center = world.getBlockAt(0, 0, 0);
        Block centerUpperLayer = center.getRelative(BlockFace.UP);
        Collection<Block> line = TShapedBlockPattern.getLineNorthSouth(centerUpperLayer);
        Collection<Block> tShape = new ArrayList<>(line);
        tShape.add(center);
        Assertions.assertEquals(tShape, TShapedBlockPattern.getTShapeNorthSouth(center.getLocation()));
    }

    @Test
    @DisplayName("Test that getTShapeEastWest returns the correct blocks")
    @Order(2)
    void testGetTShapeEastWest() {
        WorldMock world = new WorldMock();
        Block center = world.getBlockAt(0, 0, 0);
        Block centerUpperLayer = center.getRelative(BlockFace.UP);
        Collection<Block> line = TShapedBlockPattern.getLineEastWest(centerUpperLayer);
        Collection<Block> tShape = new ArrayList<>(line);
        tShape.add(center);
        Assertions.assertEquals(tShape, TShapedBlockPattern.getTShapeEastWest(center.getLocation()));
    }

    @Test
    @DisplayName("Test that getMatchingBlocks finds blocks with the correct material which are in a T-shape")
    @Order(3)
    void testGetMatchingBlocks() {
        WorldMock world = new WorldMock();
        Location base = new Location(world, 0, 0, 0);
        Material targetMaterial = Material.IRON_BLOCK;

        Assertions.assertTrue(TShapedBlockPattern.getMatchingBlocks(targetMaterial, base).isEmpty());
        // test blocks on the east-west direction
        Collection<Block> tShapeEastWest = TShapedBlockPattern.getTShapeEastWest(base);
        tShapeEastWest.forEach(block -> block.setType(targetMaterial));

        Assertions.assertFalse(TShapedBlockPattern.getMatchingBlocks(targetMaterial, base).isEmpty());
        tShapeEastWest.forEach(block -> block.setType(Material.AIR));

        // test blocks on the north-south direction
        Collection<Block> tShapeNorthSouth = TShapedBlockPattern.getTShapeNorthSouth(base);
        tShapeNorthSouth.forEach(block -> block.setType(targetMaterial));
        Assertions.assertFalse(TShapedBlockPattern.getMatchingBlocks(targetMaterial, base).isEmpty());
    }

    @Test
    @DisplayName("Test that getMatchingBlocks prefers T-shapes on the east-west direction")
    @Order(3)
    void testGetMatchingBlocks_PrefersEastWest() {
        WorldMock world = new WorldMock();
        Location base = new Location(world, 0, 0, 0);
        Collection<Block> tShapeEastWest = TShapedBlockPattern.getTShapeEastWest(base);
        Material targetMaterial = Material.IRON_BLOCK;
        tShapeEastWest.forEach(block -> block.setType(targetMaterial));
        Collection<Block> tShapeNorthSouth = TShapedBlockPattern.getTShapeNorthSouth(base);
        tShapeNorthSouth.forEach(block -> block.setType(targetMaterial));
        Collection<Block> matchingBlocks = TShapedBlockPattern.getMatchingBlocks(targetMaterial, base);
        Assertions.assertEquals(tShapeEastWest, matchingBlocks);
    }

}
