package io.github.thebusybiscuit.slimefun5.utils.compatibility;

import java.util.Optional;

import javax.annotation.Nullable;

import io.github.thebusybiscuit.slimefun5.libraries.keys.NamespacedKey;

/**
 * Java-8 universal port: a single, fully-reflective Persistent-Data (PDC) abstraction.
 * <p>
 * PDC is 1.14+. Rather than referencing {@code PersistentDataContainer}/{@code PersistentDataType}/
 * {@code PersistentDataHolder} (1.14+ types) in core bytecode, every operation here is reflective and
 * keyed by Slimefun's own {@link NamespacedKey} (converted to the real {@code org.bukkit.NamespacedKey}
 * via {@link BukkitKeys} only at the call into the server). On servers without PDC (1.8&ndash;1.13) the
 * container/type/key resolve to {@code null} and all operations degrade to no-ops / defaults, so callers
 * are inherently version-safe. {@code typeName} is a {@code PersistentDataType} field name such as
 * {@code "STRING"}, {@code "BYTE"}, {@code "INTEGER"}, {@code "LONG"} or {@code "FLOAT"}.
 */
public final class PdcCompat {

    private PdcCompat() {}

    @Nullable
    private static Object container(Object holder) {
        return ReflectionCompat.invoke(holder, "getPersistentDataContainer");
    }

    @Nullable
    private static Object dataType(String typeName) {
        try {
            return Class.forName("org.bukkit.persistence.PersistentDataType").getField(typeName).get(null);
        } catch (Throwable ignored) {
            return null;
        }
    }

    public static void set(Object holder, NamespacedKey key, String typeName, Object value) {
        Object c = container(holder);
        Object t = dataType(typeName);
        Object k = BukkitKeys.toBukkit(key);

        if (c != null && t != null && k != null) {
            ReflectionCompat.invoke(c, "set", k, t, value);
        }
    }

    @Nullable
    public static Object get(Object holder, NamespacedKey key, String typeName) {
        Object c = container(holder);
        Object t = dataType(typeName);
        Object k = BukkitKeys.toBukkit(key);

        if (c == null || t == null || k == null) {
            return null;
        }

        return ReflectionCompat.invoke(c, "get", k, t);
    }

    public static Object getOrDefault(Object holder, NamespacedKey key, String typeName, Object defaultValue) {
        Object value = get(holder, key, typeName);
        return value != null ? value : defaultValue;
    }

    public static boolean has(Object holder, NamespacedKey key, String typeName) {
        Object c = container(holder);
        Object t = dataType(typeName);
        Object k = BukkitKeys.toBukkit(key);

        if (c == null || t == null || k == null) {
            return false;
        }

        return Boolean.TRUE.equals(ReflectionCompat.invoke(c, "has", k, t));
    }

    public static void remove(Object holder, NamespacedKey key) {
        Object c = container(holder);
        Object k = BukkitKeys.toBukkit(key);

        if (c != null && k != null) {
            ReflectionCompat.invoke(c, "remove", k);
        }
    }

    // --- Convenience accessors mirroring the dough PersistentDataAPI usage Slimefun relied on ---

    public static void setString(Object holder, NamespacedKey key, String value) {
        set(holder, key, "STRING", value);
    }

    @Nullable
    public static String getString(Object holder, NamespacedKey key) {
        Object value = get(holder, key, "STRING");
        return value instanceof String ? (String) value : null;
    }

    public static Optional<String> getOptionalString(Object holder, NamespacedKey key) {
        return Optional.ofNullable(getString(holder, key));
    }

    public static void setByte(Object holder, NamespacedKey key, byte value) {
        set(holder, key, "BYTE", value);
    }

    public static byte getByte(Object holder, NamespacedKey key) {
        Object value = get(holder, key, "BYTE");
        return value instanceof Byte ? (Byte) value : 0;
    }

    public static boolean hasByte(Object holder, NamespacedKey key) {
        return has(holder, key, "BYTE");
    }

    public static void setInt(Object holder, NamespacedKey key, int value) {
        set(holder, key, "INTEGER", value);
    }

    public static int getInt(Object holder, NamespacedKey key) {
        return getInt(holder, key, 0);
    }

    public static int getInt(Object holder, NamespacedKey key, int defaultValue) {
        Object value = get(holder, key, "INTEGER");
        return value instanceof Integer ? (Integer) value : defaultValue;
    }
}
