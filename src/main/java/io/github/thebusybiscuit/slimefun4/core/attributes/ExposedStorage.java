package io.github.thebusybiscuit.slimefun4.core.attributes;

import javax.annotation.Nonnull;

import org.bukkit.Location;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;

import java.util.Map;

/**
 * This interface, when attached to a {@link SlimefunItem}, provides methods for the item to
 * expose its contents, for when {@link Inventory#getContents} may not be a suitable reflection
 * of the Storages content.
 * Examples include deep storage barrels or inventories with multiple pages.
 * 
 * It also adds methods for requesting items and adding items directly into storage
 * 
 * @author Sefiraat
 * 
 */
public interface ExposedStorage extends ItemAttribute {

    /**
     * This method should return the full contents of the block in question
     * summarized into a map of {@link ItemStack} and total amount stored.
     * The {@link ItemStack} is representative only and shouldn't be used
     * directly.
     *
     * An {@link ItemStack} should only appear once in the Map regardless of
     * how many may be in storage. E.g. if you have 2 stacks of stone, you would
     * do Map.put(stoneStack, 128);
     *
     * @param location
     *          The {@link Location} of the storage block that the content request
     *
     * @return A {@link Map} that lists all exposed items and their quantity
     */
    @Nonnull Map<ItemStack, Integer> getStoredItems(@Nonnull Location location);

    /**
     * This method should is used to withdraw an item from the storage.
     * The volume of the matching {@link ItemStack} within storage should be reduced
     * during/after withdrawal.
     *
     * @param location
     *          The {@link Location} of the storage block that the withdrawal request is for
     * @param similarStack
     *          An {@link ItemStack} that will be used to match against stored items.
     * @param amount
     *          The amount of items requested for withdrawal, capped
     *
     * @return The number of items successfully removed from the storage to up {@param amount}
     */
    int withdrawItem(@Nonnull Location location, @Nonnull ItemStack similarStack, int amount);

    /**
     * This method is used to submit a single {@link ItemStack} into the
     * {@link ExposedStorage}. By default, this just calls {@link #depositItemStacks}
     *
     * The {@link ItemStack} should have its amount reduced as appropriate during
     * consumption, so it can be checked by the requester to return items to their
     * starting point.
     *
     * @param location
     *          The {@link Location} of the storage block that the deposit request is for
     * @param itemToDeposit
     *          The {@link ItemStack} to be added into storage
     */
    default void depositItemStack(@Nonnull Location location, @Nonnull ItemStack itemToDeposit) {
        depositItemStacks(location, new ItemStack[]{itemToDeposit});
    }

    /**
     * This method is used to submit an array of {@link ItemStack}s into the
     * {@link ExposedStorage}.
     *
     * Each {@link ItemStack} should have its amount reduced as appropriate during
     * consumption, so it can be checked by the requester to return items to their
     * starting point.
     *
     * @param location
     *          The {@link Location} of the storage block that the deposit request is for
     * @param itemsToDeposit
     *          The {@link ItemStack} array to be added into storage
     */
    void depositItemStacks(@Nonnull Location location, @Nonnull ItemStack[] itemsToDeposit);

    boolean contains(@Nonnull Location location, @Nonnull ItemStack testItemStack);
}
