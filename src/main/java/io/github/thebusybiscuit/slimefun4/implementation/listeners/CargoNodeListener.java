package io.github.thebusybiscuit.slimefun4.implementation.listeners;

import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import io.github.thebusybiscuit.slimefun4.utils.itemstack.ItemStackWrapper;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

/**
 * This {@link Listener} is solely responsible for preventing Cargo Nodes from being placed
 * on the top or bottom of a block.
 *
 * @author TheBusyBiscuit
 */
public class CargoNodeListener implements Listener {

    public CargoNodeListener(SlimefunPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(ignoreCancelled = true)
    public void onCargoNodePlace(BlockPlaceEvent e) {
        if (e.getBlock().getY() != e.getBlockAgainst().getY() && isCargoNode(new ItemStackWrapper(e.getItemInHand()))) {
            SlimefunPlugin.getLocal().sendMessage(e.getPlayer(), "machines.CARGO_NODES.must-be-placed", true);
            e.setCancelled(true);
        }
    }

    private boolean isCargoNode(ItemStack item) {
        return SlimefunUtils.isItemSimilar(item, SlimefunItems.CARGO_INPUT, false)
                || SlimefunUtils.isItemSimilar(item, SlimefunItems.CARGO_OUTPUT, false)
                || SlimefunUtils.isItemSimilar(item, SlimefunItems.CARGO_OUTPUT_ADVANCED, false);
    }
}