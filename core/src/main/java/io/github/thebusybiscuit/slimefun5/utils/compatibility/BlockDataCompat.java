package io.github.thebusybiscuit.slimefun5.utils.compatibility;

import java.lang.reflect.Method;
import java.util.function.Consumer;

import javax.annotation.Nullable;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.FallingBlock;

/**
 * Compatibility helpers for the {@code org.bukkit.block.data.BlockData} API, which only exists from
 * Minecraft 1.13 onwards.
 *
 * <p>
 * <strong>Universal-jar contract:</strong> none of these methods reference any {@code block.data.*}
 * type in their signatures - block data is passed around as {@link Object}. This is essential: the
 * {@code block.data.*} interfaces are compileOnly stubs that are NOT shaded into the jar (org.bukkit
 * classes cannot be loaded from a plugin jar), so a consumer that named e.g. {@code Orientable} in
 * its bytecode would fail class verification with {@code NoClassDefFoundError} on a legacy server.
 * Consumers therefore hold block data as {@link Object} and read/write its properties through the
 * reflective {@link #get(Object, String)} / {@link #set(Object, String, Object)} helpers, never
 * casting to a {@code block.data.*} type.
 *
 * <p>
 * On legacy servers every lookup fails gracefully (returns {@code null} / no-op); on modern servers
 * the real {@code BlockData} methods are used.
 */
public final class BlockDataCompat {

    private BlockDataCompat() {}

    /**
     * Resolves the real {@code org.bukkit.block.data.BlockData} class, or {@code null} on legacy
     * versions where it does not exist.
     */
    @Nullable
    private static Class<?> blockDataClass() {
        try {
            return Class.forName("org.bukkit.block.data.BlockData");
        } catch (Throwable e) {
            return null;
        }
    }

    /**
     * Reflective equivalent of {@code block.getBlockData()}.
     *
     * @param block
     *            The {@link Block}
     *
     * @return The block's block data as an opaque {@link Object}, or {@code null} on legacy versions
     */
    @Nullable
    public static Object getBlockData(Block block) {
        try {
            Method method = Block.class.getMethod("getBlockData");
            return method.invoke(block);
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
     * @return The state's block data as an opaque {@link Object}, or {@code null} on legacy versions
     */
    @Nullable
    public static Object getBlockData(BlockState state) {
        try {
            Method method = BlockState.class.getMethod("getBlockData");
            return method.invoke(state);
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
     *            The block data (an opaque {@link Object} obtained from this class)
     */
    public static void setBlockData(Block block, Object data) {
        Class<?> blockData = blockDataClass();

        if (blockData == null || data == null) {
            return;
        }

        try {
            Method method = Block.class.getMethod("setBlockData", blockData);
            method.invoke(block, data);
        } catch (Throwable e) {
            // Not supported on this version - silently ignore.
        }
    }

    /**
     * Reflective equivalent of {@code block.setBlockData(data, applyPhysics)}. No-op on legacy versions.
     *
     * @param block
     *            The {@link Block}
     * @param data
     *            The block data (an opaque {@link Object} obtained from this class)
     * @param applyPhysics
     *            Whether to apply physics
     */
    public static void setBlockData(Block block, Object data, boolean applyPhysics) {
        Class<?> blockData = blockDataClass();

        if (blockData == null || data == null) {
            return;
        }

        try {
            Method method = Block.class.getMethod("setBlockData", blockData, boolean.class);
            method.invoke(block, data, applyPhysics);
        } catch (Throwable e) {
            // Not supported on this version - silently ignore.
        }
    }

    /**
     * Reflective equivalent of {@code material.createBlockData()}.
     *
     * @param material
     *            The {@link Material}
     *
     * @return The created block data as an opaque {@link Object}, or {@code null} on legacy versions
     */
    @Nullable
    public static Object createBlockData(Material material) {
        try {
            Method method = Material.class.getMethod("createBlockData");
            return method.invoke(material);
        } catch (Throwable e) {
            return null;
        }
    }

    /**
     * Reflective equivalent of {@code material.createBlockData(consumer)} (the consumer-configured
     * overload). The consumer receives the block data as an opaque {@link Object} (use
     * {@link #set(Object, String, Object)} to configure it). Returns {@code null} on legacy versions
     * (the consumer is then never invoked).
     *
     * @param material
     *            The {@link Material}
     * @param consumer
     *            A consumer that configures the created block data
     *
     * @return The created block data as an opaque {@link Object}, or {@code null} on legacy versions
     */
    @Nullable
    public static Object createBlockData(Material material, Consumer<Object> consumer) {
        try {
            Method method = Material.class.getMethod("createBlockData", Consumer.class);
            return method.invoke(material, consumer);
        } catch (Throwable e) {
            return null;
        }
    }

    /**
     * Reflectively reads a property of a block data object, e.g. {@code get(data, "getAxis")} or
     * {@code get(data, "getFacing")}. The result is returned as an {@link Object} (which the caller
     * may cast to a version-safe type such as {@code BlockFace}, but never to a {@code block.data.*}
     * type).
     *
     * @param data
     *            The block data {@link Object} (may be {@code null})
     * @param getter
     *            The no-arg getter name
     *
     * @return The property value, or {@code null} if unavailable
     */
    @Nullable
    public static Object get(@Nullable Object data, String getter) {
        return data == null ? null : ReflectionCompat.invoke(data, getter);
    }

    /**
     * Reflectively reads an {@code int} property of a block data object, e.g.
     * {@code getInt(data, "getLevel")}.
     *
     * @param data
     *            The block data {@link Object} (may be {@code null})
     * @param getter
     *            The no-arg getter name
     *
     * @return The property value, or {@code 0} if unavailable
     */
    public static int getInt(@Nullable Object data, String getter) {
        Object result = get(data, getter);
        return result instanceof Number ? ((Number) result).intValue() : 0;
    }

    /**
     * Reflectively writes a property of a block data object, e.g. {@code set(data, "setAxis", axis)}.
     * No-op if the data or method is unavailable.
     *
     * @param data
     *            The block data {@link Object} (may be {@code null})
     * @param setter
     *            The single-arg setter name
     * @param value
     *            The value to set
     */
    public static void set(@Nullable Object data, String setter, Object value) {
        if (data != null) {
            ReflectionCompat.invoke(data, setter, value);
        }
    }

    /**
     * Version-safe {@code instanceof} test against a {@code block.data.*} type by name, e.g.
     * {@code isInstance(data, "org.bukkit.block.data.Ageable")}. Returns {@code false} on legacy
     * versions where the type does not exist.
     *
     * @param data
     *            The block data {@link Object} (may be {@code null})
     * @param className
     *            The fully-qualified class name to test against
     *
     * @return Whether {@code data} is an instance of the named type
     */
    public static boolean isInstance(@Nullable Object data, String className) {
        if (data == null) {
            return false;
        }

        try {
            return Class.forName(className).isInstance(data);
        } catch (Throwable e) {
            return false;
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
     * Reflective equivalent of {@code world.spawnFallingBlock(location, data)} (the block-data
     * overload, 1.13+), falling back to the legacy {@code (Location, Material, byte)} variant.
     *
     * @param world
     *            The {@link World}
     * @param location
     *            The spawn {@link Location}
     * @param data
     *            The block data {@link Object}
     *
     * @return The spawned {@link FallingBlock}, or {@code null} if unsupported
     */
    @Nullable
    public static FallingBlock spawnFallingBlock(World world, Location location, Object data) {
        Class<?> blockData = blockDataClass();

        if (blockData != null && data != null) {
            try {
                Method method = World.class.getMethod("spawnFallingBlock", Location.class, blockData);
                return (FallingBlock) method.invoke(world, location, data);
            } catch (Throwable ignored) {
                // Fall through to the legacy accessor.
            }
        }

        try {
            Object material = data != null ? ReflectionCompat.invoke(data, "getMaterial") : Material.STONE;
            Material mat = material instanceof Material ? (Material) material : Material.STONE;
            Method legacy = World.class.getMethod("spawnFallingBlock", Location.class, Material.class, byte.class);
            return (FallingBlock) legacy.invoke(world, location, mat, (byte) 0);
        } catch (Throwable e2) {
            return null;
        }
    }

    /**
     * Spawns a {@link FallingBlock} matching the given source {@link Block}. On 1.13+ it uses the
     * block's {@code BlockData}; on 1.8-1.12 it spawns from the block's real {@link Material} and legacy
     * data byte (so the falling block is the actual block, not a STONE placeholder).
     *
     * @param world     The {@link World}
     * @param location  The spawn {@link Location}
     * @param source    The {@link Block} to copy
     *
     * @return The spawned {@link FallingBlock}, or {@code null} if unsupported
     */
    @Nullable
    public static FallingBlock spawnFallingBlock(World world, Location location, Block source) {
        Object data = getBlockData(source);
        if (data != null) {
            FallingBlock fb = spawnFallingBlock(world, location, data);
            if (fb != null) {
                return fb;
            }
        }

        try {
            Method legacy = World.class.getMethod("spawnFallingBlock", Location.class, Material.class, byte.class);
            return (FallingBlock) legacy.invoke(world, location, source.getType(), source.getData());
        } catch (Throwable e) {
            return null;
        }
    }
}
