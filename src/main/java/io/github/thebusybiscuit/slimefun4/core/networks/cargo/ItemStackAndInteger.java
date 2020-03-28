package io.github.thebusybiscuit.slimefun4.core.networks.cargo;

import org.bukkit.inventory.ItemStack;

class ItemStackAndInteger {

    private final ItemStack item;
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

    public void add(int amount) {
        number += amount;
    }

}
