package io.github.thebusybiscuit.slimefun5.utils.compatibility;

import javax.annotation.Nullable;

/**
 * Java-8 universal port: {@code Keyed#getKey()} (returning a {@code NamespacedKey}) is 1.13+. Older
 * types ({@code Enchantment}, {@code Biome}, ...) exposed only legacy names. These helpers read a
 * keyed object's namespace/key reflectively, falling back to {@code getName()}/{@code name()} on legacy
 * servers, so call sites that build config paths or log messages compile against the 1.8.8 floor.
 */
public final class KeyedCompat {

    private KeyedCompat() {}

    @Nullable
    private static Object namespacedKey(Object keyed) {
        return ReflectionCompat.invoke(keyed, "getKey");
    }

    public static String namespace(Object keyed) {
        Object key = namespacedKey(keyed);
        Object namespace = ReflectionCompat.invoke(key, "getNamespace");
        return namespace != null ? namespace.toString() : "minecraft";
    }

    public static String key(Object keyed) {
        Object key = namespacedKey(keyed);
        Object value = ReflectionCompat.invoke(key, "getKey");

        if (value != null) {
            return value.toString();
        }

        // Legacy fallback: getName() (e.g. Enchantment) or enum name().
        Object name = ReflectionCompat.invoke(keyed, "getName");

        if (name == null && keyed instanceof Enum) {
            name = ((Enum<?>) keyed).name();
        }

        return name != null ? name.toString().toLowerCase() : String.valueOf(keyed);
    }

    /**
     * The full {@code namespace:key} string, or a best-effort legacy name.
     */
    public static String keyName(Object keyed) {
        Object key = namespacedKey(keyed);

        if (key != null) {
            return key.toString();
        }

        return key(keyed);
    }
}
