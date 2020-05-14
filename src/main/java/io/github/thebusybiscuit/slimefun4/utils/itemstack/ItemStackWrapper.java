package io.github.thebusybiscuit.slimefun4.utils.itemstack;

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
 *
 */
public final class ItemStackWrapper extends ItemStack {

    private final ItemMeta meta;
    private final boolean hasItemMeta;

    public ItemStackWrapper(ItemStack item) {
        super(item.getType());
        meta = item.getItemMeta();
        hasItemMeta = item.hasItemMeta();
    }

    @Override
    public boolean hasItemMeta() {
        return hasItemMeta;
    }

    @Override
    public ItemMeta getItemMeta() {
        // This method normally always does a .clone() operation which can be very slow.
        // Since this class is immutable, we can simply let the super class create one copy
        // and then store that instead of creating a clone everytime.
        // This will significantly speed up any loop comparisons if used correctly.
        return meta;
    }

    @Override
    public int getAmount() {
        return 1;
    }

    @Override
    public boolean equals(Object obj) {
        throw new UnsupportedOperationException("ItemStackWrappers do not allow .equals()");
    }

    @Override
    public int hashCode() {
        throw new UnsupportedOperationException("You cannot hash an ItemStackWrapper");
    }

    @Override
    public ItemStack clone() {
        throw new UnsupportedOperationException("You cannot clone an ItemStackWrapper");
    }

    @Override
    public void setType(Material type) {
        throw new UnsupportedOperationException("ItemStackWrappers are immutable and not indended for actual usage.");
    }

    @Override
    public void setAmount(int amount) {
        throw new UnsupportedOperationException("ItemStackWrappers are immutable and not indended for actual usage.");
    }

    @Override
    public boolean setItemMeta(ItemMeta itemMeta) {
        throw new UnsupportedOperationException("ItemStackWrappers are immutable and not indended for actual usage.");
    }

    @Override
    public void addUnsafeEnchantment(Enchantment ench, int level) {
        throw new UnsupportedOperationException("ItemStackWrappers are immutable and not indended for actual usage.");
    }

}
