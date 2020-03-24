package me.mrCookieSlime.Slimefun.api.item_transport;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

class ItemRequest {

    private final ItemStack item;
    private final ItemTransportFlow flow;
    private final Location terminal;
    private final int slot;

    ItemRequest(Location terminal, int slot, ItemStack item, ItemTransportFlow flow) {
        this.terminal = terminal;
        this.item = item;
        this.slot = slot;
        this.flow = flow;
    }

    public Location getTerminal() {
        return terminal;
    }

    public ItemStack getItem() {
        return item;
    }

    public ItemTransportFlow getDirection() {
        return flow;
    }

    public int getSlot() {
        return slot;
    }

}
