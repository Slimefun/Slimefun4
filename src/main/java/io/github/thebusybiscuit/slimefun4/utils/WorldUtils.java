package io.github.thebusybiscuit.slimefun4.utils;

import javax.annotation.Nonnull;

import io.github.thebusybiscuit.slimefun4.api.MinecraftVersion;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.papermc.lib.PaperLib;
import org.apache.commons.lang.Validate;
import org.bukkit.World;

/**
 * This class holds utilities for {@link World}. This will become especially useful with the changes
 * in the "Cliffs and Caves" update.
 *
 * @author Walshy
 */
public final class WorldUtils {

    private WorldUtils() {}

    /**
     * Get the minimum Y of the given {@link World}. This is a feature introduced in Minecraft 1.17
     * and introduced into the Bukkit API in Minecraft 1.16.
     *
     * @param world
     *            The world of which to get minimum Y in.
     *
     * @return The minimum Y of the given world.
     */
    public static int getMinHeight(@Nonnull World world) {
        Validate.notNull(world, "World cannot be null!");

        MinecraftVersion major = SlimefunPlugin.getMinecraftVersion();
        int patch = PaperLib.getMinecraftPatchVersion();

        if (major.isAtLeast(MinecraftVersion.MINECRAFT_1_17) || major.isMinecraftVersion(16) && patch == 5) {
            return world.getMinHeight();
        } else {
            return 0;
        }
    }
}
