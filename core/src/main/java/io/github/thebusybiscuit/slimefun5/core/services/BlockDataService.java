package io.github.thebusybiscuit.slimefun5.core.services;

import io.github.thebusybiscuit.slimefun5.utils.compatibility.PdcCompat;

import io.github.thebusybiscuit.slimefun5.utils.compatibility.MaterialCompat;

import java.util.Optional;
import java.util.logging.Level;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import io.github.thebusybiscuit.slimefun5.utils.tags.SlimefunTag;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import io.github.thebusybiscuit.slimefun5.libraries.keys.Keyed;
import org.bukkit.Material;
import io.github.thebusybiscuit.slimefun5.libraries.keys.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.plugin.Plugin;

import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;
import io.papermc.lib.PaperLib;

/**
 * The {@link BlockDataService} is similar to the {@link CustomItemDataService},
 * it is responsible for storing NBT data inside a {@link TileState}.
 * 
 * This is used to speed up performance and prevent
 * 
 * @author TheBusyBiscuit
 *
 */
public class BlockDataService implements Keyed {

    // org.bukkit.block.TileState is 1.14+; resolve it reflectively so this class still loads on older
    // servers, where block identity is handled entirely by the file-based BlockStorage instead.
    private static final Class<?> TILE_STATE = resolveTileState();

    private final NamespacedKey namespacedKey;

    private static Class<?> resolveTileState() {
        try {
            return Class.forName("org.bukkit.block.TileState");
        } catch (Throwable ignored) {
            return null;
        }
    }

    /**
     * This creates a new {@link BlockDataService} for the given {@link Plugin}.
     * The {@link Plugin} and key will together form a {@link NamespacedKey} used to store
     * data on a {@link TileState}.
     * 
     * @param plugin
     *            The {@link Plugin} responsible for this service
     * @param key
     *            The key under which to store data
     */
    public BlockDataService(@Nonnull Plugin plugin, @Nonnull String key) {
        namespacedKey = new NamespacedKey(plugin, key);
    }

    @Override
    public NamespacedKey getKey() {
        return namespacedKey;
    }

    /**
     * This will store the given {@link String} inside the NBT data of the given {@link Block}
     * 
     * @param b
     *            The {@link Block} in which to store the given value
     * @param value
     *            The value to store
     */
    public void setBlockData(@Nonnull Block b, @Nonnull String value) {
        Validate.notNull(b, "The block cannot be null!");
        Validate.notNull(value, "The value cannot be null!");

        /**
         * Don't use PaperLib here, it seems to be quite buggy in block-placing scenarios
         * and it would be too tedious to check for individual build versions to circumvent this.
         */
        BlockState state = b.getState();

        if (TILE_STATE != null && TILE_STATE.isInstance(state)) {
            try {
                PdcCompat.set(state, namespacedKey, "STRING", value);
                state.update();
            } catch (Exception x) {
                Slimefun.logger().log(Level.SEVERE, "Please check if your Server Software is up to date!");

                String serverSoftware = PaperLib.isSpigot() && !PaperLib.isPaper() ? "Spigot" : Bukkit.getName();
                Slimefun.logger().log(Level.SEVERE, () -> serverSoftware + " | " + Bukkit.getVersion() + " | " + Bukkit.getBukkitVersion());

                Slimefun.logger().log(Level.SEVERE, "An Exception was thrown while trying to set Persistent Data for a Block", x);
            }
        }
    }

    /**
     * This method returns the NBT data previously stored inside this {@link Block}.
     * 
     * @param b
     *            The {@link Block} to retrieve data from
     * 
     * @return The stored value
     */
    public Optional<String> getBlockData(@Nonnull Block b) {
        Validate.notNull(b, "The block cannot be null!");

        BlockState state = PaperLib.getBlockState(b, false).getState();

        if (TILE_STATE != null && TILE_STATE.isInstance(state)) {
            return Optional.ofNullable((String) PdcCompat.get(state, namespacedKey, "STRING"));
        } else {
            return Optional.empty();
        }
    }

    /**
     * This method checks whether the given {@link Material} is a Tile Entity.
     * This is used to determine whether the {@link Block} produced by this {@link Material}
     * produces a {@link TileState}, making it useable as a {@link PersistentDataHolder}.
     * 
     * Due to {@link Block#getState()} being a very expensive call performance-wise though,
     * this simple lookup method is used instead.
     * 
     * @param type
     *            The {@link Material} to check for
     * 
     * @return Whether the given {@link Material} is considered a Tile Entity
     */
    public boolean isTileEntity(@Nullable Material type) {
        if (type == null || MaterialCompat.isAir(type)) {
            // Cannot store data on air
            return false;
        }

        return SlimefunTag.TILE_ENTITIES.isTagged(type);
    }

}
