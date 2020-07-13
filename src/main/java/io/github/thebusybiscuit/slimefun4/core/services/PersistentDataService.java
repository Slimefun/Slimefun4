package io.github.thebusybiscuit.slimefun4.core.services;

import java.util.Optional;

import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.persistence.PersistentDataType;

import io.github.thebusybiscuit.slimefun4.api.MinecraftVersion;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;

/**
 * This interface is used to defer calls to Persistent Data and make sure they are only called
 * if the {@link MinecraftVersion} supports it.
 * 
 * @author TheBusyBiscuit
 *
 */
interface PersistentDataService {

    default void setString(Object obj, NamespacedKey key, String value) {
        if (SlimefunPlugin.getMinecraftVersion().isAtLeast(MinecraftVersion.MINECRAFT_1_14) && obj instanceof PersistentDataHolder) {
            PersistentDataContainer container = ((PersistentDataHolder) obj).getPersistentDataContainer();
            container.set(key, PersistentDataType.STRING, value);
        }
    }

    default Optional<String> getString(Object obj, NamespacedKey key) {
        if (SlimefunPlugin.getMinecraftVersion().isAtLeast(MinecraftVersion.MINECRAFT_1_14) && obj instanceof PersistentDataHolder) {
            PersistentDataContainer container = ((PersistentDataHolder) obj).getPersistentDataContainer();
            return Optional.ofNullable(container.get(key, PersistentDataType.STRING));
        }

        return Optional.empty();
    }

}
