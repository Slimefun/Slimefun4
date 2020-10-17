package io.github.thebusybiscuit.slimefun4.core.services;

import java.util.Optional;

import org.bukkit.Keyed;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.TileState;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.plugin.Plugin;

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

    public BlockDataService(Plugin plugin, String key) {
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
    public void setBlockData(Block b, String value) {
        BlockState state = b.getState();

        if (state instanceof TileState) {
            setString((TileState) state, namespacedKey, value);
            state.update();
        }
    }

    /**
     * This method returns the NBT data previously stored inside this {@link Block}.
     * 
     * @param b
     *            The {@link Block} to retrieve data from
     * @return The stored value
     */
    public Optional<String> getBlockData(Block b) {
        BlockState state = b.getState();

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
     * @return Whether the given {@link Material} is considered a Tile Entity
     */
    public boolean isTileEntity(Material type) {
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
            return true;
        default:
            return false;
        }
    }

}
