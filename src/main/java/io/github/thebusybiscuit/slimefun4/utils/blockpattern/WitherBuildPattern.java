package io.github.thebusybiscuit.slimefun4.utils.blockpattern;

import org.apache.commons.lang.Validate;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * This utility class holds methods relating to a pattern of blocks which can be used to build/spawn the wither.
 * <br>
 * The pattern to test for is the following: <br>
 * $ represents a wither skeleton skull and # represents soul sand
 * <br>
 * $$$ <br>
 * ### <br>
 * &nbsp #<br>
 * <br>
 *
 * @author md5sha256
 */
@ParametersAreNonnullByDefault
public class WitherBuildPattern {

    private WitherBuildPattern() {}

    /**
     * Get the blocks which match the build pattern for a wither.
     * <br>
     * This method will always check in the east-west direction before the north-south direction.
     * The reasoning behind east-west first is to match vanilla when withers are spawned and
     * both directions contain a valid T-shape, the east-west direction is always preferred.
     * <br>
     * <strong>This method should not be used to test if a Wither can be spawned at this location as it does not
     * check whether the other blocks within the pattern are air blocks.</strong>
     *
     * @param location The {@link Location} of the lowest block at the center, aka the base block of the build pattern
     * @return Returns non-null {@link Collection} of {@link Block}s which are eligible for creating a wither
     */
    public static @Nonnull Collection<Block> getMatchingBlocks(Location location) {
        Validate.notNull(location, "Location cannot be null");
        Validate.notNull(location.getWorld(), "Location#getWorld cannot be null");
        Collection<Block> baseEastWest = TShapedBlockPattern.getTShapeEastWest(location);
        if (TShapedBlockPattern.allBlocksMatchMaterial(Material.SOUL_SAND, baseEastWest)) {
            Collection<Block> blocks = new ArrayList<>(getWitherHeadsEastWest(location));
            if (!TShapedBlockPattern.allBlocksMatchMaterial(Material.WITHER_SKELETON_SKULL, blocks)) {
                return Collections.emptyList();
            }
            blocks.addAll(baseEastWest);
            return blocks;
        }

        Collection<Block> baseNorthSouth = TShapedBlockPattern.getTShapeNorthSouth(location);
        if (TShapedBlockPattern.allBlocksMatchMaterial(Material.SOUL_SAND, baseNorthSouth)) {
            Collection<Block> blocks = new ArrayList<>(getWitherHeadsNorthSouth(location));
            if (!TShapedBlockPattern.allBlocksMatchMaterial(Material.WITHER_SKELETON_SKULL, blocks)) {
                return Collections.emptyList();
            }
            blocks.addAll(baseNorthSouth);
            return blocks;
        }
        return Collections.emptyList();
    }

    /**
     * Get the blocks which should contain wither heads if the structure is facing east-west
     *
     * @param location The base of the wither structure
     * @return Returns the line of blocks facing east-west
     */
    private static @Nonnull Collection<Block> getWitherHeadsEastWest(Location location) {
        Validate.notNull(location, "Location cannot be null");
        Validate.notNull(location.getWorld(), "Location#getWorld cannot be null");
        Block base = location.getBlock();
        Block center = base.getRelative(BlockFace.UP, 2);
        return TShapedBlockPattern.getLineEastWest(center);
    }

    /**
     * Get the blocks which should contain wither heads if the structure is facing north-south
     *
     * @param location The base of the wither structure
     * @return Returns the line of blocks facing north-south
     */
    private static @Nonnull Collection<Block> getWitherHeadsNorthSouth(Location location) {
        Validate.notNull(location, "Location cannot be null");
        Validate.notNull(location.getWorld(), "Location#getWorld cannot be null");
        Block base = location.getBlock();
        Block center = base.getRelative(BlockFace.UP, 2);
        return TShapedBlockPattern.getLineNorthSouth(center);
    }

}
