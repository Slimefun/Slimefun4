package io.github.thebusybiscuit.slimefun5.utils.compatibility;

import javax.annotation.Nullable;

import org.bukkit.inventory.meta.ItemMeta;

/**
 * Java-8 universal port: reflective access to {@link ItemMeta} methods that do not exist at the 1.8.8
 * compile floor.
 * <p>
 * {@code setUnbreakable}/{@code isUnbreakable} are 1.11+ (on 1.8 the flag lived on the {@code Spigot}
 * sub-API) and the custom-model-data accessors are 1.14+. All are reached reflectively so the code
 * compiles against 1.8.8 while keeping full behaviour on modern servers; reads degrade to sensible
 * defaults on legacy.
 */
public final class ItemMetaCompat {

    private ItemMetaCompat() {}

    public static void setUnbreakable(ItemMeta meta, boolean unbreakable) {
        ReflectionCompat.invoke(meta, "setUnbreakable", unbreakable);
    }

    public static boolean isUnbreakable(ItemMeta meta) {
        return Boolean.TRUE.equals(ReflectionCompat.invoke(meta, "isUnbreakable"));
    }

    public static boolean hasCustomModelData(ItemMeta meta) {
        return Boolean.TRUE.equals(ReflectionCompat.invoke(meta, "hasCustomModelData"));
    }

    public static int getCustomModelData(ItemMeta meta) {
        Object result = ReflectionCompat.invoke(meta, "getCustomModelData");
        return result instanceof Integer ? (Integer) result : 0;
    }

    public static void setCustomModelData(ItemMeta meta, @Nullable Integer data) {
        ReflectionCompat.invoke(meta, "setCustomModelData", data);
    }
}
