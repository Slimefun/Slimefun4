package org.bukkit.event.inventory;

import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

/**
 * Compile-only stub for {@code org.bukkit.event.inventory.PrepareSmithingEvent} (Minecraft 1.16+). Not
 * shaded; on modern servers the real concrete class is used at runtime. Stubbed as a class extending
 * the real {@link InventoryEvent} (present on 1.8); {@code getInventory()} is inherited and
 * {@code setResult(ItemStack)} mirrors the real type (from {@code PrepareInventoryResultEvent}).
 */
public class PrepareSmithingEvent extends InventoryEvent {

    public PrepareSmithingEvent(InventoryView view) {
        super(view);
    }

    public void setResult(ItemStack result) {
        // no-op stub
    }
}
