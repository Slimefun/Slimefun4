package io.github.thebusybiscuit.slimefun4.utils.itemstack;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import org.apache.commons.lang.Validate;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * This {@link ItemStack}, which is <b>not intended for actual usage</b>, caches its {@link ItemMeta}.
 * This significantly speeds up any {@link ItemStack} comparisons a lot.
 * 
 * You cannot invoke {@link #equals(Object)}, {@link #hashCode()} or any of its setter on an
 * {@link ItemStackWrapper}.<br>
 * Please be very careful when using this.
 * 
 * @author TheBusyBiscuit
 * @author md5sha256
 *
 */
public final class ItemStackWrapper extends ItemStack {

    private static final String ERROR_MESSAGE = "ItemStackWrappers are immutable and not intended for actual usage.";

    private final ItemMeta meta;
    private final int amount;
    private final boolean hasItemMeta;

    private ItemStackWrapper(@Nonnull ItemStack item) {
        super(item.getType());

        amount = item.getAmount();
        hasItemMeta = item.hasItemMeta();

        if (hasItemMeta) {
            meta = item.getItemMeta();
        } else {
            meta = null;
        }
    }

    @Override
    public boolean hasItemMeta() {
        return hasItemMeta;
    }

    @Override
    public ItemMeta getItemMeta() {
        /*
         * This method normally always does a .clone() operation which can be very slow.
         * Since this class is immutable, we can simply let the super class create one copy
         * and then store that instead of creating a clone everytime.
         * This will significantly speed up any loop comparisons if used correctly.
         */
        if (meta == null) {
            throw new UnsupportedOperationException("This ItemStack has no ItemMeta! Make sure to check ItemStack#hasItemMeta() before accessing this method!");
        } else {
            return meta;
        }
    }

    @Override
    public int getAmount() {
        return amount;
    }

    @Override
    public boolean equals(Object obj) {
        throw new UnsupportedOperationException(ERROR_MESSAGE);
    }

    @Override
    public int hashCode() {
        throw new UnsupportedOperationException(ERROR_MESSAGE);
    }

    @Override
    public ItemStack clone() {
        throw new UnsupportedOperationException(ERROR_MESSAGE);
    }

    @Override
    public void setType(Material type) {
        throw new UnsupportedOperationException(ERROR_MESSAGE);
    }

    @Override
    public void setAmount(int amount) {
        throw new UnsupportedOperationException(ERROR_MESSAGE);
    }

    @Override
    public boolean setItemMeta(ItemMeta itemMeta) {
        throw new UnsupportedOperationException(ERROR_MESSAGE);
    }

    @Override
    public void addUnsafeEnchantment(Enchantment ench, int level) {
        throw new UnsupportedOperationException(ERROR_MESSAGE);
    }

    /**
     * Creates an {@link ItemStackWrapper} of an {@link ItemStack}. This method
     * will not check if the passed {@link ItemStack} has already been wrapped
     *
     * @param itemStack
     *            The {@link ItemStack} to wrap
     * @return Returns an {@link ItemStackWrapper} of the passed {@link ItemStack}
     * @see #wrap(ItemStack)
     */
    public static @Nonnull ItemStackWrapper forceWrap(@Nonnull ItemStack itemStack) {
        Validate.notNull(itemStack, "The ItemStack cannot be null!");

        return new ItemStackWrapper(itemStack);
    }

    /**
     * Creates an {@link ItemStackWrapper} of an {@link ItemStack}. This method
     * will return the the casted reference of the passed {@link ItemStack} if it
     * is already an {@link ItemStackWrapper}
     *
     * @param itemStack
     *            The {@link ItemStack} to wrap
     * @return Returns an {@link ItemStackWrapper} of the passed {@link ItemStack}
     * @see #forceWrap(ItemStack)
     */
    public static @Nonnull ItemStackWrapper wrap(@Nonnull ItemStack itemStack) {
        Validate.notNull(itemStack, "The ItemStack cannot be null!");

        if (itemStack instanceof ItemStackWrapper) {
            return (ItemStackWrapper) itemStack;
        }

        return new ItemStackWrapper(itemStack);
    }

    /**
     * This creates an {@link ItemStackWrapper} array from a given {@link ItemStack} array.
     * 
     * @param items
     *            The array of {@link ItemStack ItemStacks} to transform
     * 
     * @return An {@link ItemStackWrapper} array
     */
    public static @Nonnull ItemStackWrapper[] wrapArray(@Nonnull ItemStack[] items) {
        Validate.notNull(items, "The array must not be null!");

        ItemStackWrapper[] array = new ItemStackWrapper[items.length];

        for (int i = 0; i < items.length; i++) {
            if (items[i] != null) {
                array[i] = wrap(items[i]);
            }
        }

        return array;
    }

    /**
     * This creates an {@link ItemStackWrapper} {@link List} from a given {@link ItemStack} {@link List} *
     * 
     * @param items
     *            The {@link List} of {@link ItemStack ItemStacks} to transform
     * 
     * @return An {@link ItemStackWrapper} array
     */
    public static @Nonnull List<ItemStackWrapper> wrapList(@Nonnull List<ItemStack> items) {
        Validate.notNull(items, "The list must not be null!");
        List<ItemStackWrapper> list = new ArrayList<>(items.size());

        for (ItemStack item : items) {
            if (item != null) {
                list.add(wrap(item));
            } else {
                list.add(null);
            }
        }

        return list;
    }

}
