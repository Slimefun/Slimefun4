package io.github.thebusybiscuit.slimefun4.core.networks.cargo;

import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.utils.itemstack.ItemStackWrapper;

class ItemStackAndInteger {

    private final ItemStack item;
    private ItemStackWrapper wrapper;
    private int number;

    ItemStackAndInteger(ItemStack item, int amount) {
        this.number = amount;
        if (item instanceof ItemStackWrapper) {
            this.item = new ItemStack(item.getType(), item.getAmount());
            if (item.hasItemMeta()) {
                this.item.setItemMeta(item.getItemMeta());
            }
        } else {
            this.item = item;
        }
    }

    public int getInt() {
        return number;
    }

    public ItemStack getItem() {
        return item;
    }

    public ItemStackWrapper getItemStackWrapper() {
        if (wrapper == null && item != null) {
            wrapper = ItemStackWrapper.ofItem(item);
        }

        return wrapper;
    }

    public void add(int amount) {
        number += amount;
    }

}
