package io.github.thebusybiscuit.slimefun4.utils.blockpattern;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

/**
 * This utility class holds methods relating to a pattern of blocks in a T-shape
 *
 * @author md5sha256
 */
@ParametersAreNonnullByDefault
public class TShapedBlockPattern {

    private TShapedBlockPattern() {}

    /**
     * Get the blocks of a specified material which are in a T-shape pattern at a given location.
     * <br>
     * <strong>NOTE: The T-Shape is defined as 3 blocks wide and 2 blocks tall</strong>
     * <br>
     * This method will always check in the east-west direction before the north-south direction.
     * The reasoning behind east-west first is to match vanilla when iron golems or withers are spawned and
     * both directions contain a valid T-shape, the east-west direction is always preferred.
     *
     * @param material The {@link Material} which the blocks should match
     * @param location The {@link Location} of the lowest block at the center, aka the base block of the T-shape
     * @return Returns non-null {@link Collection} of {@link Block}s which match the specified {@link Material} and are
     * in a T-Shape in no particular order
     */
    public static @Nonnull Collection<Block> getMatchingBlocks(Material material, Location location) {
        Collection<Block> eastWest = getTShapeEastWest(location);
        if (matches(material, eastWest)) {
            return eastWest;
        }
        Collection<Block> northSouth = getTShapeNorthSouth(location);
        if (matches(material, northSouth)) {
            return northSouth;
        }
        return Collections.emptyList();
    }

    /**
     * Check whether all the {@link Block}s in a {@link Collection} are of a specified {@link Material}
     *
     * @param material The material to check
     * @param blocks   THe blocks to check
     * @return True if all blocks are of the specified material, false otherwise
     */
    public static boolean matches(Material material, Collection<Block> blocks) {
        for (Block block : blocks) {
            if (block.getType() != material) {
                return false;
            }
        }
        return true;
    }

    /**
     * Get the blocks which make up at T-shape facing east-west
     *
     * @param location The {@link Location} of the lowest block at the center, aka the base block of the T-shape
     * @return Returns a {@link Collection} of {@link Block}s which match the T-shape
     */
    public static @Nonnull Collection<Block> getTShapeEastWest(Location location) {
        Block base = location.getBlock();
        Block center = base.getRelative(BlockFace.UP);
        Collection<Block> blocks = new ArrayList<>(getLineEastWest(center));
        blocks.add(base);
        return blocks;
    }

    /**
     * Get the blocks which make up at T-shape facing north-south
     *
     * @param location The {@link Location} of the lowest block at the center, aka the base block of the T-shape
     * @return Returns a {@link Collection} of {@link Block}s which match the T-shape
     */
    public static @Nonnull Collection<Block> getTShapeNorthSouth(Location location) {
        Block base = location.getBlock();
        Block center = base.getRelative(BlockFace.UP);
        Collection<Block> blocks = new ArrayList<>(getLineNorthSouth(center));
        blocks.add(base);
        return blocks;
    }

    /**
     * Gets the blocks which are in a 3-block line facing north-south
     *
     * @param center The block at the center of the line
     * @return Returns a {@link Collection} of {@link Block}s comprised of the 3 blocks
     */
    public static @Nonnull Collection<Block> getLineNorthSouth(Block center) {
        Block north = center.getRelative(BlockFace.NORTH);
        Block south = center.getRelative(BlockFace.SOUTH);
        return Arrays.asList(center, north, south);
    }

    /**
     * Gets the blocks which are in a 3-block line facing east-west
     *
     * @param center The block at the center of the line
     * @return Returns a {@link Collection} of {@link Block}s comprised of the 3 blocks
     */
    public static @Nonnull Collection<Block> getLineEastWest(Block center) {
        Block east = center.getRelative(BlockFace.EAST);
        Block west = center.getRelative(BlockFace.WEST);
        return Arrays.asList(center, east, west);
    }

}
