package io.github.thebusybiscuit.slimefun4.tests.multiblocks;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import be.seeseemelk.mockbukkit.MockBukkit;
import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import io.github.thebusybiscuit.slimefun4.core.MultiBlock;
import io.github.thebusybiscuit.slimefun4.mocks.SlimefunMocks;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;

public class TestMultiBlocks {

    @BeforeAll
    public static void load() {
        MockBukkit.mock();
    }

    @AfterAll
    public static void unload() {
        MockBukkit.unmock();
    }

    @Test
    public void testInvalidConstructors() {
        SlimefunItem item = SlimefunMocks.mockSlimefunItem("MULTIBLOCK_TEST", new CustomItem(Material.BRICK, "&5Multiblock Test"));

        Assertions.assertThrows(IllegalArgumentException.class, () -> new MultiBlock(null, null, null));

        Assertions.assertThrows(IllegalArgumentException.class, () -> new MultiBlock(item, null, null));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new MultiBlock(item, new Material[4], null));

        Assertions.assertThrows(IllegalArgumentException.class, () -> new MultiBlock(item, new Material[9], BlockFace.EAST_NORTH_EAST));
    }

    @Test
    public void testValidConstructor() {
        SlimefunItem item = SlimefunMocks.mockSlimefunItem("MULTIBLOCK_TEST", new CustomItem(Material.BRICK, "&5Multiblock Test"));
        MultiBlock multiblock = new MultiBlock(item, new Material[9], BlockFace.DOWN);

        Assertions.assertEquals(item, multiblock.getSlimefunItem());
        Assertions.assertArrayEquals(new Material[9], multiblock.getStructure());
        Assertions.assertEquals(BlockFace.DOWN, multiblock.getTriggerBlock());
    }

    @Test
    public void testSymmetry() {
        SlimefunItem item = SlimefunMocks.mockSlimefunItem("MULTIBLOCK_TEST", new CustomItem(Material.BRICK, "&5Multiblock Test"));

        MultiBlock multiblock = new MultiBlock(item, new Material[] { null, null, null, Material.DIAMOND_BLOCK, null, Material.DIAMOND_BLOCK, null, Material.DISPENSER, null }, BlockFace.DOWN);

        Assertions.assertTrue(multiblock.isSymmetric());

        MultiBlock multiblock2 = new MultiBlock(item, new Material[] { Material.EMERALD_BLOCK, null, null, Material.EMERALD_BLOCK, null, Material.DIAMOND_BLOCK, Material.EMERALD_BLOCK, Material.DISPENSER, null }, BlockFace.DOWN);

        Assertions.assertFalse(multiblock2.isSymmetric());
    }

    @Test
    public void testEquality() {
        SlimefunItem item = SlimefunMocks.mockSlimefunItem("MULTIBLOCK_TEST", new CustomItem(Material.BRICK, "&5Multiblock Test"));

        MultiBlock multiblock = new MultiBlock(item, new Material[] { Material.BIRCH_WOOD, Material.BIRCH_WOOD, Material.BIRCH_WOOD, null, Material.CRAFTING_TABLE, null, Material.BIRCH_WOOD, Material.DISPENSER, Material.BIRCH_WOOD }, BlockFace.DOWN);

        MultiBlock multiblock2 = new MultiBlock(item, new Material[] { Material.BIRCH_WOOD, Material.BIRCH_WOOD, Material.BIRCH_WOOD, null, Material.CRAFTING_TABLE, null, Material.BIRCH_WOOD, Material.DISPENSER, Material.BIRCH_WOOD }, BlockFace.DOWN);

        MultiBlock multiblock3 = new MultiBlock(item, new Material[] { Material.BIRCH_WOOD, Material.BIRCH_WOOD, Material.BIRCH_WOOD, null, Material.EMERALD_BLOCK, null, Material.BIRCH_WOOD, Material.DISPENSER, Material.BIRCH_WOOD }, BlockFace.DOWN);

        MultiBlock multiblock4 = new MultiBlock(item, new Material[] { Material.DROPPER, Material.BIRCH_WOOD, Material.BIRCH_WOOD, null, Material.DIAMOND_BLOCK, null, Material.BIRCH_WOOD, Material.DISPENSER, Material.TNT }, BlockFace.DOWN);

        MultiBlock multiblock5 = new MultiBlock(item, new Material[] { Material.BIRCH_WOOD, Material.BIRCH_WOOD, Material.BIRCH_WOOD, null, Material.CRAFTING_TABLE, null, Material.BIRCH_WOOD, Material.DISPENSER, Material.BIRCH_WOOD }, BlockFace.SELF);

        Assertions.assertTrue(multiblock.isSymmetric());

        Assertions.assertTrue(multiblock.equals(multiblock2));
        Assertions.assertFalse(multiblock.equals(null));
        Assertions.assertFalse(multiblock.equals(multiblock3));
        Assertions.assertFalse(multiblock.equals(multiblock4));
        Assertions.assertFalse(multiblock.equals(multiblock5));
    }

}
