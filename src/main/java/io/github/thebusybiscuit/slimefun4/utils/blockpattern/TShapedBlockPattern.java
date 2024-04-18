package io.github.thebusybiscuit.slimefun4.utils.blockpattern;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import com.google.common.base.Preconditions;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

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
        Preconditions.checkNotNull(material, "Material cannot be null");
        Preconditions.checkNotNull(location, "Location cannot be null");
        Preconditions.checkNotNull(location.getWorld(), "Location#getWorld cannot be null");
        // Try non-inverted positions
        Collection<Block> eastWest = getTShapeEastWest(location, false);
        if (allBlocksMatchMaterial(material, eastWest)) {
            return eastWest;
        }
        Collection<Block> northSouth = getTShapeNorthSouth(location, false);
        if (allBlocksMatchMaterial(material, northSouth)) {
            return northSouth;
        }
        // Try inverted positions
        Collection<Block> eastWestInverted = getTShapeEastWest(location, true);
        if (allBlocksMatchMaterial(material, eastWestInverted)) {
            return eastWestInverted;
        }
        Collection<Block> northSouthInverted = getTShapeNorthSouth(location, true);
        if (allBlocksMatchMaterial(material, northSouthInverted)) {
            return northSouthInverted;
        }
        return Collections.emptyList();
    }

    /**
     * Check whether all the {@link Block}s in a {@link Collection} are of a specified {@link Material}.
     *
     * @param material The material to check
     * @param blocks   The blocks to check
     * @return True if all blocks are of the specified material, false otherwise
     */
    public static boolean allBlocksMatchMaterial(Material material, Collection<Block> blocks) {
        Preconditions.checkNotNull(material, "Material cannot be null");
        Preconditions.checkNotNull(blocks, "blocks cannot be null");
        for (Block block : blocks) {
            if (block.getType() != material) {
                return false;
            }
        }
        return true;
    }

    /**
     * Check whether all the {@link Block}s in a {@link Collection} are of a specified {@link Material}
     * which is {@link Tag tagged}
     * This method assumes that all elements in materials are interchangeable and can be
     * mixed and matched
     *
     * @param tag    The material tag
     * @param blocks The blocks to check
     * @return True if all blocks are any of the specified materials, false otherwise
     */
    public static boolean allBlocksMatchTag(Tag<Material> tag, Collection<Block> blocks) {
        Preconditions.checkNotNull(tag, "Tag cannot be null");
        Preconditions.checkNotNull(blocks, "blocks cannot be null");
        for (Block block : blocks) {
            if (!tag.isTagged(block.getType())) {
                return false;
            }
        }
        return true;
    }

    /**
     * Get the blocks which make up at T-shape facing east-west
     *
     * @param location The {@link Location} of the lowest block at the center, aka the base block of the T-shape
     * @param inverted Whether the T shape is inverted
     * @return Returns a {@link Collection} of {@link Block}s which match the T-shape
     */
    public static @Nonnull Collection<Block> getTShapeEastWest(Location location, boolean inverted) {
        Preconditions.checkNotNull(location, "Location cannot be null");
        Preconditions.checkNotNull(location.getWorld(), "Location#getWorld cannot be null");
        Block base = location.getBlock();
        Block center = inverted ? base : base.getRelative(BlockFace.UP);
        Collection<Block> blocks = new ArrayList<>(getLineEastWest(center));
        blocks.add(base);
        return blocks;
    }

    /**
     * Get the blocks which make up at T-shape facing north-south
     *
     * @param location The {@link Location} of the lowest block at the center, aka the base block of the T-shape
     * @param inverted Whether the T shape is inverted
     * @return Returns a {@link Collection} of {@link Block}s which match the T-shape
     */
    public static @Nonnull Collection<Block> getTShapeNorthSouth(Location location, boolean inverted) {
        Preconditions.checkNotNull(location, "Location cannot be null");
        Preconditions.checkNotNull(location.getWorld(), "Location#getWorld cannot be null");
        Block base = location.getBlock();
        Block center = inverted ? base : base.getRelative(BlockFace.UP);
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
        Preconditions.checkNotNull(center, "center cannot be null");
        Block north = center.getRelative(BlockFace.NORTH);
        Block south = center.getRelative(BlockFace.SOUTH);
        return List.of(center, north, south);
    }

    /**
     * Gets the blocks which are in a 3-block line facing east-west
     *
     * @param center The block at the center of the line
     * @return Returns a {@link Collection} of {@link Block}s comprised of the 3 blocks
     */
    public static @Nonnull Collection<Block> getLineEastWest(Block center) {
        Preconditions.checkNotNull(center, "center cannot be null");
        Block east = center.getRelative(BlockFace.EAST);
        Block west = center.getRelative(BlockFace.WEST);
        return List.of(center, east, west);
    }

}
