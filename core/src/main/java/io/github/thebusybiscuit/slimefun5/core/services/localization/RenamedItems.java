package io.github.thebusybiscuit.slimefun5.core.services.localization;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun5.libraries.keys.NamespacedKey;
import io.github.thebusybiscuit.slimefun5.utils.compatibility.NbtItemCompat;
import io.github.thebusybiscuit.slimefun5.utils.compatibility.PdcCompat;

/**
 * Marks a {@link SlimefunItem} {@link ItemStack} as having been given a custom name by a player (via an
 * anvil rename). The translation layer honours this marker by leaving the item's display name untouched -
 * only its lore is still translated - so a player's rename is never overwritten by the per-viewer name
 * rewrite or the server-default name baked into the template.
 * <p>
 * Version-safe like {@link io.github.thebusybiscuit.slimefun5.core.services.CustomItemDataService}: modern
 * servers use the persistent-data container, 1.8&ndash;1.13 fall back to real item NBT.
 */
public final class RenamedItems {

    private RenamedItems() {}

    private static final String MARKER = "1";

    private static NamespacedKey key;

    @Nonnull
    private static NamespacedKey key() {
        if (key == null) {
            key = new NamespacedKey(Slimefun.instance(), "renamed");
        }

        return key;
    }

    /** Marks the given meta as carrying a player-applied custom name. */
    public static void mark(@Nonnull ItemMeta meta) {
        if (PdcCompat.isSupported()) {
            PdcCompat.setString(meta, key(), MARKER);
        } else {
            NbtItemCompat.setString(meta, key().toString(), MARKER);
        }
    }

    /** @return whether the given meta was marked as player-renamed. */
    public static boolean isRenamed(@Nullable ItemMeta meta) {
        if (meta == null) {
            return false;
        }

        String value = PdcCompat.isSupported()
            ? PdcCompat.getString(meta, key())
            : NbtItemCompat.getString(meta, key().toString());

        return MARKER.equals(value);
    }

    /** @return whether the given item was marked as player-renamed. */
    public static boolean isRenamed(@Nullable ItemStack item) {
        return item != null && item.hasItemMeta() && isRenamed(item.getItemMeta());
    }
}
