package io.github.thebusybiscuit.slimefun5.utils.compatibility;

import javax.annotation.Nullable;

import org.bukkit.Material;

/**
 * Java-8 universal port: cross-version {@link Material} helpers for APIs added after 1.8.8.
 *
 * @author Slimefun (Java-8 port)
 */
public final class MaterialCompat {

    private MaterialCompat() {}

    /**
     * Version-safe replacement for {@code Material#isAir()} (added in 1.13). Resolved by name so it
     * works on every version: {@code AIR} on all, plus {@code CAVE_AIR}/{@code VOID_AIR} on 1.13+.
     */
    public static boolean isAir(@Nullable Material material) {
        if (material == null) {
            return true;
        }

        switch (material.name()) {
            case "AIR":
            case "CAVE_AIR":
            case "VOID_AIR":
                return true;
            default:
                return false;
        }
    }

    /**
     * Reflective {@code Material#isInteractable()} (added in 1.13). Returns {@code false} on older
     * servers, where the distinction is unavailable.
     */
    public static boolean isInteractable(@Nullable Material material) {
        return Boolean.TRUE.equals(ReflectionCompat.invoke(material, "isInteractable"));
    }

    /**
     * Reflective {@code Material#isItem()} (1.13+). Returns {@code true} on older servers, where every
     * material can exist as an item.
     */
    public static boolean isItem(@Nullable Material material) {
        Object result = ReflectionCompat.invoke(material, "isItem");
        return result instanceof Boolean ? (Boolean) result : true;
    }

    /**
     * Reflective {@code Material#isLegacy()} (1.13+). Returns {@code false} on older servers, which have
     * no legacy-material concept.
     */
    public static boolean isLegacy(@Nullable Material material) {
        return Boolean.TRUE.equals(ReflectionCompat.invoke(material, "isLegacy"));
    }

    /**
     * Reflective {@code Material#isFuel()} (1.13+). Returns {@code false} on older servers, where the
     * distinction is unavailable.
     */
    public static boolean isFuel(@Nullable Material material) {
        return Boolean.TRUE.equals(ReflectionCompat.invoke(material, "isFuel"));
    }
}
