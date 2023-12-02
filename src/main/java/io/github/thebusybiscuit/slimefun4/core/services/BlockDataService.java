package io.github.thebusybiscuit.slimefun4.core.services;

import java.util.Optional;
import java.util.logging.Level;

import javax.annotation.Nonnull;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import com.jeff_media.customblockdata.CustomBlockData;

import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.papermc.lib.PaperLib;

/**
 * The {@link BlockDataService} is similar to the {@link CustomItemDataService},
 * it is responsible for storing NBT data inside {@link CustomBlockData}.
 * 
 * This is used to speed up performance and prevent blockstate from reverting to vanilla.
 * 
 * @author TheBusyBiscuit
 * @author test137e29b
 *
 */
public class BlockDataService {

    private final Plugin plugin;
    /**
     * This creates a new {@link BlockDataService} for the given {@link Plugin}.
     * The {@link Plugin} and provided key will together form a {@link NamespacedKey} used to store
     * data in {@link CustomBlockData}.
     * 
     * @param plugin
     * The {@link Plugin} responsible for this service
     */
    public BlockDataService(@Nonnull Plugin plugin) {
        this.plugin = plugin;
    }

    /**
     * This will store the given {@link String} inside the NBT data of the given {@link Block}
     * 
     * @param b
     * The {@link Block} in which to store the given value
     * @param key
     * The key to store
     * @param value
     * The value to store
     */
    public void setBlockData(@Nonnull Block b, @Nonnull String key, @Nonnull String value) {
        Validate.notNull(b, "The block cannot be null!");
        Validate.notNull(key, "The key cannot be null!");
        Validate.notNull(value, "The value cannot be null!");

        try {
            CustomBlockData blockData = new CustomBlockData(b, plugin);
            NamespacedKey namespacedKey = new NamespacedKey(plugin, key);
            blockData.set(namespacedKey, PersistentDataType.STRING, value);
        } catch (Exception x) {
            Slimefun.logger().log(Level.SEVERE, "Please check if your Server Software is up to date!");

            String serverSoftware = PaperLib.isSpigot() && !PaperLib.isPaper() ? "Spigot" : Bukkit.getName();
            Slimefun.logger().log(Level.SEVERE, () -> serverSoftware + " | " + Bukkit.getVersion() + " | " + Bukkit.getBukkitVersion());

            Slimefun.logger().log(Level.SEVERE, "An Exception was thrown while trying to set Persistent Data for a Block", x);
        }
        
    }

    /**
     * This method returns the NBT data previously stored inside this {@link Block}.
     * 
     * @param b
     * The {@link Block} to retrieve data form
     * @param key
     * The key to retrieve
     * 
     * @return The stored value
     */
    public Optional<String> getBlockData(@Nonnull Block b, @Nonnull String key) {
        Validate.notNull(b, "The block cannot be null!");
        Validate.notNull(key, "The key cannot be null!");

        CustomBlockData blockData = new CustomBlockData(b, plugin);
        NamespacedKey namespacedKey = new NamespacedKey(plugin, key);

        if (blockData.has(namespacedKey)) {
            return Optional.of(blockData.get(namespacedKey, PersistentDataType.STRING));
        } else {
            return Optional.empty();
        }
    }

    public void moveBlockData(@Nonnull Block from, @Nonnull Block to) {
        Validate.notNull(from, "The block cannot be null!");
        Validate.notNull(to, "The block cannot be null!");

        CustomBlockData fromData = new CustomBlockData(from, plugin);

        fromData.copyTo(to, plugin);
        fromData.clear();
    }

    public void clearBlockData(@Nonnull Block b) {
        Validate.notNull(b, "The block cannot be null!");

        CustomBlockData blockData = new CustomBlockData(b, plugin);
        blockData.clear();
    }
}