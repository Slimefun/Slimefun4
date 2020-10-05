package io.github.thebusybiscuit.slimefun4.testing.tests.multiblocks;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import be.seeseemelk.mockbukkit.MockBukkit;
import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import io.github.thebusybiscuit.slimefun4.core.multiblocks.MultiBlock;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.testing.TestUtilities;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;

class TestMultiBlocks {

    private static SlimefunPlugin plugin;

    @BeforeAll
    public static void load() {
        MockBukkit.mock();
        plugin = MockBukkit.load(SlimefunPlugin.class);
    }

    @AfterAll
    public static void unload() {
        MockBukkit.unmock();
    }

    @Test
    @DisplayName("Test Exceptions for invalid MultiBlock constructors")
    void testInvalidConstructors() {
        SlimefunItem item = TestUtilities.mockSlimefunItem(plugin, "MULTIBLOCK_TEST", new CustomItem(Material.BRICK, "&5Multiblock Test"));

        Assertions.assertThrows(IllegalArgumentException.class, () -> new MultiBlock(null, null, null));

        Assertions.assertThrows(IllegalArgumentException.class, () -> new MultiBlock(item, null, null));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new MultiBlock(item, new Material[4], null));

        Assertions.assertThrows(IllegalArgumentException.class, () -> new MultiBlock(item, new Material[9], BlockFace.EAST_NORTH_EAST));
    }

    @Test
    @DisplayName("Test MultiBlock constructor")
    void testValidConstructor() {
        SlimefunItem item = TestUtilities.mockSlimefunItem(plugin, "MULTIBLOCK_TEST", new CustomItem(Material.BRICK, "&5Multiblock Test"));
        MultiBlock multiblock = new MultiBlock(item, new Material[9], BlockFace.DOWN);

        Assertions.assertEquals(item, multiblock.getSlimefunItem());
        Assertions.assertArrayEquals(new Material[9], multiblock.getStructure());
        Assertions.assertEquals(BlockFace.DOWN, multiblock.getTriggerBlock());
    }

    @Test
    @DisplayName("Test symmetric MultiBlocks")
    void testSymmetry() {
        SlimefunItem item = TestUtilities.mockSlimefunItem(plugin, "MULTIBLOCK_TEST", new CustomItem(Material.BRICK, "&5Multiblock Test"));

        MultiBlock multiblock = new MultiBlock(item, new Material[] { null, null, null, Material.DIAMOND_BLOCK, null, Material.DIAMOND_BLOCK, null, Material.DISPENSER, null }, BlockFace.DOWN);
        Assertions.assertTrue(multiblock.isSymmetric());

        MultiBlock multiblock2 = new MultiBlock(item, new Material[] { Material.EMERALD_BLOCK, null, null, Material.EMERALD_BLOCK, null, Material.DIAMOND_BLOCK, Material.EMERALD_BLOCK, Material.DISPENSER, null }, BlockFace.DOWN);
        Assertions.assertFalse(multiblock2.isSymmetric());
    }

    @Test
    @DisplayName("Test equal MultiBlocks being equal")
    void testEqual() {
        SlimefunItem item = TestUtilities.mockSlimefunItem(plugin, "MULTIBLOCK_TEST", new CustomItem(Material.BRICK, "&5Multiblock Test"));

        MultiBlock multiblock = new MultiBlock(item, new Material[] { Material.BIRCH_WOOD, Material.BIRCH_WOOD, Material.BIRCH_WOOD, null, Material.CRAFTING_TABLE, null, Material.BIRCH_WOOD, Material.DISPENSER, Material.BIRCH_WOOD }, BlockFace.DOWN);
        MultiBlock multiblock2 = new MultiBlock(item, new Material[] { Material.BIRCH_WOOD, Material.BIRCH_WOOD, Material.BIRCH_WOOD, null, Material.CRAFTING_TABLE, null, Material.BIRCH_WOOD, Material.DISPENSER, Material.BIRCH_WOOD }, BlockFace.DOWN);

        Assertions.assertEquals(multiblock, multiblock2);
    }

    @Test
    @DisplayName("Test equal MultiBlocks with Tags but different Materials being equal")
    void testEqualWithTags() {
        SlimefunItem item = TestUtilities.mockSlimefunItem(plugin, "MULTIBLOCK_TEST", new CustomItem(Material.BRICK, "&5Multiblock Test"));

        // The wooden fences are different but the structure should still match.
        MultiBlock multiblock = new MultiBlock(item, new Material[] { Material.OAK_FENCE, Material.OAK_FENCE, Material.OAK_FENCE, null, Material.CRAFTING_TABLE, null, Material.BIRCH_WOOD, Material.DISPENSER, Material.BIRCH_WOOD }, BlockFace.DOWN);
        MultiBlock multiblock2 = new MultiBlock(item, new Material[] { Material.BIRCH_FENCE, Material.BIRCH_FENCE, Material.BIRCH_FENCE, null, Material.CRAFTING_TABLE, null, Material.BIRCH_WOOD, Material.DISPENSER, Material.BIRCH_WOOD }, BlockFace.DOWN);

        Assertions.assertEquals(multiblock, multiblock2);
    }

    @Test
    @DisplayName("Test MultiBlocks with moving pistons still being equal")
    void testEqualWithMovingPistons() {
        SlimefunItem item = TestUtilities.mockSlimefunItem(plugin, "PISTON_MULTIBLOCK_TEST", new CustomItem(Material.BRICK, "&5Multiblock Test"));

        // Some Pistons are moving but that should not interefere with the Multiblock
        MultiBlock multiblock = new MultiBlock(item, new Material[] { Material.PISTON, Material.MOVING_PISTON, Material.PISTON, null, Material.CRAFTING_TABLE, null, Material.PISTON, Material.STONE, Material.PISTON }, BlockFace.DOWN);
        MultiBlock multiblock2 = new MultiBlock(item, new Material[] { Material.MOVING_PISTON, Material.PISTON, Material.MOVING_PISTON, null, Material.CRAFTING_TABLE, null, Material.PISTON, Material.STONE, Material.PISTON }, BlockFace.DOWN);
        MultiBlock multiblock3 = new MultiBlock(item, new Material[] { Material.PISTON, Material.PISTON, Material.STICKY_PISTON, null, Material.CRAFTING_TABLE, null, Material.PISTON, Material.STONE, Material.PISTON }, BlockFace.DOWN);

        Assertions.assertEquals(multiblock, multiblock2);
        Assertions.assertNotEquals(multiblock, multiblock3);
    }

    @Test
    @DisplayName("Test different MultiBlocks not being equal")
    void testNotEqual() {
        SlimefunItem item = TestUtilities.mockSlimefunItem(plugin, "MULTIBLOCK_TEST", new CustomItem(Material.BRICK, "&5Multiblock Test"));

        MultiBlock multiblock = new MultiBlock(item, new Material[] { Material.BIRCH_WOOD, Material.BIRCH_WOOD, Material.BIRCH_WOOD, null, Material.CRAFTING_TABLE, null, Material.BIRCH_WOOD, Material.DISPENSER, Material.BIRCH_WOOD }, BlockFace.DOWN);
        MultiBlock multiblock2 = new MultiBlock(item, new Material[] { Material.BIRCH_WOOD, Material.BIRCH_WOOD, Material.BIRCH_WOOD, null, Material.EMERALD_BLOCK, null, Material.BIRCH_WOOD, Material.DISPENSER, Material.BIRCH_WOOD }, BlockFace.DOWN);
        MultiBlock multiblock3 = new MultiBlock(item, new Material[] { Material.DROPPER, Material.BIRCH_WOOD, Material.BIRCH_WOOD, null, Material.DIAMOND_BLOCK, null, Material.BIRCH_WOOD, Material.DISPENSER, Material.TNT }, BlockFace.DOWN);
        MultiBlock multiblock4 = new MultiBlock(item, new Material[] { Material.BIRCH_WOOD, Material.BIRCH_WOOD, Material.BIRCH_WOOD, null, Material.CRAFTING_TABLE, null, Material.BIRCH_WOOD, Material.DISPENSER, Material.BIRCH_WOOD }, BlockFace.SELF);

        Assertions.assertNotEquals(multiblock, null);
        Assertions.assertNotEquals(multiblock, multiblock2);
        Assertions.assertNotEquals(multiblock, multiblock3);
        Assertions.assertNotEquals(multiblock, multiblock4);
    }

}
