package io.github.thebusybiscuit.slimefun4.testing.tests.multiblocks;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import io.github.thebusybiscuit.slimefun4.core.multiblocks.MultiBlock;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.testing.TestUtilities;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;

public class TestMultiBlocks {

    private static SlimefunPlugin plugin;

    @BeforeAll
    public static void load() {
        ServerMock server = MockBukkit.mock();
        TestUtilities.registerDefaultTags(server);
        plugin = MockBukkit.load(SlimefunPlugin.class);
    }

    @AfterAll
    public static void unload() {
        MockBukkit.unmock();
    }

    @Test
    public void testInvalidConstructors() {
        SlimefunItem item = TestUtilities.mockSlimefunItem(plugin, "MULTIBLOCK_TEST", new CustomItem(Material.BRICK, "&5Multiblock Test"));

        Assertions.assertThrows(IllegalArgumentException.class, () -> new MultiBlock(null, null, null));

        Assertions.assertThrows(IllegalArgumentException.class, () -> new MultiBlock(item, null, null));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new MultiBlock(item, new Material[4], null));

        Assertions.assertThrows(IllegalArgumentException.class, () -> new MultiBlock(item, new Material[9], BlockFace.EAST_NORTH_EAST));
    }

    @Test
    public void testValidConstructor() {
        SlimefunItem item = TestUtilities.mockSlimefunItem(plugin, "MULTIBLOCK_TEST", new CustomItem(Material.BRICK, "&5Multiblock Test"));
        MultiBlock multiblock = new MultiBlock(item, new Material[9], BlockFace.DOWN);

        Assertions.assertEquals(item, multiblock.getSlimefunItem());
        Assertions.assertArrayEquals(new Material[9], multiblock.getStructure());
        Assertions.assertEquals(BlockFace.DOWN, multiblock.getTriggerBlock());
    }

    @Test
    public void testSymmetry() {
        SlimefunItem item = TestUtilities.mockSlimefunItem(plugin, "MULTIBLOCK_TEST", new CustomItem(Material.BRICK, "&5Multiblock Test"));

        MultiBlock multiblock = new MultiBlock(item, new Material[] { null, null, null, Material.DIAMOND_BLOCK, null, Material.DIAMOND_BLOCK, null, Material.DISPENSER, null }, BlockFace.DOWN);
        Assertions.assertTrue(multiblock.isSymmetric());

        MultiBlock multiblock2 = new MultiBlock(item, new Material[] { Material.EMERALD_BLOCK, null, null, Material.EMERALD_BLOCK, null, Material.DIAMOND_BLOCK, Material.EMERALD_BLOCK, Material.DISPENSER, null }, BlockFace.DOWN);
        Assertions.assertFalse(multiblock2.isSymmetric());
    }

    @Test
    public void testEqual() {
        SlimefunItem item = TestUtilities.mockSlimefunItem(plugin, "MULTIBLOCK_TEST", new CustomItem(Material.BRICK, "&5Multiblock Test"));

        MultiBlock multiblock = new MultiBlock(item, new Material[] { Material.BIRCH_WOOD, Material.BIRCH_WOOD, Material.BIRCH_WOOD, null, Material.CRAFTING_TABLE, null, Material.BIRCH_WOOD, Material.DISPENSER, Material.BIRCH_WOOD }, BlockFace.DOWN);
        MultiBlock multiblock2 = new MultiBlock(item, new Material[] { Material.BIRCH_WOOD, Material.BIRCH_WOOD, Material.BIRCH_WOOD, null, Material.CRAFTING_TABLE, null, Material.BIRCH_WOOD, Material.DISPENSER, Material.BIRCH_WOOD }, BlockFace.DOWN);

        Assertions.assertTrue(multiblock.equals(multiblock2));
    }

    @Test
    public void testEqualWithTags() {
        SlimefunItem item = TestUtilities.mockSlimefunItem(plugin, "MULTIBLOCK_TEST", new CustomItem(Material.BRICK, "&5Multiblock Test"));

        // The wooden fences are different but the structure should still match.
        MultiBlock multiblock = new MultiBlock(item, new Material[] { Material.OAK_FENCE, Material.OAK_FENCE, Material.OAK_FENCE, null, Material.CRAFTING_TABLE, null, Material.BIRCH_WOOD, Material.DISPENSER, Material.BIRCH_WOOD }, BlockFace.DOWN);
        MultiBlock multiblock2 = new MultiBlock(item, new Material[] { Material.BIRCH_FENCE, Material.BIRCH_FENCE, Material.BIRCH_FENCE, null, Material.CRAFTING_TABLE, null, Material.BIRCH_WOOD, Material.DISPENSER, Material.BIRCH_WOOD }, BlockFace.DOWN);

        Assertions.assertTrue(multiblock.equals(multiblock2));
    }

    @Test
    public void testEqualWithMovingPistons() {
        SlimefunItem item = TestUtilities.mockSlimefunItem(plugin, "PISTON_MULTIBLOCK_TEST", new CustomItem(Material.BRICK, "&5Multiblock Test"));

        // Some Pistons are moving but that should not interefere with the Multiblock
        MultiBlock multiblock = new MultiBlock(item, new Material[] { Material.PISTON, Material.MOVING_PISTON, Material.PISTON, null, Material.CRAFTING_TABLE, null, Material.PISTON, Material.STONE, Material.PISTON }, BlockFace.DOWN);
        MultiBlock multiblock2 = new MultiBlock(item, new Material[] { Material.MOVING_PISTON, Material.PISTON, Material.MOVING_PISTON, null, Material.CRAFTING_TABLE, null, Material.PISTON, Material.STONE, Material.PISTON }, BlockFace.DOWN);
        MultiBlock multiblock3 = new MultiBlock(item, new Material[] { Material.PISTON, Material.PISTON, Material.STICKY_PISTON, null, Material.CRAFTING_TABLE, null, Material.PISTON, Material.STONE, Material.PISTON }, BlockFace.DOWN);

        Assertions.assertTrue(multiblock.equals(multiblock2));
        Assertions.assertFalse(multiblock.equals(multiblock3));
    }

    @Test
    public void testNotEqual() {
        SlimefunItem item = TestUtilities.mockSlimefunItem(plugin, "MULTIBLOCK_TEST", new CustomItem(Material.BRICK, "&5Multiblock Test"));

        MultiBlock multiblock = new MultiBlock(item, new Material[] { Material.BIRCH_WOOD, Material.BIRCH_WOOD, Material.BIRCH_WOOD, null, Material.CRAFTING_TABLE, null, Material.BIRCH_WOOD, Material.DISPENSER, Material.BIRCH_WOOD }, BlockFace.DOWN);
        MultiBlock multiblock2 = new MultiBlock(item, new Material[] { Material.BIRCH_WOOD, Material.BIRCH_WOOD, Material.BIRCH_WOOD, null, Material.EMERALD_BLOCK, null, Material.BIRCH_WOOD, Material.DISPENSER, Material.BIRCH_WOOD }, BlockFace.DOWN);
        MultiBlock multiblock3 = new MultiBlock(item, new Material[] { Material.DROPPER, Material.BIRCH_WOOD, Material.BIRCH_WOOD, null, Material.DIAMOND_BLOCK, null, Material.BIRCH_WOOD, Material.DISPENSER, Material.TNT }, BlockFace.DOWN);
        MultiBlock multiblock4 = new MultiBlock(item, new Material[] { Material.BIRCH_WOOD, Material.BIRCH_WOOD, Material.BIRCH_WOOD, null, Material.CRAFTING_TABLE, null, Material.BIRCH_WOOD, Material.DISPENSER, Material.BIRCH_WOOD }, BlockFace.SELF);

        Assertions.assertFalse(multiblock.equals(null));
        Assertions.assertFalse(multiblock.equals(multiblock2));
        Assertions.assertFalse(multiblock.equals(multiblock3));
        Assertions.assertFalse(multiblock.equals(multiblock4));
    }

}
