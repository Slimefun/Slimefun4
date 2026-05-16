package io.github.thebusybiscuit.slimefun5.utils.compatibility;

import java.lang.reflect.Field;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.meta.ItemMeta;

import io.github.thebusybiscuit.slimefun5.api.MinecraftVersion;
import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;

public class VersionedItemFlag {
    
    public static final @Nullable ItemFlag HIDE_ADDITIONAL_TOOLTIP;

    // HIDE_ATTRIBUTES and HIDE_ENCHANTS were removed in 1.21.5+ in favor of
    // the tooltip_display component. We resolve them via reflection so the
    // plugin does not crash on newer versions where these constants no longer exist.
    public static final @Nullable ItemFlag HIDE_ATTRIBUTES;
    public static final @Nullable ItemFlag HIDE_ENCHANTS;

    static {
        MinecraftVersion version = Slimefun.getMinecraftVersion();

        if (version != null && version.isAtLeast(MinecraftVersion.MINECRAFT_1_20_5)) {
            ItemFlag flag = getKey("HIDE_ADDITIONAL_TOOLTIP");
            HIDE_ADDITIONAL_TOOLTIP = flag != null ? flag : getKey("HIDE_POTION_EFFECTS");
        } else {
            HIDE_ADDITIONAL_TOOLTIP = getKey("HIDE_POTION_EFFECTS");
        }

        HIDE_ATTRIBUTES = getKey("HIDE_ATTRIBUTES");
        HIDE_ENCHANTS = getKey("HIDE_ENCHANTS");
    }

    // Safely adds ItemFlags to ItemMeta, skipping null flags that don't exist in the current Minecraft version
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

