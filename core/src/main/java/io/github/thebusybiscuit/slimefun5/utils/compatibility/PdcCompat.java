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

    private static final boolean SUPPORTED = resolveSupported();

    /**
     * @return whether this server exposes the 1.14+ PersistentDataContainer API.
     */
    public static boolean isSupported() {
        return SUPPORTED;
    }

    private static boolean resolveSupported() {
        try {
            Class.forName("org.bukkit.persistence.PersistentDataContainer");
            return true;
        } catch (Throwable ignored) {
            return false;
        }
    }

    // --- Legacy (pre-1.14) fallback ---
    // The PersistentDataContainer API doesn't exist before 1.14, so the reflective calls below resolve
    // to null and do nothing. For holders with a stable id (players/entities) we instead persist data
    // in a YAML keyed by UUID, so e.g. the player's chosen guide language actually sticks on 1.8.
    // Item holders have no id here and keep the no-op (their data flows through item-NBT paths).
    private static org.bukkit.configuration.file.YamlConfiguration legacyStore;
    private static java.io.File legacyFile;

    private static synchronized org.bukkit.configuration.file.YamlConfiguration legacy() {
        if (legacyStore == null) {
            legacyFile = new java.io.File(io.github.thebusybiscuit.slimefun5.implementation.Slimefun.instance().getDataFolder(), "legacy-pdc.yml");
            legacyStore = org.bukkit.configuration.file.YamlConfiguration.loadConfiguration(legacyFile);
        }
        return legacyStore;
    }

    @Nullable
    private static String legacyPath(Object holder, NamespacedKey key) {
        Object id = ReflectionCompat.invoke(holder, "getUniqueId");
        return id != null ? id + "." + key.toString().replace(':', '_').replace('.', '_') : null;
    }

    private static synchronized void legacySave() {
        try {
            if (legacyFile != null) {
                legacyStore.save(legacyFile);
            }
        } catch (Exception ignored) {
            // Best-effort persistence; never break a data write on a save failure.
        }
    }

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
            return;
        }

        String path = legacyPath(holder, key);
        if (path != null) {
            legacy().set(path, value);
            legacySave();
        }
    }

    @Nullable
    public static Object get(Object holder, NamespacedKey key, String typeName) {
        Object c = container(holder);
        Object t = dataType(typeName);
        Object k = BukkitKeys.toBukkit(key);

        if (c != null && t != null && k != null) {
            return ReflectionCompat.invoke(c, "get", k, t);
        }

        String path = legacyPath(holder, key);
        return path != null ? legacy().get(path) : null;
    }

    public static Object getOrDefault(Object holder, NamespacedKey key, String typeName, Object defaultValue) {
        Object value = get(holder, key, typeName);
        return value != null ? value : defaultValue;
    }

    public static boolean has(Object holder, NamespacedKey key, String typeName) {
        Object c = container(holder);
        Object t = dataType(typeName);
        Object k = BukkitKeys.toBukkit(key);

        if (c != null && t != null && k != null) {
            return Boolean.TRUE.equals(ReflectionCompat.invoke(c, "has", k, t));
        }

        String path = legacyPath(holder, key);
        return path != null && legacy().contains(path);
    }

    /**
     * Version-safe equality of two holders' PersistentDataContainers. On servers without PDC
     * (1.8&ndash;1.13) the containers resolve to {@code null}; two such holders are treated as equal.
     */
    public static boolean containersEqual(Object holderA, Object holderB) {
        Object a = container(holderA);
        Object b = container(holderB);

        if (a == null || b == null) {
            return a == b;
        }

        return a.equals(b);
    }

    public static void remove(Object holder, NamespacedKey key) {
        Object c = container(holder);
        Object k = BukkitKeys.toBukkit(key);

        if (c != null && k != null) {
            ReflectionCompat.invoke(c, "remove", k);
            return;
        }

        String path = legacyPath(holder, key);
        if (path != null) {
            legacy().set(path, null);
            legacySave();
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
