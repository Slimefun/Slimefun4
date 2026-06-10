package org.bukkit;

import org.bukkit.plugin.Plugin;

/**
 * Compile-only shadow stub of Bukkit's {@code org.bukkit.NamespacedKey} (introduced in MC 1.12).
 * <p>
 * Slimefun's core compiles against the 1.8.8 API floor, where this type does not exist. This stub
 * exposes only the surface that core uses so the code compiles. It is supplied through a
 * {@code compileOnly} dependency and is <strong>not</strong> shaded into the jar: on 1.12+ servers
 * the server's real {@code NamespacedKey} is used at runtime. On 1.8&ndash;1.11 the type is absent at
 * runtime, so any code path touching it must be guarded by the version layer.
 * <p>
 * The method and constructor signatures mirror Bukkit's exactly so bytecode compiled against this
 * stub binds to the real class at runtime.
 */
public final class NamespacedKey {

    private final String namespace;
    private final String key;

    public NamespacedKey(Plugin owner, String key) {
        this.namespace = owner == null ? "minecraft" : owner.getName().toLowerCase();
        this.key = key;
    }

    public NamespacedKey(String namespace, String key) {
        this.namespace = namespace;
        this.key = key;
    }

    public static NamespacedKey minecraft(String key) {
        return new NamespacedKey("minecraft", key);
    }

    public String getNamespace() {
        return namespace;
    }

    public String getKey() {
        return key;
    }

    @Override
    public int hashCode() {
        return (namespace + ":" + key).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof NamespacedKey)) {
            return false;
        }

        NamespacedKey other = (NamespacedKey) obj;
        return namespace.equals(other.namespace) && key.equals(other.key);
    }

    @Override
    public String toString() {
        return namespace + ":" + key;
    }
}
