package io.github.thebusybiscuit.slimefun4.core.networks.cargo;

import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.utils.itemstack.ItemStackWrapper;

class ItemStackAndInteger {

    private final ItemStack item;
    private ItemStackWrapper wrapper;
    private int number;

    ItemStackAndInteger(ItemStack item, int amount) {
        this.number = amount;
        this.item = item;
    }

    public int getInt() {
        return number;
    }

    public ItemStack getItem() {
        return item;
    }

    public ItemStackWrapper getItemStackWrapper() {
        if (wrapper == null && item != null) {
            wrapper = new ItemStackWrapper(item);
        }

        return wrapper;
    }

    public void add(int amount) {
        number += amount;
    }

}
