package io.github.thebusybiscuit.slimefun4.implementation.listeners;

import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import javax.annotation.Nonnull;

import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.implementation.items.cargo.CargoNode;

/**
 * This {@link Listener} is solely responsible for preventing Cargo Nodes from being placed
 * on the top or bottom of a block.
 * 
 * @author TheBusyBiscuit
 *
 */
public class CargoNodeListener implements Listener {

    public CargoNodeListener(@Nonnull Slimefun plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(ignoreCancelled = true)
    public void onCargoNodePlace(BlockPlaceEvent e) {
        Block b = e.getBlock();

        if ((b.getY() != e.getBlockAgainst().getY() || !e.getBlockReplacedState().getType().isAir()) && isCargoNode(e.getItemInHand())) {
            Slimefun.getLocalization().sendMessage(e.getPlayer(), "machines.CARGO_NODES.must-be-placed", true);
            e.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onFilterMove(InventoryClickEvent e) {
        String invName = e.getWhoClicked().getOpenInventory().getTitle();
        if ((invName.equals("Cargo Node (Input)") || invName.equals("Advanced Cargo Node (Output)"))) {
            if (Slimefun.getCfg().getBoolean("options.allow-custom-items-in-cargo-filters")) {
                return;
            }

            if (e.getClick() == ClickType.NUMBER_KEY) {
                e.setCancelled(true);
                return;
            }

            ItemStack item = e.getCurrentItem();
            if (item == null) {
                return;
            }

            SlimefunItem sfItem = SlimefunItem.getByItem(item);
            if (sfItem == null && item.getItemMeta() != new ItemStack(item.getType()).getItemMeta()) {
                Slimefun.getLocalization().sendMessage(e.getWhoClicked(), "machines.CARGO_NODES.no-custom-items", true);
                e.setCancelled(true);
            }
        }
    }

    private boolean isCargoNode(@Nonnull ItemStack item) {
        return SlimefunItem.getByItem(item) instanceof CargoNode;
    }
}
