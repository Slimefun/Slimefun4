package io.github.thebusybiscuit.slimefun4.implementation.items.electric.gadgets;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import io.github.thebusybiscuit.slimefun4.api.items.ItemSetting;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;

class MultiToolMode {

    private final int id;
    private final String itemId;
    private final ItemSetting<String> item;
    private final ItemSetting<Boolean> enabled;

    // TODO: Move "id" into some NamespacedKey
    MultiToolMode(@Nonnull MultiTool multiTool, int id, @Nonnull String itemId) {
        this.id = id;
        this.itemId = itemId;
        this.item = new ItemSetting<>(multiTool, "mode." + id + ".item", itemId);
        this.enabled = new ItemSetting<>(multiTool, "mode." + id + ".enabled", true);

        multiTool.addItemSetting(item, enabled);
    }

    /**
     * This method is deprecated and should not be used.
     *
     *
     * @return The ID of this mode
     */
    @Deprecated(since = "RC-37", forRemoval = true)
    public int getId() {
        return id;
    }

    @Nullable
    SlimefunItem getItem() {
        return SlimefunItem.getById(item.getValue());
    }

    @Nonnull
    String getItemId() {
        return itemId;
    }

    boolean isEnabled() {
        return enabled.getValue();
    }
}