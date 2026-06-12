package io.github.thebusybiscuit.slimefun5.utils.compatibility;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Map;

import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Java-8 universal port: stores Slimefun's item id in real item NBT on pre-1.14 servers, where the
 * {@code PersistentDataContainer} API does not yet exist.
 * <p>
 * It writes a string tag into CraftMetaItem's {@code unhandledTags} map, which CraftBukkit persists
 * verbatim through {@code getItemMeta}/{@code setItemMeta}. Identity therefore survives renames and lore
 * edits, unlike a lore-based marker (the approach big 1.8 servers use). Everything is reflective; no
 * CraftBukkit/NMS type is referenced at compile time. On 1.14+ {@link PdcCompat} is used instead and this
 * class is never initialised.
 */
public final class NbtItemCompat {

    private static final Constructor<?> NBT_TAG_STRING_CTOR = resolveStringConstructor();

    private NbtItemCompat() {}

    public static boolean isSupported() {
        return NBT_TAG_STRING_CTOR != null;
    }

    public static boolean setString(@Nullable ItemMeta meta, String key, String value) {
        if (meta == null || NBT_TAG_STRING_CTOR == null) {
            return false;
        }

        try {
            Map<String, Object> tags = unhandledTags(meta);

            if (tags == null) {
                return false;
            }

            tags.put(key, NBT_TAG_STRING_CTOR.newInstance(value));
            return true;
        } catch (Throwable ignored) {
            return false;
        }
    }

    @Nullable
    public static String getString(@Nullable ItemMeta meta, String key) {
        if (meta == null) {
            return null;
        }

        try {
            Map<String, Object> tags = unhandledTags(meta);

            if (tags == null) {
                return null;
            }

            Object tag = tags.get(key);
            return tag == null ? null : extractString(tag);
        } catch (Throwable ignored) {
            return null;
        }
    }

    @Nullable
    @SuppressWarnings("unchecked")
    private static Map<String, Object> unhandledTags(ItemMeta meta) throws IllegalAccessException {
        Field field = findField(meta.getClass(), "unhandledTags");

        if (field == null) {
            return null;
        }

        field.setAccessible(true);
        return (Map<String, Object>) field.get(meta);
    }

    @Nullable
    private static String extractString(Object nbtTagString) throws IllegalAccessException {
        // NBTTagString wraps a single String field whose name is obfuscated across versions; find it by
        // type rather than by name.
        for (Field field : nbtTagString.getClass().getDeclaredFields()) {
            if (field.getType() == String.class) {
                field.setAccessible(true);
                return (String) field.get(nbtTagString);
            }
        }

        return null;
    }

    @Nullable
    private static Field findField(Class<?> type, String name) {
        for (Class<?> current = type; current != null; current = current.getSuperclass()) {
            try {
                return current.getDeclaredField(name);
            } catch (NoSuchFieldException ignored) {
                // Declared further up the hierarchy (CraftMetaItem) — keep walking.
            }
        }

        return null;
    }

    @Nullable
    private static Constructor<?> resolveStringConstructor() {
        try {
            String cbPackage = Bukkit.getServer().getClass().getPackage().getName();
            String version = cbPackage.substring(cbPackage.lastIndexOf('.') + 1);
            Constructor<?> ctor = Class.forName("net.minecraft.server." + version + ".NBTTagString").getConstructor(String.class);
            ctor.setAccessible(true);
            return ctor;
        } catch (Throwable ignored) {
            return null;
        }
    }
}
