package io.github.thebusybiscuit.slimefun4.core.services;

import java.util.Optional;

import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.persistence.PersistentDataType;

import io.github.thebusybiscuit.cscorelib2.data.PersistentDataAPI;
import io.github.thebusybiscuit.slimefun4.api.MinecraftVersion;

/**
 * This interface is used to defer calls to Persistent Data and make sure they are only called
 * if the {@link MinecraftVersion} supports it.
 * 
 * @author TheBusyBiscuit
 * 
 * @deprecated This is redundant, we can use {@link PersistentDataAPI} instead.
 *
 */
@Deprecated
interface PersistentDataService {

    default void setString(Object obj, NamespacedKey key, String value) {
        PersistentDataContainer container = ((PersistentDataHolder) obj).getPersistentDataContainer();
        container.set(key, PersistentDataType.STRING, value);
    }

    default Optional<String> getString(Object obj, NamespacedKey key) {
        PersistentDataContainer container = ((PersistentDataHolder) obj).getPersistentDataContainer();
        return Optional.ofNullable(container.get(key, PersistentDataType.STRING));
    }

}
