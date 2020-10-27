package io.github.thebusybiscuit.slimefun4.core.services;

import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.Keyed;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.TileState;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.plugin.Plugin;

import io.papermc.lib.PaperLib;
import io.papermc.lib.features.blockstatesnapshot.BlockStateSnapshotResult;

/**
 * The {@link BlockDataService} is similar to the {@link CustomItemDataService},
 * it is responsible for storing NBT data inside a {@link TileState}.
 * 
 * This is used to speed up performance and prevent
 * 
 * @author TheBusyBiscuit
 *
 */
public class BlockDataService implements PersistentDataService, Keyed {

    private final NamespacedKey namespacedKey;

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
        BlockStateSnapshotResult result = PaperLib.getBlockState(b, false);
        BlockState state = result.getState();

        if (state instanceof TileState) {
            setString((TileState) state, namespacedKey, value);

            if (result.isSnapshot()) {
                state.update();
            }
        }
    }

    /**
     * This method returns the NBT data previously stored inside this {@link Block}.
     * 
     * @param b
     *            The {@link Block} to retrieve data from
     * @return The stored value
     */
    public Optional<String> getBlockData(@Nonnull Block b) {
        BlockState state = PaperLib.getBlockState(b, false).getState();

        if (state instanceof TileState) {
            return getString((TileState) state, namespacedKey);
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
        if (type == null || type.isAir()) {
            // Cannot store data on air
            return false;
        }

        switch (type) {
        case PLAYER_HEAD:
        case PLAYER_WALL_HEAD:
        case CHEST:
        case DISPENSER:
        case BREWING_STAND:
        case DROPPER:
        case FURNACE:
        case BLAST_FURNACE:
        case HOPPER:
        case LECTERN:
        case JUKEBOX:
        case ENDER_CHEST:
        case ENCHANTING_TABLE:
        case DAYLIGHT_DETECTOR:
        case SMOKER:
        case BARREL:
        case SPAWNER:
        case BEACON:
            // All of the above Materials are Tile Entities
            return true;
        default:
            // Otherwise we assume they're not Tile Entities
            return false;
        }
    }

}
