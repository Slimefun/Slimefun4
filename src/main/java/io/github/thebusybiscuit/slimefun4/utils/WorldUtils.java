package io.github.thebusybiscuit.slimefun4.utils;

import javax.annotation.Nonnull;

import io.github.thebusybiscuit.slimefun4.api.MinecraftVersion;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import org.bukkit.World;

/**
 * Utilities for {@link World}.
 *
 * @author WalshyDev
 */
public final class WorldUtils {

    private WorldUtils() {}

    /**
     * Get the minimum Y of the given {@link World}. This is a feature introduced in Minecraft 1.17
     * and introduced into the Bukkit API in Minecraft 1.16.
     *
     * @param world The world of which to get minimum Y in.
     * @return The minimum Y of the given world.
     */
    public static int getMinHeight(@Nonnull World world) {
        return SlimefunPlugin.getMinecraftVersion().isAtLeast(MinecraftVersion.MINECRAFT_1_16)
            ? world.getMinHeight() : 0;
    }
}
