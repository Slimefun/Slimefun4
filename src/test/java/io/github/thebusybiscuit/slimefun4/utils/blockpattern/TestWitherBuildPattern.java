package io.github.thebusybiscuit.slimefun4.utils.blockpattern;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.WorldMock;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;

class TestWitherBuildPattern {

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
    @DisplayName("Test that getMatchingBlocks finds blocks with the correct material and shape to make a wither ")
    void testGetMatchingBlocks() {
        WorldMock world = new WorldMock();
        Block base = world.getBlockAt(0, 0, 0);
        Block centerMiddleLayer = base.getRelative(BlockFace.UP);
        Block centerUpperLayer = base.getRelative(BlockFace.UP, 2);

        Assertions.assertTrue(WitherBuildPattern.getMatchingBlocks(base.getLocation()).isEmpty());
        base.setType(Material.SOUL_SAND);

        Collection<Block> soulSandLineEastWest = TShapedBlockPattern.getLineEastWest(centerMiddleLayer);
        Collection<Block> witherHeadsLineEastWest = TShapedBlockPattern.getLineEastWest(centerUpperLayer);
        soulSandLineEastWest.forEach(block -> block.setType(Material.SOUL_SAND));
        // Test that missing wither skeleton skulls is checked
        Assertions.assertTrue(WitherBuildPattern.getMatchingBlocks(base.getLocation()).isEmpty());

        witherHeadsLineEastWest.forEach(block -> block.setType(Material.WITHER_SKELETON_SKULL));
        Assertions.assertFalse(WitherBuildPattern.getMatchingBlocks(base.getLocation()).isEmpty());

        soulSandLineEastWest.forEach(block -> block.setType(Material.AIR));
        witherHeadsLineEastWest.forEach(block -> block.setType(Material.AIR));
        Collection<Block> soulSandLineNorthSouth = TShapedBlockPattern.getLineNorthSouth(centerMiddleLayer);
        Collection<Block> witherHeadsLineNorthSouth = TShapedBlockPattern.getLineNorthSouth(centerUpperLayer);
        soulSandLineNorthSouth.forEach(block -> block.setType(Material.SOUL_SAND));
        // Test that missing wither skeleton skulls is checked
        Assertions.assertTrue(WitherBuildPattern.getMatchingBlocks(base.getLocation()).isEmpty());

        witherHeadsLineNorthSouth.forEach(block -> block.setType(Material.WITHER_SKELETON_SKULL));
        Assertions.assertFalse(WitherBuildPattern.getMatchingBlocks(base.getLocation()).isEmpty());
    }

    @Test
    @DisplayName("Test that getMatchingBlocks prefers eligible withers on the east-west direction")
    void testGetMatchingBlocks_PrefersEastWest() {
        WorldMock world = new WorldMock();
        Block base = world.getBlockAt(0, 0, 0);
        Block centerMiddleLayer = base.getRelative(BlockFace.UP);
        Block centerUpperLayer = base.getRelative(BlockFace.UP, 2);

        Assertions.assertTrue(WitherBuildPattern.getMatchingBlocks(base.getLocation()).isEmpty());
        base.setType(Material.SOUL_SAND);

        Collection<Block> soulSandLineEastWest = TShapedBlockPattern.getLineEastWest(centerMiddleLayer);
        Collection<Block> witherHeadsLineEastWest = TShapedBlockPattern.getLineEastWest(centerUpperLayer);
        soulSandLineEastWest.forEach(block -> block.setType(Material.SOUL_SAND));
        witherHeadsLineEastWest.forEach(block -> block.setType(Material.WITHER_SKELETON_SKULL));

        Collection<Block> soulSandLineNorthSouth = TShapedBlockPattern.getLineNorthSouth(centerMiddleLayer);
        Collection<Block> witherHeadsLineNorthSouth = TShapedBlockPattern.getLineNorthSouth(centerUpperLayer);
        soulSandLineNorthSouth.forEach(block -> block.setType(Material.SOUL_SAND));
        witherHeadsLineNorthSouth.forEach(block -> block.setType(Material.WITHER_SKELETON_SKULL));

        Collection<Block> expected = new ArrayList<>(soulSandLineEastWest);
        expected.addAll(witherHeadsLineEastWest);
        expected.add(base);
        Collection<Block> actual = WitherBuildPattern.getMatchingBlocks(base.getLocation());
        Assertions.assertEquals(expected.size(), actual.size());
        Assertions.assertTrue(expected.containsAll(actual));
    }

}
