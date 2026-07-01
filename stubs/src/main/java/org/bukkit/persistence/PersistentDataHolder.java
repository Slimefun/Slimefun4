package org.bukkit.persistence;

/**
 * Compile-only stub for {@code org.bukkit.persistence.PersistentDataHolder} (Minecraft 1.14+). Not
 * shaded; on modern servers the real interface (implemented by ItemMeta, TileState, Entity, etc.) is
 * used at runtime. Code casts a holder to this type before reading its container.
 */
public interface PersistentDataHolder {

    PersistentDataContainer getPersistentDataContainer();
}
