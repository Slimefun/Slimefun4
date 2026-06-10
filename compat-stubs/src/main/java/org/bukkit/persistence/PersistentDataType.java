package org.bukkit.persistence;

/**
 * Compile-only stub for {@code org.bukkit.persistence.PersistentDataType} (Minecraft 1.14+). Not
 * shaded; on modern servers the real interface and its constants are used at runtime. Only the typed
 * constants that Slimefun references are declared. The {@code null} initializers are never read at
 * runtime — consumers resolve the real static fields via {@code getstatic}.
 */
public interface PersistentDataType<P, C> {

    PersistentDataType<String, String> STRING = null;
    PersistentDataType<Byte, Byte> BYTE = null;
    PersistentDataType<Short, Short> SHORT = null;
    PersistentDataType<Integer, Integer> INTEGER = null;
    PersistentDataType<Long, Long> LONG = null;
    PersistentDataType<Float, Float> FLOAT = null;
    PersistentDataType<Double, Double> DOUBLE = null;
    PersistentDataType<byte[], byte[]> BYTE_ARRAY = null;
}
