package io.github.thebusybiscuit.slimefun4.utils.compatibility;

import java.lang.reflect.Field;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.meta.ItemMeta;

import io.github.thebusybiscuit.slimefun4.api.MinecraftVersion;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;

public class VersionedItemFlag {
    
    public static final @Nullable ItemFlag HIDE_ADDITIONAL_TOOLTIP;

    // HIDE_ATTRIBUTES and HIDE_ENCHANTS were removed in 1.21.5+ in favor of
    // the tooltip_display component. We resolve them via reflection so the
    // plugin does not crash on newer versions where these constants no longer exist.
    public static final @Nullable ItemFlag HIDE_ATTRIBUTES;
    public static final @Nullable ItemFlag HIDE_ENCHANTS;

    static {
        MinecraftVersion version = Slimefun.getMinecraftVersion();

        // In 1.20.5+ HIDE_POTION_EFFECTS was renamed to HIDE_ADDITIONAL_TOOLTIP.
        // In 1.21.5+ HIDE_ADDITIONAL_TOOLTIP was removed in favor of tooltip_display component.
        // We use reflection for both paths to be safe across all versions.
        if (version.isAtLeast(MinecraftVersion.MINECRAFT_1_20_5)) {
            ItemFlag flag = getKey("HIDE_ADDITIONAL_TOOLTIP");
            HIDE_ADDITIONAL_TOOLTIP = flag != null ? flag : getKey("HIDE_POTION_EFFECTS");
        } else {
            HIDE_ADDITIONAL_TOOLTIP = getKey("HIDE_POTION_EFFECTS");
        }

        HIDE_ATTRIBUTES = getKey("HIDE_ATTRIBUTES");
        HIDE_ENCHANTS = getKey("HIDE_ENCHANTS");
    }

    /**
     * Safely adds {@link ItemFlag}s to the given {@link ItemMeta}, skipping any
     * {@code null} flags. This is necessary because some flags may not exist on
     * the running Minecraft version (e.g. HIDE_ATTRIBUTES was removed in 1.21.5).
     *
     * @param meta  The {@link ItemMeta} to add flags to
     * @param flags The flags to add (nulls are silently ignored)
     */
    public static void addFlags(@Nonnull ItemMeta meta, @Nullable ItemFlag... flags) {
        for (ItemFlag flag : flags) {
            if (flag != null) {
                meta.addItemFlags(flag);
            }
        }
    }

    @Nullable
    private static ItemFlag getKey(@Nonnull String key) {
        try {
            Field field = ItemFlag.class.getDeclaredField(key);
            return (ItemFlag) field.get(null);
        } catch(Exception e) {
            return null;
        }
    }
}
