package io.github.thebusybiscuit.slimefun5.libraries.keys;

import org.bukkit.plugin.Plugin;

/**
 * Java-8 universal port: an own, relocated stand-in for {@code org.bukkit.NamespacedKey} (which only
 * exists on MC 1.12+).
 * <p>
 * Slimefun's identity system uses namespaced keys pervasively, in always-loaded class signatures
 * (many types {@code implements Keyed}). Bukkit's {@code PluginClassLoader} refuses to load
 * {@code org.bukkit.*} classes shipped inside a plugin jar, so the real {@code NamespacedKey} cannot be
 * provided on 1.8&ndash;1.11. This own type lives in Slimefun's own package, is bundled into the jar,
 * and is therefore loadable on every version. It is a plain {@code (namespace, key)} identity pair;
 * conversion to the real {@code org.bukkit.NamespacedKey} happens only at the few server-API boundaries
 * (PDC, registries, recipe lookups) via {@code BukkitKeys}, all of which are 1.12+/1.14+ only.
 *
 * <p>
 * The public surface mirrors {@code org.bukkit.NamespacedKey} so call sites read identically after the
 * import swap.
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
