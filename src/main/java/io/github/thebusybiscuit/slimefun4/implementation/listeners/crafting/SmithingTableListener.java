package io.github.thebusybiscuit.slimefun4.implementation.listeners.crafting;

import javax.annotation.Nonnull;

import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;

/**
 * This {@link Listener} prevents any {@link SlimefunItem} from being used in a
 * smithing table.
 * 
 * @author Sefiraat
 */
public class SmithingTableListener implements SlimefunCraftingListener {

    public SmithingTableListener(@Nonnull Slimefun plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(ignoreCancelled = true)
    public void onSmith(InventoryClickEvent e) {
        if (e.getInventory().getType() == InventoryType.SMITHING && e.getRawSlot() == 2 && e.getWhoClicked() instanceof Player) {
            ItemStack materialItem = e.getInventory().getContents()[1];

            // Checks if the item in the Material/Netherite slot is allowed to be used.
            if (isUnallowed(materialItem)) {
                e.setResult(Result.DENY);
                Slimefun.getLocalization().sendMessage(e.getWhoClicked(), "smithing_table.not-working", true);
            }
        }
    }
}
