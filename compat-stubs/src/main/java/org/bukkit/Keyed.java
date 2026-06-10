package org.bukkit;

/**
 * Compile-only shadow stub of Bukkit's {@code org.bukkit.Keyed} (introduced in MC 1.12).
 * <p>
 * See {@link NamespacedKey} for the rationale. This is a {@code compileOnly} dependency and is not
 * shaded into the jar. Note: classes that {@code implements Keyed} will fail to class-load on
 * 1.8&ndash;1.11 servers (the interface is absent at runtime); those are handled in the version layer.
 */
public interface Keyed {

    NamespacedKey getKey();
}
