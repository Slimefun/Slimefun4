package io.github.thebusybiscuit.slimefun4.api.blocks;

import javax.annotation.Nonnull;

import org.apache.commons.lang.Validate;
import org.bukkit.block.data.BlockData;

import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;

/**
 * The {@link BlockDatabase} will handle {@link SlimefunBlockData} in the future and
 * possibly allow for SQL support too.
 * <p>
 * Right now, it is just merely a WIP class.
 * 
 * @author TheBusyBiscuit
 * 
 * @see SlimefunBlockData
 *
 */
public class BlockDatabase {

    /**
     * This is our {@link SlimefunPlugin} instance.
     */
    private final SlimefunPlugin plugin;

    /**
     * This is our singleton instance for {@link EmptyBlockData}.
     * It will always return null and cannot hold any values.
     */
    private final EmptyBlockData emptyBlockData = new EmptyBlockData();

    /**
     * This creates a new {@link BlockDatabase}.
     * 
     * @param plugin
     *            Our {@link SlimefunPlugin} instance
     */
    public BlockDatabase(@Nonnull SlimefunPlugin plugin) {
        Validate.notNull(plugin, "The plugin instance cannot be null!");

        this.plugin = plugin;
    }

}
