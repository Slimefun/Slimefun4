package io.github.thebusybiscuit.slimefun4.core.services;

import java.util.Optional;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.TileState;
import org.bukkit.plugin.Plugin;

import io.github.thebusybiscuit.cscorelib2.data.PersistentDataAPI;

public class BlockDataService {

    private final NamespacedKey namespacedKey;

    public BlockDataService(Plugin plugin, String key) {
        namespacedKey = new NamespacedKey(plugin, key);
    }

    public void setBlockData(Block b, String value) {
        TileState tileEntity = (TileState) b.getState();
        PersistentDataAPI.setString(tileEntity, namespacedKey, value);
        tileEntity.update();
    }

    public Optional<String> getBlockData(Block b) {
        return PersistentDataAPI.getOptionalString((TileState) b.getState(), namespacedKey);
    }

    public boolean isTileEntity(Material type) {
        switch (type) {
        case PLAYER_HEAD:
        case PLAYER_WALL_HEAD:
        case CHEST:
        case DISPENSER:
        case DROPPER:
            return true;
        default:
            return false;
        }
    }

}
