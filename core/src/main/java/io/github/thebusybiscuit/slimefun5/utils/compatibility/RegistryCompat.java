package io.github.thebusybiscuit.slimefun5.utils.compatibility;

import javax.annotation.Nullable;

import org.bukkit.NamespacedKey;

/**
 * Java-8 universal port: {@code org.bukkit.Registry} (1.14+) does not exist at the 1.8.8 compile floor,
 * so it cannot be referenced as a type. This helper resolves {@code Registry.<FIELD>.get(NamespacedKey)}
 * entirely through reflection (no {@code Registry} type anywhere), returning {@code null} on versions
 * that lack the registry. Callers supply a legacy fallback (e.g. {@code Enchantment#getByName}).
 */
public final class RegistryCompat {

    private RegistryCompat() {}

    @Nullable
    public static Object get(String registryFieldName, String key) {
        try {
            Class<?> registryClass = Class.forName("org.bukkit.Registry");
            Object registry = registryClass.getField(registryFieldName).get(null);

            // NamespacedKey resolves to the server's real class at runtime (the stub is compile-only);
            // on servers without it (<1.12) this throws and we fall through to null.
            Object namespacedKey = NamespacedKey.minecraft(key);
            Class<?> nskClass = Class.forName("org.bukkit.NamespacedKey");

            return registryClass.getMethod("get", nskClass).invoke(registry, namespacedKey);
        } catch (Throwable ignored) {
            return null;
        }
    }
}
