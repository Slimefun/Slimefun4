package me.mrCookieSlime.Slimefun.Setup;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;

public final class SlimefunManager {

    private SlimefunManager() {}

    /**
     * Checks if two items are similar.
     * 
     * @deprecated Use {@link SlimefunUtils#isItemSimilar(ItemStack, ItemStack, boolean)} instead
     * 
     * @param item
     *            the item
     * @param sfitem
     *            the other item
     * @param checkLore
     *            Whether to compare lore
     * @return Whether the items are similar
     */
    @Deprecated
    public static boolean isItemSimilar(ItemStack item, ItemStack sfitem, boolean checkLore) {
        return SlimefunUtils.isItemSimilar(item, sfitem, checkLore);
    }

    /**
     * Checks if the Inventory has a similar item
     * 
     * @deprecated Use {@link SlimefunUtils#containsSimilarItem(Inventory, ItemStack, boolean)}
     * 
     * @param inventory
     *            the Inventory
     * @param itemStack
     *            the item
     * @param checkLore
     *            Whether to compare lore
     * @return Whether here is such an item
     */
    @Deprecated
    public static boolean containsSimilarItem(Inventory inventory, ItemStack itemStack, boolean checkLore) {
        return SlimefunUtils.containsSimilarItem(inventory, itemStack, checkLore);
    }
}
