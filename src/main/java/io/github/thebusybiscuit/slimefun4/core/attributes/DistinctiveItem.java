package io.github.thebusybiscuit.slimefun4.core.attributes;

import org.bukkit.inventory.meta.ItemMeta;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;

import javax.annotation.Nonnull;

/**
 * Implement this interface for any {@link SlimefunItem} to prevent
 * cargo using only its ID when comparing. #canStack is used when
 * comparing stacks
 *
 * @author Sefiraat
 */
public interface DistinctiveItem extends ItemAttribute {

    /**
     * This method is called by {@link SlimefunUtils#isItemSimilar} when two {@link SlimefunItemStack}
     * IDs match on a DistinctiveItem and should return if the two items can stack
     * with one another.
     *
     * @param itemMetaOne
     *                    The {@link ItemMeta} of the first stack being compared.
     * @param itemMetaTwo
     *                    The {@link ItemMeta} of the second stack being compared.
     *
     * @return Whether the two {@link ItemMeta}s are distinct
     */
    boolean canStack(@Nonnull ItemMeta itemMetaOne, @Nonnull ItemMeta itemMetaTwo);
}
