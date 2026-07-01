package org.bukkit.event.inventory;

import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.inventory.InventoryView;

/**
 * Compile-only stub for {@code org.bukkit.event.inventory.SmithItemEvent} (Minecraft 1.16+). Not
 * shaded; on modern servers the real concrete class is used at runtime. Stubbed as a class extending
 * the real {@link InventoryClickEvent} (present on 1.8); the methods Slimefun uses (getInventory,
 * setResult, getWhoClicked) are inherited.
 */
public class SmithItemEvent extends InventoryClickEvent {

    public SmithItemEvent(InventoryView view, SlotType type, int slot, ClickType click, InventoryAction action) {
        super(view, type, slot, click, action);
    }
}
