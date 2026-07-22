package io.github.thebusybiscuit.slimefun4.api.items;

import javax.annotation.Nullable;

import org.bukkit.inventory.ItemStack;

/**
 * Backwards-compatibility shim for the pre-relocation Slimefun package.
 * <p>
 * This fork relocated Slimefun's API from {@code io.github.thebusybiscuit.slimefun4.*} to
 * {@code io.github.thebusybiscuit.slimefun5.*}. Third-party plugins - and libraries they shade, such as
 * ProjectUnified UniItem (used by ExtraStorage and others) - were compiled against the old package and
 * reference {@code io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem}, so they fail with
 * {@link NoClassDefFoundError} on this fork. This thin bridge re-exposes the minimal {@code SlimefunItem}
 * surface those integrations actually use, delegating every call to the real
 * {@link io.github.thebusybiscuit.slimefun5.api.items.SlimefunItem}.
 * <p>
 * Intentionally minimal: only the members third-party item-identification code depends on are provided
 * ({@link #getByItem(ItemStack)}, {@link #getById(String)}, {@link #getId()}, {@link #getItem()},
 * {@link #isItem(ItemStack)}). Extend it only when a real plugin needs more - do NOT mirror the whole API.
 */
public class SlimefunItem {

    private final io.github.thebusybiscuit.slimefun5.api.items.SlimefunItem delegate;

    private SlimefunItem(io.github.thebusybiscuit.slimefun5.api.items.SlimefunItem delegate) {
        this.delegate = delegate;
    }

    @Nullable
    public static SlimefunItem getByItem(@Nullable ItemStack item) {
        return wrap(io.github.thebusybiscuit.slimefun5.api.items.SlimefunItem.getByItem(item));
    }

    @Nullable
    public static SlimefunItem getById(@Nullable String id) {
        if (id == null) {
            return null;
        }

        return wrap(io.github.thebusybiscuit.slimefun5.api.items.SlimefunItem.getById(id));
    }

    @Nullable
    private static SlimefunItem wrap(@Nullable io.github.thebusybiscuit.slimefun5.api.items.SlimefunItem item) {
        return item == null ? null : new SlimefunItem(item);
    }

    public String getId() {
        return delegate.getId();
    }

    public ItemStack getItem() {
        return delegate.getItem();
    }

    public boolean isItem(@Nullable ItemStack item) {
        return delegate.isItem(item);
    }
}
