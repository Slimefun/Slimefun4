package io.github.thebusybiscuit.slimefun5.utils.compatibility;

import java.lang.reflect.Constructor;

import javax.annotation.Nullable;

import io.github.thebusybiscuit.slimefun5.libraries.keys.NamespacedKey;

/**
 * Java-8 universal port: bridges Slimefun's own {@link NamespacedKey} to the real
 * {@code org.bukkit.NamespacedKey} at the (1.12+/1.14+) server-API boundaries (PDC, registries, recipe
 * lookups). Everything is reflective so no {@code org.bukkit.NamespacedKey} type reference exists in the
 * bytecode; on servers without it (1.8&ndash;1.11) the boundaries are never reached and this returns
 * {@code null}.
 */
public final class BukkitKeys {

    private BukkitKeys() {}

    /**
     * Converts an own {@link NamespacedKey} to a real {@code org.bukkit.NamespacedKey} instance.
     *
     * @return the real key as an {@link Object}, or {@code null} if the type is absent (legacy) or
     *         construction failed
     */
    @Nullable
    public static Object toBukkit(@Nullable NamespacedKey key) {
        if (key == null) {
            return null;
        }

        Class<?> bukkitKey;
        try {
            bukkitKey = Class.forName("org.bukkit.NamespacedKey");
        } catch (Throwable ignored) {
            return null;
        }

        // 1.14-1.20 expose the (String, String) constructor; 1.21+/26.x removed it, so fall back to
        // the fromString("namespace:key") factory.
        try {
            Constructor<?> ctor = bukkitKey.getDeclaredConstructor(String.class, String.class);
            ctor.setAccessible(true);
            return ctor.newInstance(key.getNamespace(), key.getKey());
        } catch (Throwable ignored) {
            // fall through
        }

        try {
            return bukkitKey.getMethod("fromString", String.class).invoke(null, key.getNamespace() + ":" + key.getKey());
        } catch (Throwable ignored) {
            return null;
        }
    }
}
