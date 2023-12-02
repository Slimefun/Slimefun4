package io.github.thebusybiscuit.slimefun4.core.services;

import java.util.Optional;
import java.util.logging.Level;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.papermc.lib.PaperLib;

/**
 * The {@link ChunkDataService} is similar to the {@link CustomItemDataService},
 * it is responsible for storing NBT data inside a {@link Chunk}'s {@link PersistentDataContainer}.
 * 
 * This is used to speed up performance
 * 
 * @author test137e29b
 *
 */
public class ChunkDataService {

    private final Plugin plugin;

    /**
     * This creates a new {@link ChunkDataService} for the given {@link Plugin}.
     * The {@link Plugin} and key will together form a {@link NamespacedKey} used to store
     * data on a {@link Chunk}'s {@link PersistentDataContainer}.
     * 
     * @param plugin
     * The {@link Plugin} responsible for this service
     */
    public ChunkDataService(@Nonnull Plugin plugin) {
        this.plugin = plugin;
    }

    /**
     * This will store the given {@link String} inside the NBT data of the given {@link Chunk}
     * 
     * @param b
     * The {@link Chunk} in which to store the given value
     * @param key
     * The key to store
     * @param value
     * The value to store
     */
    public void setChunkData(@Nonnull Chunk c, @Nonnull String key, @Nonnull String value) {
        Validate.notNull(c, "The chunk cannot be null!");
        Validate.notNull(key, "The key cannot be null!");
        Validate.notNull(value, "The value cannot be null!");
    
        try {
            PersistentDataContainer container = c.getPersistentDataContainer();
            NamespacedKey namespacedKey = new NamespacedKey(plugin, key);
            container.set(namespacedKey, PersistentDataType.STRING, value);
        } catch (Exception x) {
            Slimefun.logger().log(Level.SEVERE, "Please check if your Server Software is up to date!");

            String serverSoftware = PaperLib.isSpigot() && !PaperLib.isPaper() ? "Spigot" : Bukkit.getName();
            Slimefun.logger().log(Level.SEVERE, () -> serverSoftware + " | " + Bukkit.getVersion() + " | " + Bukkit.getBukkitVersion());

            Slimefun.logger().log(Level.SEVERE, "An Exception was thrown while trying to set Persistent Data for a Chunk", x);
        }
    }

    /**
     * This method returns the NBT data previously stored inside this {@link Chunk}.
     * 
     * @param c
     * The {@link Chunk} to retrieve data from
     * @param key
     * The key to retrieve
     * 
     * @return The stored value
     */
    public @Nullable Optional<String> getChunkData(@Nonnull Chunk c, @Nonnull String key) {
        Validate.notNull(c, "The chunk cannot be null!");
        Validate.notNull(key, "The key cannot be null!");

        PersistentDataContainer container = c.getPersistentDataContainer();
        NamespacedKey namespacedKey = new NamespacedKey(plugin, key);

        if (container != null) {
            return Optional.ofNullable(container.get(namespacedKey, PersistentDataType.STRING));
        } else {
            return Optional.empty();
        }
    }

    /**
     * This method checks whether the given {@link Chunk} has the given key stored.
     * 
     * @param c
     * The {@link Chunk} to check
     * @param key
     * The key to check
     * 
     * @return Whether the {@link Chunk} has the given key stored
     */
    public boolean hasChunkData(@Nonnull Chunk c, @Nonnull String key) {
        Validate.notNull(c, "The chunk cannot be null!");
        Validate.notNull(key, "The key cannot be null!");

        PersistentDataContainer container = c.getPersistentDataContainer();
        NamespacedKey namespacedKey = new NamespacedKey(plugin, key);

        return container.has(namespacedKey, PersistentDataType.STRING);
    }
}