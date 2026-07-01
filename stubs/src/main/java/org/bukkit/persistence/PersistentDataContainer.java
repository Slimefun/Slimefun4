package org.bukkit.persistence;

import org.bukkit.NamespacedKey;

/**
 * Compile-only stub for {@code org.bukkit.persistence.PersistentDataContainer} (Minecraft 1.14+). Not
 * shaded; on modern servers the real interface is used at runtime. Method signatures match the real
 * type so virtual calls resolve via {@code invokeinterface}.
 */
public interface PersistentDataContainer {

    <T, Z> void set(NamespacedKey key, PersistentDataType<T, Z> type, Z value);

    <T, Z> boolean has(NamespacedKey key, PersistentDataType<T, Z> type);

    <T, Z> Z get(NamespacedKey key, PersistentDataType<T, Z> type);

    <T, Z> Z getOrDefault(NamespacedKey key, PersistentDataType<T, Z> type, Z defaultValue);

    void remove(NamespacedKey key);
}
