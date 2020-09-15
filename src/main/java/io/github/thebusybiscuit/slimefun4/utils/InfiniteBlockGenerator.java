package io.github.thebusybiscuit.slimefun4.utils;

import java.util.function.Predicate;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.block.BlockFromToEvent;

import io.github.thebusybiscuit.slimefun4.implementation.items.androids.MinerAndroid;
import scala.util.control.TailCalls.Call;

/**
 * This enum holds various ways of infinite block generators.
 * The most prominent member of these is the standard Cobblestone Generator.
 * We use this enum for performance optimizations for the {@link MinerAndroid}.
 * 
 * @author TheBusyBiscuit
 *
 */
public enum InfiniteBlockGenerator implements Predicate<Block> {

    /**
     * Your standard Cobblestone Generator with flowing lava and water.
     */
    COBBLESTONE_GENERATOR("COBBLESTONE"),

    /**
     * When lava flows onto a stationary water block it generates normal stone.
     */
    STONE_GENERATOR("STONE"),

    /**
     * The Basalt Generator (1.16+ only) allows you to generate infinite Basalt!
     */
    BASALT_GENERATOR("BASALT");

    public static final InfiniteBlockGenerator[] values = values();
    private static final BlockFace[] sameLevelFaces = { BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST };

    @Nullable
    private final Material material;

    @ParametersAreNonnullByDefault
    InfiniteBlockGenerator(String type) {
        this.material = Material.matchMaterial(type);
    }

    @Override
    public boolean test(@Nonnull Block b) {
        Validate.notNull(b, "Block cannot be null!");

        // This will eliminate non-matching base materials
        // If we are on a version without Basalt, it will be null here and not match.
        if (b.getType() == material) {
            switch (this) {
            case COBBLESTONE_GENERATOR:
                return hasSurroundingMaterials(b, true, Material.WATER, Material.LAVA);
            case STONE_GENERATOR:
                if (b.getRelative(BlockFace.UP).getType() == Material.LAVA) {
                    // We manually call the event here since it actually flows from the top
                    callEvent(b.getRelative(BlockFace.UP), b);
                    return hasSurroundingMaterials(b, false, Material.WATER);
                }
                else {
                    return false;
                }
            case BASALT_GENERATOR:
                if (b.getRelative(BlockFace.DOWN).getType() == Material.SOUL_SOIL) {
                    return hasSurroundingMaterials(b, true, Material.LAVA, Material.BLUE_ICE);
                }
                else {
                    return false;
                }
            default:
                return false;
            }
        }
        else {
            return false;
        }
    }

    @ParametersAreNonnullByDefault
    private boolean hasSurroundingMaterials(Block b, boolean firesEvent, Material... materials) {
        Validate.notNull(b, "The Block cannot be null!");
        Validate.notEmpty(materials, "Materials need to have a size of at least one!");

        boolean[] matches = new boolean[materials.length];
        int count = 0;

        for (BlockFace face : sameLevelFaces) {
            Block neighbour = b.getRelative(face);

            for (int i = 0; i < materials.length; i++) {
                if (neighbour.getType() == materials[i] && !matches[i]) {
                    matches[i] = true;
                    count++;

                    // This is our "trigger" material for the Event
                    if (firesEvent && i == 0) {
                        callEvent(neighbour, b);
                    }

                    break;
                }
            }

            if (count == materials.length) {
                return true;
            }
        }

        return count == materials.length;
    }

    /**
     * This method calls a {@link BlockFromToEvent} for this {@link InfiniteBlockGenerator}.
     * There are a few plugins who catch these events to inject custom {@link Material Materials} into
     * Cobblestone Generators, so we wanna give them the oppurtunity to catch this as well.
     * 
     * @param from
     *            The {@link Block} where our liquid is coming from.
     * @param to
     *            The {@link Block} our liquid has flown to / solidified at.
     * 
     * @return Our called {@link BlockFromToEvent}
     */
    @Nonnull
    private BlockFromToEvent callEvent(@Nonnull Block from, @Nonnull Block to) {
        BlockFromToEvent event = new BlockFromToEvent(from, to);
        Bukkit.getPluginManager().callEvent(event);
        return event;
    }

    /**
     * This will attempt to find an {@link InfiniteBlockGenerator} at the given {@link Block}.
     * 
     * @param b
     *            The {@link Block}
     * 
     * @return An {@link InfiniteBlockGenerator} or null if none was found.
     */
    @Nullable
    public static InfiniteBlockGenerator findAt(@Nonnull Block b) {
        Validate.notNull(b, "Cannot find a generator without a Location!");

        for (InfiniteBlockGenerator generator : values) {
            if (generator.test(b)) {
                return generator;
            }
        }

        return null;
    }

}
