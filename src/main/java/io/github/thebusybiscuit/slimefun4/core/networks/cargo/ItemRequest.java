package io.github.thebusybiscuit.slimefun4.core.networks.cargo;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.Slimefun.api.item_transport.ItemTransportFlow;

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
