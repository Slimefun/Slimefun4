package io.github.thebusybiscuit.slimefun4.utils.compatibility;

import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.attribute.Attribute;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Compatibility layer for {@link Attribute} constants that were renamed
 * when {@link Attribute} changed from an enum to an interface in Minecraft 1.21.2+.
 *
 * <p>In 1.21.2+, {@code Attribute.GENERIC_MAX_HEALTH} was renamed to
 * {@code Attribute.MAX_HEALTH}. Using the registry-based lookup ensures
 * compatibility across both old and new API versions.</p>
 */
public class VersionedAttribute {

    /**
     * The max health attribute.
     * Was {@code GENERIC_MAX_HEALTH} pre-1.21.2, renamed to {@code MAX_HEALTH} in 1.21.2+.
     */
    public static final Attribute MAX_HEALTH;

    static {
        // Registry key is "generic.max_health" across all versions
        MAX_HEALTH = getKey("generic.max_health");
    }

    @Nullable
    private static Attribute getKey(@Nonnull String key) {
        return Registry.ATTRIBUTE.get(NamespacedKey.minecraft(key));
    }
}
