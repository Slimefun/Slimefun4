package io.github.thebusybiscuit.slimefun5.utils.compatibility;

import java.lang.reflect.Method;
import java.util.function.Consumer;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.FallingBlock;

/**
 * Compatibility helpers for the {@code BlockData} API, which only exists from Minecraft 1.13 onwards.
 *
 * <p>
 * {@code Block#getBlockData()}, {@code Block#setBlockData(BlockData)} and
 * {@code Material#createBlockData()} are absent on the 1.8.8 API floor, so they are invoked
 * reflectively. On legacy servers the lookups fail gracefully (returning {@code null} / no-op); on
 * modern servers the real methods are used and the returned values are the server's real
 * {@code BlockData} implementations, which satisfy our (non-shaded) compat interfaces.
 */
public final class BlockDataCompat {

    private BlockDataCompat() {}

    /**
     * Reflective equivalent of {@code block.getBlockData()}.
     *
     * @param block
     *            The {@link Block}
     *
     * @return The block's {@link BlockData}, or {@code null} on legacy versions
     */
    public static BlockData getBlockData(Block block) {
        try {
            Method method = Block.class.getMethod("getBlockData");
            return (BlockData) method.invoke(block);
        } catch (Throwable e) {
            return null;
        }
    }

    /**
     * Reflective equivalent of {@code state.getBlockData()}.
     *
     * @param state
     *            The {@link BlockState}
     *
     * @return The state's {@link BlockData}, or {@code null} on legacy versions
     */
    public static BlockData getBlockData(BlockState state) {
        try {
            Method method = BlockState.class.getMethod("getBlockData");
            return (BlockData) method.invoke(state);
        } catch (Throwable e) {
            return null;
        }
    }

    /**
     * Reflective equivalent of {@code block.setBlockData(data)}. No-op on legacy versions.
     *
     * @param block
     *            The {@link Block}
     * @param data
     *            The {@link BlockData} to apply
     */
    public static void setBlockData(Block block, BlockData data) {
        try {
            Method method = Block.class.getMethod("setBlockData", BlockData.class);
            method.invoke(block, data);
        } catch (Throwable e) {
            // Not supported on this version — silently ignore.
        }
    }

    /**
     * Reflective equivalent of {@code block.setBlockData(data, applyPhysics)}. No-op on legacy versions.
     *
     * @param block
     *            The {@link Block}
     * @param data
     *            The {@link BlockData} to apply
     * @param applyPhysics
     *            Whether to apply physics
     */
    public static void setBlockData(Block block, BlockData data, boolean applyPhysics) {
        try {
            Method method = Block.class.getMethod("setBlockData", BlockData.class, boolean.class);
            method.invoke(block, data, applyPhysics);
        } catch (Throwable e) {
            // Not supported on this version — silently ignore.
        }
    }

    /**
     * Reflective equivalent of {@code material.createBlockData()}.
     *
     * @param material
     *            The {@link Material}
     *
     * @return The created {@link BlockData}, or {@code null} on legacy versions
     */
    public static BlockData createBlockData(Material material) {
        try {
            Method method = Material.class.getMethod("createBlockData");
            return (BlockData) method.invoke(material);
        } catch (Throwable e) {
            return null;
        }
    }

    /**
     * Reflective equivalent of {@code material.createBlockData(consumer)} (the consumer-configured
     * overload). Returns {@code null} on legacy versions (the consumer is then never invoked).
     *
     * @param material
     *            The {@link Material}
     * @param consumer
     *            A consumer that configures the created {@link BlockData}
     *
     * @return The created {@link BlockData}, or {@code null} on legacy versions
     */
    public static BlockData createBlockData(Material material, Consumer<BlockData> consumer) {
        try {
            Method method = Material.class.getMethod("createBlockData", Consumer.class);
            return (BlockData) method.invoke(material, consumer);
        } catch (Throwable e) {
            return null;
        }
    }

    /**
     * Returns the {@link Material} represented by a {@link FallingBlock}, across versions. Uses the
     * modern {@code getBlockData().getMaterial()} when available and falls back to the legacy
     * {@code getMaterial()}.
     *
     * @param fallingBlock
     *            The {@link FallingBlock}
     *
     * @return The {@link Material}, or {@link Material#STONE} if it could not be determined
     */
    public static Material getMaterial(FallingBlock fallingBlock) {
        try {
            Object data = FallingBlock.class.getMethod("getBlockData").invoke(fallingBlock);

            if (data != null) {
                Object material = data.getClass().getMethod("getMaterial").invoke(data);

                if (material instanceof Material) {
                    return (Material) material;
                }
            }
        } catch (Throwable ignored) {
            // Fall through to the legacy accessor.
        }

        try {
            Object material = FallingBlock.class.getMethod("getMaterial").invoke(fallingBlock);

            if (material instanceof Material) {
                return (Material) material;
            }
        } catch (Throwable ignored) {
            // Neither accessor available.
        }

        return Material.STONE;
    }

    /**
     * Reflective equivalent of {@code world.spawnFallingBlock(location, data)} (the {@link BlockData}
     * overload, 1.13+), falling back to the legacy {@code (Location, Material, byte)} variant.
     *
     * @param world
     *            The {@link World}
     * @param location
     *            The spawn {@link Location}
     * @param data
     *            The {@link BlockData}
     *
     * @return The spawned {@link FallingBlock}, or {@code null} if unsupported
     */
    public static FallingBlock spawnFallingBlock(World world, Location location, BlockData data) {
        try {
            Method method = World.class.getMethod("spawnFallingBlock", Location.class, BlockData.class);
            return (FallingBlock) method.invoke(world, location, data);
        } catch (Throwable e) {
            try {
                Material material = data != null ? data.getMaterial() : Material.STONE;
                Method legacy = World.class.getMethod("spawnFallingBlock", Location.class, Material.class, byte.class);
                return (FallingBlock) legacy.invoke(world, location, material, (byte) 0);
            } catch (Throwable e2) {
                return null;
            }
        }
    }
}
