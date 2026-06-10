package io.github.thebusybiscuit.slimefun5.utils.compatibility;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * Java-8 universal port: {@code Inventory#getStorageContents()} is 1.9+. Reached reflectively, falling
 * back to {@code getContents()} on older servers (equivalent for the non-player inventories Slimefun
 * uses it on).
 */
public final class InventoryCompat {

    private InventoryCompat() {}

    public static ItemStack[] getStorageContents(Inventory inventory) {
        Object result = ReflectionCompat.invoke(inventory, "getStorageContents");
        return result instanceof ItemStack[] ? (ItemStack[]) result : inventory.getContents();
    }

    /**
     * {@code Inventory#isEmpty()} is 1.20+. Falls back to scanning {@code getContents()} for any non-null,
     * non-air stack on older servers.
     */
    public static boolean isEmpty(Inventory inventory) {
        Object result = ReflectionCompat.invoke(inventory, "isEmpty");

        if (result instanceof Boolean) {
            return (Boolean) result;
        }

        for (ItemStack item : inventory.getContents()) {
            if (item != null && item.getType() != org.bukkit.Material.AIR) {
                return false;
            }
        }

        return true;
    }
}
