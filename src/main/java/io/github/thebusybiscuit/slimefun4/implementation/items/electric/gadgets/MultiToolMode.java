package io.github.thebusybiscuit.slimefun4.implementation.items.electric.gadgets;

import io.github.thebusybiscuit.slimefun4.api.items.ItemSetting;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;

class MultiToolMode {

    private final ItemSetting<String> item;
    private final ItemSetting<Boolean> enabled;

    MultiToolMode(MultiTool multiTool, int id, String itemId) {
        this.item = new ItemSetting<>("mode." + id + ".item", itemId);
        this.enabled = new ItemSetting<>("mode." + id + ".enabled", true);

        multiTool.addItemSetting(item, enabled);
    }

    public SlimefunItem getItem() {
        return SlimefunItem.getByID(item.getValue());
    }

    public boolean isEnabled() {
        return enabled.getValue();
    }
}
